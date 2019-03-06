package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureError;

import androidx.annotation.Nullable;

/**
 * An {@link Exception} that represents an internal problem with OtpSecure API.
 */
public class APIException extends OtpSecureException {

    public APIException(@Nullable String message, @Nullable String requestId,
                        @Nullable Integer statusCode, @Nullable OtpSecureError otpsecureError, @Nullable Throwable e) {
        super(otpsecureError, message, requestId, statusCode, e);
    }
}