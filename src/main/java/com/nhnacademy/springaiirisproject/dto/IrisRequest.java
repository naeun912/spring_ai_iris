package com.nhnacademy.springaiirisproject.dto;

import lombok.Value;

/**
 * 클라이언트로부터 전달받는 붓꽃 특징 데이터
 * 붓꽃(Iris) 예측에 필요한 4가지 특징(Feature)
 * @param sepalLength 꽃받침 길이
 * @param sepalWidth 꽃받침 너비
 * @param petalLength 꽃잎 길이
 * @param petalWidth 꽃잎 너비
 */
//@Value
public record IrisRequest(
        double sepalLength,
        double sepalWidth,
        double petalLength,
        double petalWidth
) {}

