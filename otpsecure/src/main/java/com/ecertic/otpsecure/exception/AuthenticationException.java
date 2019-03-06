package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureError;

import androidx.annotation.Nullable;

/**
 * An {@link Exception} that represents a failure to authenticate with OtpSecure API.
 */
public class AuthenticationException extends OtpSecureException {

    public AuthenticationException(@Nullable String message, @Nullable String requestId,
                                   @Nullable Integer statusCode,
                                   @Nullable OtpSecureError otpsecureError) {
        super(message, requestId, statusCode, otpsecureError);
    }
}