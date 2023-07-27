package com.charly.demo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageType {

    STATUS_TYPE_CREATED_NOT_FOUND("status_type_created_not_found", "Status type Created not found"),
    STATUS_TYPE_VALIDATED_NOT_FOUND("status_type_validated_not_found", "Status type Validated not found"),
    STATUS_TYPE_EXPIRED_NOT_FOUND("status_type_expired_not_found", "Status type Expired not found"),
    OTP_NOT_FOUND("otp_not_found", "OTP Not Found"),
    OTP_EXPIRED("otp_expired", "OTP has expired"),
    OTP_INVALID("otp_invalid", "OTP is invalid"),
    // Add more error codes here as needed
    ;

    private final String error;
    private final String description;

    public static ErrorMessageType findByError(String error){
        for (ErrorMessageType type : ErrorMessageType.values()) {
            if (type.getError().equals(error)) {
                return type;
            }
        }
        return null;
    }


}
