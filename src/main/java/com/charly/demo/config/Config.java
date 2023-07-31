package com.charly.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Getter
    @Value("${otp.validation.success.message}")
    private String otpValidationSuccessMessage;

}
