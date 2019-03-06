package com.ecertic.otpsecure.exception;

import androidx.annotation.Nullable;

/**
 * An {@link Exception} that represents a failure to connect to OtpSecure API.
 */
public class APIConnectionException extends OtpSecureException {

    public APIConnectionException(@Nullable String message) {
        this(message, null);
    }

    public APIConnectionException(@Nullable String message, @Nullable Throwable e) {
        super(null, message, 0, e);
    }

}