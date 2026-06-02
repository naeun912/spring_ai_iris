package com.nhnacademy.springaiirisproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IrisViewController {
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
