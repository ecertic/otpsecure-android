package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureError;

import androidx.annotation.Nullable;

/**
 * An {@link Exception} indicating that invalid parameters were used in a request.
 */
public class InvalidRequestException extends OtpSecureException {

    @Nullable
    private final String mParam;
    @Nullable
    private final String mErrorCode;

    public InvalidRequestException(@Nullable String message, @Nullable String param,
                                   @Nullable String requestId, @Nullable Integer statusCode,
                                   @Nullable String errorCode, @Nullable OtpSecureError otpsecureError, @Nullable Throwable e) {
        super(otpsecureError, message, requestId, statusCode, e);
        mParam = param;
        mErrorCode = errorCode;
    }

    @Nullable
    public String getParam() {
        return mParam;
    }

    @Nullable
    public String getErrorCode() {
        return mErrorCode;
    }

}
