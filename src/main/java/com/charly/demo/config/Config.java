package com.charly.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("${otp.expiration.minutes}")
    private int otpExpirationMinutes;

    @Getter
    @Value("${otp.validation.success.message}")
    private String otpValidationSuccessMessage;

    public Long getOtpExpirationMinutes(){
        return Long.valueOf(otpExpirationMinutes);
    }

}
