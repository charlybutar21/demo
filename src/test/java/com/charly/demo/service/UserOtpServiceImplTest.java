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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserOtpServiceImplTest {

    @Mock
    private Config config;

    @Mock
    private OTPCacheRepository otpCacheRepository;

    @InjectMocks
    private UserOtpServiceImpl userOtpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateOtpSuccess() {
        OtpRequest request = new OtpRequest("userId");

        OtpResponse response = userOtpService.generateOtp(request);
        assertNotNull(response);
        verify(otpCacheRepository).put(anyString(), anyInt());
        assertEquals("userId", response.getUserId());
        assertEquals(5, response.getOtp().length());
    }

    @Test
    void generateOtpFailed() {
        OtpRequest request = new OtpRequest("user123");

        doThrow(new RuntimeException("Error while retrieving from the cache ")).when(otpCacheRepository).put(anyString(), anyInt());

        assertThrows(RuntimeException.class, () -> userOtpService.generateOtp(request));
    }

   @Test
    void validateOtpSuccess() throws OtpValidationException {
        ValidateOtpRequest request = new ValidateOtpRequest("userId", "12345");

        when(otpCacheRepository.get(request.getUserId())).thenReturn(Optional.of("12345"));
        when(config.getOtpValidationSuccessMessage()).thenReturn("OTP validated successfully.");

        ValidateOtpResponse response = userOtpService.validateOtp(request);
        assertNotNull(response);
        verify(otpCacheRepository).remove(request.getUserId());
        assertEquals(request.getUserId(), response.getUserId());
        assertEquals("OTP validated successfully.", response.getMessage());

    }

    @Test
    void validateOtpExpired() throws OtpValidationException {
        ValidateOtpRequest request = new ValidateOtpRequest("userId", "12345");

        when(otpCacheRepository.get(request.getUserId())).thenReturn(Optional.of("54321"));

        assertThrows(OtpValidationException.class, () -> userOtpService.validateOtp(request));

    }

    @Test
    void validateOtpInvalid() throws OtpValidationException {
        ValidateOtpRequest request = new ValidateOtpRequest("userId", "12345");

        when(otpCacheRepository.get(request.getUserId())).thenReturn(Optional.empty());

        assertThrows(OtpValidationException.class, () -> userOtpService.validateOtp(request));

    }
}