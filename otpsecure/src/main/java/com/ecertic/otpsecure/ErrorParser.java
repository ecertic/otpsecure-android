package com.ecertic.otpsecure;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ErrorParser {

    static final String MALFORMED_RESPONSE_MESSAGE =
            "An improperly formatted error response was found";

    private static final String FIELD_CODE = "code";
    private static final String FIELD_ERROR = "error";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_PARAM = "param";
    private static final String FIELD_TYPE = "type";

    @NonNull
    static OtpSecureError parseError(@Nullable String rawError) {
        String code = null;
        String message;
        String param = null;
        String type = null;
        try {
            JSONObject jsonError = new JSONObject(rawError);
            JSONObject errorObject = jsonError.getJSONObject(FIELD_ERROR);
            code = errorObject.optString(FIELD_CODE);
            message = errorObject.optString(FIELD_MESSAGE);
            param = errorObject.optString(FIELD_PARAM);
            type = errorObject.optString(FIELD_TYPE);
        } catch (JSONException jsonException) {
            message = MALFORMED_RESPONSE_MESSAGE;
        }
        return new OtpSecureError(type, message, code, param);
    }

}
