package com.busify.project.chat.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai.api")
@Data
@Slf4j
public class OpenAIConfig {
    
    private String key;
    private String model = "meta-llama/llama-3.3-8b-instruct:free";
    private int timeout = 30;
    private int maxTokens = 500;
    private double temperature = 0.7;
    
    public String getKey() {
        return key;
    }
    
    public String getModel() {
        return model;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public int getMaxTokens() {
        return maxTokens;
    }
    
    public double getTemperature() {
        return temperature;
    }
}