package com.nhnacademy.springaiirisproject.config;

import com.nhnacademy.springaiirisproject.service.IrisModelService;
import lombok.RequiredArgsConstructor;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class MultiLayerNetWorkConfig {
    private final IrisModelService irisModelService;
    private final ModelProperties modelProperties;

    @Bean
    public MultiLayerNetwork multiLayerNetWork() throws IOException{
        // 모델 초기화
        irisModelService.initModel();

        File modelFile = new File(modelProperties.getModelPath());

        MultiLayerNetwork model = null;
        if(modelFile.exists()){
            // 저장된 모델 파일을 읽어와서 객체로 복원(역직렬화)
            model = ModelSerializer.restoreMultiLayerNetwork(modelFile);
        }

        // 서비스에 로드된 모델을 설정해준다(순환 참조 방지를 위해 수정자 주입 활용)
        irisModelService.setModel(model);

        return model;
    }
}
