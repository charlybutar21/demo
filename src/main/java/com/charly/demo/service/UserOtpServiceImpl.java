package com.charly.demo.service;

import com.charly.demo.config.Config;
import com.charly.demo.dto.request.OtpRequest;
import com.charly.demo.dto.request.ValidateOtpRequest;
import com.charly.demo.dto.response.OtpResponse;
import com.charly.demo.dto.response.ValidateOtpResponse;
import com.charly.demo.entity.OtpStatus;
import com.charly.demo.entity.OtpStatusType;
import com.charly.demo.entity.UserOtp;
import com.charly.demo.exception.OtpValidationException;
import com.charly.demo.repository.OTPCacheRepository;
import com.charly.demo.repository.OtpStatusRepository;
import com.charly.demo.repository.UserOtpRepository;
import com.charly.demo.util.ErrorMessageType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserOtpServiceImpl implements UserOtpService {

    private Config config;

    private OTPCacheRepository otpCacheRepository;

    @Override
    public OtpResponse generateOtp(OtpRequest request) {
        Random random = new Random();
        String otp = String.format("%05d", random.nextInt(100000));

        otpCacheRepository.put(request.getUserId(), Integer.parseInt(otp));

        return OtpResponse.builder().
                userId(request.getUserId()).
                otp(otp).
                build();
    }

    @Override
    public ValidateOtpResponse validateOtp(ValidateOtpRequest request) throws OtpValidationException {

        Optional<String> cachedOtp = otpCacheRepository.get(request.getUserId());

        if (cachedOtp.isPresent() && cachedOtp.get().equals(request.getOtp())) {
            otpCacheRepository.remove(request.getUserId());
            return ValidateOtpResponse.builder()
                    .userId(request.getUserId())
                    .message(config.getOtpValidationSuccessMessage())
                    .build();

        } else if (cachedOtp.isPresent() && !cachedOtp.get().equals(request.getOtp())) {
            throw new OtpValidationException(ErrorMessageType.OTP_EXPIRED.getError());
        } else {
            throw new OtpValidationException(ErrorMessageType.OTP_INVALID.getError());
        }
    }
}
