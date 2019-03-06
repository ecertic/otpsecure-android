package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureError;

import androidx.annotation.Nullable;

public class PermissionException extends AuthenticationException {

    public PermissionException(@Nullable String message, @Nullable String requestId,
                               @Nullable Integer statusCode, @Nullable OtpSecureError otpsecureError) {
        super(message, requestId, statusCode, otpsecureError);
    }
}