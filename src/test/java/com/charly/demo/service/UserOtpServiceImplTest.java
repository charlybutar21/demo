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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserOtpServiceImplTest {

    @Mock
    private Config config;

    @Mock
    private UserOtpRepository userOtpRepository;

    @Mock
    private OtpStatusRepository otpStatusRepository;

    @InjectMocks
    private UserOtpServiceImpl userOtpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateOtpSuccess() {
        OtpRequest request = new OtpRequest();
        request.setUserId("userId");

        when(config.getOtpExpirationMinutes()).thenReturn(2L);

        OtpStatus created = new OtpStatus(1L, OtpStatusType.CREATED.toString());
        when(otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString())).thenReturn(Optional.of(created));

        OtpResponse response = userOtpService.generateOtp(request);
        assertNotNull(response);
        assertEquals("userId", response.getUserId());
    }

    @Test
    void generateOtpFailed() {
        OtpRequest request = new OtpRequest();
        request.setUserId("userId");

        when(config.getOtpExpirationMinutes()).thenReturn(2L);
        when(otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userOtpService.generateOtp(request);
        });
    }

    @Test
    void validateOtpSuccess() throws OtpValidationException {
        ValidateOtpRequest request = new ValidateOtpRequest();
        request.setUserId("userId");
        request.setOtp("12345");

        OtpStatus created = new OtpStatus(1L, OtpStatusType.CREATED.toString());
        when(otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString())).thenReturn(Optional.of(created));

        UserOtp userOtp = new UserOtp();
        userOtp.setId(1L);
        userOtp.setStatus(created);
        userOtp.setUserId("userId");
        userOtp.setExpiryTime(LocalDateTime.now().plusMinutes(2));
        when(userOtpRepository.findUserOtpByUserIdAndStatusId("userId", created.getId())).thenReturn(Optional.of(userOtp));

        OtpStatus validated = new OtpStatus(2L, OtpStatusType.VALIDATED.toString());
        when(otpStatusRepository.findByStatus(OtpStatusType.VALIDATED.toString())).thenReturn(Optional.of(validated));

        ValidateOtpResponse response = userOtpService.validateOtp(request);
        assertNotNull(response);

    }

    @Test
    void validateOtpExpired() throws OtpValidationException {
        ValidateOtpRequest request = new ValidateOtpRequest();
        request.setUserId("userId");
        request.setOtp("12345");

        OtpStatus created = new OtpStatus(1L, OtpStatusType.CREATED.toString());
        when(otpStatusRepository.findByStatus(OtpStatusType.CREATED.toString())).thenReturn(Optional.of(created));

        UserOtp userOtp = new UserOtp();
        userOtp.setId(1L);
        userOtp.setStatus(created);
        userOtp.setUserId("userId");
        userOtp.setExpiryTime(LocalDateTime.now().minusMinutes(2));
        when(userOtpRepository.findUserOtpByUserIdAndStatusId("userId", created.getId())).thenReturn(Optional.of(userOtp));

        OtpStatus expired = new OtpStatus(2L, OtpStatusType.EXPIRED.toString());
        when(otpStatusRepository.findByStatus(OtpStatusType.EXPIRED.toString())).thenReturn(Optional.of(expired));

        assertThrows(OtpValidationException.class, () -> {
            userOtpService.validateOtp(request);
        });

    }
}