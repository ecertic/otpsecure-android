package com.ecertic.otpsecure;

import androidx.annotation.Nullable;

class OtpSecureResponse {
    private final int mResponseCode;
    @Nullable
    private final String mResponseBody;


    /**
     * Object constructor.
     *
     * @param responseCode the response code (i.e. 404)
     * @param responseBody the body of the response
     */
    public OtpSecureResponse(
            int responseCode,
            @Nullable String responseBody) {
        mResponseCode = responseCode;
        mResponseBody = responseBody;
    }

    /**
     * @return the {@link #mResponseCode response code}.
     */
    int getResponseCode() {
        return mResponseCode;
    }

    /**
     * @return the {@link #mResponseBody response body}.
     */
    @Nullable
    String getResponseBody() {
        return mResponseBody;
    }

}

