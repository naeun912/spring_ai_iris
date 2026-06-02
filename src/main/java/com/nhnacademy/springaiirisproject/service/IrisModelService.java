package com.nhnacademy.springaiirisproject.service;

import com.nhnacademy.springaiirisproject.config.ModelProperties;
import com.nhnacademy.springaiirisproject.dto.IrisRequest;
import com.nhnacademy.springaiirisproject.dto.IrisResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.deeplearning4j.datasets.iterator.impl.IrisDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IrisModelService {
    private final ModelProperties modelProperties;
    @Setter
    private MultiLayerNetwork model;

    @EventListener(ApplicationReadyEvent.class) // 서버 시작 직후 실행되는 메서드로 지정??
    public void initModel() throws IOException {
        File modelFile = new File(modelProperties.getModelPath());

        if(modelFile.exists()){
            log.info("학습된 모델이 이미 존재합니다. 자동 학습을 건너뜁니다.");
            return;
        }

        log.info("모델 파일이 없습니다. 학습을 시작합니다...");
        trainAndSaveModel();
    }

    //학습 시키느 메소드
    private void trainAndSaveModel() throws IOException{
        // 데이터 준비 DataSetIterator
        DataSetIterator trainIter = new IrisDataSetIterator(150, 150);

        // 신경망 설계(?) Configuration
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(123)
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .list()
                .layer(new DenseLayer.Builder().nIn(4).nOut(10).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(10).nOut(3).build())
                .build();

        // 모델 초기화 및 학습
        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();

        for(int i = 0; i<300; i++){
            model.fit(trainIter);
        }

        // 모델 저장 Serialization
        ModelSerializer.writeModel(model, new File(modelProperties.getModelPath()), true);
        log.info("모델 학습 및 저장이 완료되었습니다: {}", modelProperties.getModelPath());
    }

    /**
     * 사용자의 입력 데이터를 기반으로 붓꽃 품종을 예측
     */
    public IrisResponse predict(IrisRequest request){
        if(model == null){
            throw new IllegalStateException("모델이 로드되지 않았습니다.");
        }

        // 입력을 INDArray(행렬)로 변환 (1행 4열)
        // 자바의 기본 double 데이터를 딥러닝 엔진이 이해할 수 있는 행렬로 변환
        INDArray input = Nd4j.create(new double[] []{{
                request.sepalLength(),
                request.sepalWidth(),
                request.petalLength(),
                request.petalWidth()
        }});

        // 모델에 값을 넣어 결과를 얻음(Inference)
        INDArray output = model.output(input);

        // 결과 해석 (가장 높은 확률을 가진 인덱스 찾기)
        double[] probabilities = output.toDoubleVector();
        String[] labels = {"Setosa", "Versicolor", "Virginica"};


        Map<String, Double> probMap = new HashMap<>();
        int maxIdx = 0;

        for(int i =0; i < labels.length; i++){
            probMap.put(labels[i], probabilities[i]);
            if(probabilities[i] > probabilities[maxIdx]){
                maxIdx = i;
            }
        }

        // 모델이 반환한 확률 값 중 가장 높은 값을 가진 품종을 찾아 IrisResponse에 담아 반환한다.
        return new IrisResponse(labels[maxIdx], probMap);
    }
}
