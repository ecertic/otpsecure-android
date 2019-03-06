package com.ecertic.otpsecure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.ecertic.otpsecure.OtpSecureUtils.removeNullAndEmptyParams;
import static com.ecertic.otpsecure.model.OtpSecureJsonUtils.optString;
import static com.ecertic.otpsecure.model.OtpSecureJsonUtils.putStringIfNotNull;

public class Validation extends OtpSecureJsonModel {

    private static final String FIELD_STATUS = "status";
    private static final String FIELD_MSG = "msg";

    @NonNull
    private final String mStatus;
    @NonNull
    private final String mMsg;

    Validation(
            String status,
            String msg
    ) {
        mStatus = status;
        mMsg = msg;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(FIELD_STATUS, mStatus);
        hashMap.put(FIELD_MSG, mMsg);

        removeNullAndEmptyParams(hashMap);
        return hashMap;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        putStringIfNotNull(jsonObject, FIELD_STATUS, mStatus);
        putStringIfNotNull(jsonObject, FIELD_MSG, mMsg);

        return jsonObject;
    }

    @Nullable
    public static Validation fromString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            return fromJson(new JSONObject(jsonString));
        } catch (JSONException ignored) {
            return null;
        }
    }

    @Nullable
    private static Validation fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String status = optString(jsonObject, FIELD_STATUS);
        String msg = optString(jsonObject, FIELD_MSG);

        if (status == null || msg == null) {
            return null;
        }

        return new Validation(
                status,
                msg);
    }

    @NonNull
    public String getStatus() {
        return mStatus;
    }

    @NonNull
    public String getMsg() {
        return mMsg;
    }
}

