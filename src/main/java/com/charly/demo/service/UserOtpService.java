package com.charly.demo.service;

import com.charly.demo.dto.request.OtpRequest;
import com.charly.demo.dto.request.ValidateOtpRequest;
import com.charly.demo.dto.response.OtpResponse;
import com.charly.demo.dto.response.ValidateOtpResponse;
import com.charly.demo.exception.OtpValidationException;

public interface UserOtpService {
    OtpResponse generateOtp(OtpRequest request) ;
    ValidateOtpResponse validateOtp(ValidateOtpRequest request) throws OtpValidationException;
}
