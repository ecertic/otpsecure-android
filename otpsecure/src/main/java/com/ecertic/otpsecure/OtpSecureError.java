package com.ecertic.otpsecure;

import androidx.annotation.Nullable;

public class OtpSecureError extends Error {

    @Nullable
    public final String type;
    @Nullable
    public final String message;

    @Nullable
    public final String code;
    @Nullable
    public final String param;

    OtpSecureError(@Nullable String type, @Nullable String message, @Nullable String code,
                   @Nullable String param) {
        this.type = type;
        this.message = message;
        this.code = code;
        this.param = param;
    }

}
