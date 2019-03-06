package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A base class for OtpSecure-related {@link Exception Exceptions}.
 */
public abstract class OtpSecureException extends Exception {

    protected static final long serialVersionUID = 1L;

    @Nullable
    private final String mRequestId;
    @Nullable
    private final Integer mStatusCode;
    @Nullable
    private final OtpSecureError mOtpSecureError;

    OtpSecureException(String message, String requestId,
                       Integer statusCode, Throwable e) {
        this(null, message, requestId, statusCode, e);
    }

    OtpSecureException(@Nullable OtpSecureError otpsecureError, @Nullable String message, @Nullable String requestId,
                       @Nullable Integer statusCode, @Nullable Throwable e) {
        super(message, e);
        mOtpSecureError = otpsecureError;
        mStatusCode = statusCode;
        mRequestId = requestId;
    }

    @Nullable
    public OtpSecureError getOtpSecureError() {
        return mOtpSecureError;
    }

    @Nullable
    public Integer getStatusCode() {
        return mStatusCode;
    }

    @NonNull
    @Override
    public String toString() {
        final String reqIdStr;
        if (mRequestId != null) {
            reqIdStr = "; request-id: " + mRequestId;
        } else {
            reqIdStr = "";
        }
        return super.toString() + reqIdStr;
    }
}
