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
import com.charly.demo.repository.OtpStatusRepository;
import com.charly.demo.repository.UserOtpRepository;
import com.charly.demo.util.ErrorMessageType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserOtpServiceImpl implements UserOtpService {

    private Config config;

    private UserOtpRepository userOtpRepository;

    private OtpStatusRepository otpStatusRepository;

    @Override
    public OtpResponse generateOtp(OtpRequest request) {
        Random random = new Random();
        String otp = String.format("%05d", random.nextInt(100000));

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(config.getOtpExpirationMinutes());
        UserOtp userOtp = new UserOtp();
        userOtp.setUserId(request.getUserId());
        userOtp.setOtp(otp);
        userOtp.setExpiryTime(expiryTime);

        OtpStatus otpStatusCreated = otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString()).
                orElseThrow(() -> new RuntimeException(ErrorMessageType.STATUS_TYPE_CREATED_NOT_FOUND.getError()));
        userOtp.setStatus(otpStatusCreated);

        userOtpRepository.save(userOtp);

        return OtpResponse.builder().
                userId(request.getUserId()).
                otp(otp).
                build();
    }

    @Override
    public ValidateOtpResponse validateOtp(ValidateOtpRequest request) throws OtpValidationException {
        OtpStatus otpStatusCreated = otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString()).
                orElseThrow(() -> new RuntimeException(ErrorMessageType.STATUS_TYPE_CREATED_NOT_FOUND.getError()));
        UserOtp userOtp = userOtpRepository.findUserOtpByUserIdAndStatusId(request.getUserId(), otpStatusCreated.getId())
                .orElseThrow(() -> new OtpValidationException(ErrorMessageType.OTP_NOT_FOUND.getError()));

        if (userOtp.getExpiryTime().isBefore(LocalDateTime.now())){
            OtpStatus otpStatusExpired = otpStatusRepository.findByStatus(OtpStatusType.EXPIRED.toString()).
                    orElseThrow(() -> new RuntimeException(ErrorMessageType.STATUS_TYPE_EXPIRED_NOT_FOUND.getError()));
            userOtp.setStatus(otpStatusExpired);
            userOtpRepository.save(userOtp);
            throw new OtpValidationException(ErrorMessageType.OTP_EXPIRED.getError());
        }

        OtpStatus otpStatusValidated = otpStatusRepository.findByStatus(OtpStatusType.VALIDATED.toString()).
                    orElseThrow(() -> new RuntimeException(ErrorMessageType.STATUS_TYPE_VALIDATED_NOT_FOUND.getError()));
        userOtp.setStatus(otpStatusValidated);
        userOtpRepository.save(userOtp);

        return ValidateOtpResponse.builder()
                .userId(userOtp.getUserId())
                .message(config.getOtpValidationSuccessMessage())
                .build();
    }
}
