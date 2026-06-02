package com.nhnacademy.springaiirisproject.dto;

import lombok.Value;

import java.util.Map;

/**
 * AI 모델의 예측 결과 및 확률 분호 데이터
 * @param predictedSpecies 예측된 품종명 (ex: Setosa)
 * @param probabilities 각 품종별 확률 (ex: {SetosaL 0.95, ...})
 */
//@Value
public record IrisResponse(String predictedSpecies,
                            Map<String, Double> probabilities) {}
