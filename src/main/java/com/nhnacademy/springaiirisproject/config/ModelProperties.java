package com.nhnacademy.springaiirisproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "iris")
@Component
@Getter
@Setter
public class ModelProperties {
    private String modelPath;
}
