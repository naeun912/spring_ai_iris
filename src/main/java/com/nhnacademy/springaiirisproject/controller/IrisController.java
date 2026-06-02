package com.nhnacademy.springaiirisproject.controller;

import com.nhnacademy.springaiirisproject.dto.IrisRequest;
import com.nhnacademy.springaiirisproject.dto.IrisResponse;
import com.nhnacademy.springaiirisproject.service.IrisModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스의 모든 응답은 JSON 형태로 반환
@RequiredArgsConstructor
@RequestMapping("/api/iris")
public class IrisController {
    private final IrisModelService irisModelService;

    @PostMapping("/predict")
    public IrisResponse predict(@RequestBody IrisRequest request){
        // 클라이언트로부터 전달받은 json 데이터를 irisRequest 객체로 매핑하여 처리
        return irisModelService.predict(request);
    }
}
