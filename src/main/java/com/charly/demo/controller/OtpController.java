package com.charly.demo.controller;

import com.charly.demo.dto.request.OtpRequest;
import com.charly.demo.dto.request.ValidateOtpRequest;
import com.charly.demo.dto.response.ErrorResponse;
import com.charly.demo.dto.response.OtpResponse;
import com.charly.demo.dto.response.ValidateOtpResponse;
import com.charly.demo.exception.OtpValidationException;
import com.charly.demo.service.UserOtpService;
import com.charly.demo.util.ErrorMessageType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OtpController {

    private final UserOtpService userOtpService;

    @PostMapping("/otp/request")
    public ResponseEntity<?> generateOtp(@RequestBody OtpRequest request) {
        try {
            OtpResponse response = userOtpService.generateOtp(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                    .error(e.getMessage())
                    .errorDescription(ErrorMessageType.findByError(e.getMessage()).getDescription())
                    .build());
        }

    }

    @PostMapping("/otp/validate")
    public  ResponseEntity<?> validateOtp(@RequestBody ValidateOtpRequest request) throws OtpValidationException {
        try {
            ValidateOtpResponse response = userOtpService.validateOtp(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                    .error(e.getMessage())
                    .errorDescription(ErrorMessageType.findByError(e.getMessage()).getDescription())
                    .build());

        } catch (OtpValidationException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder()
                    .error(e.getMessage())
                    .errorDescription(ErrorMessageType.findByError(e.getMessage()).getDescription())
                    .build());
        }
    }
}
