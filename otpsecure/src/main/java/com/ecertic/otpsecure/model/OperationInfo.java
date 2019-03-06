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

/**
 * This class represents an OperationInfo model, which can be retrieve by token
 */

public class OperationInfo extends OtpSecureJsonModel {

    private static final String FIELD_UUID = "uuid";
    private static final String FIELD_HTML = "html";

    @NonNull
    private final String mUuid;
    @NonNull
    private final String mHtml;

    OperationInfo(
            String uuid,
            String html
    ) {
        mUuid = uuid;
        mHtml = html;
    }

    @NonNull
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(FIELD_UUID, mUuid);
        hashMap.put(FIELD_HTML, mHtml);

        removeNullAndEmptyParams(hashMap);
        return hashMap;
    }

    @NonNull
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        putStringIfNotNull(jsonObject, FIELD_UUID, mUuid);
        putStringIfNotNull(jsonObject, FIELD_HTML, mHtml);

        return jsonObject;
    }


    @Nullable
    public static OperationInfo fromString(String jsonString) {
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
    static OperationInfo fromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String uuid = optString(jsonObject, FIELD_UUID);
        String html = optString(jsonObject, FIELD_HTML);

        if (uuid == null || html == null) {
            return null;
        }

        return new OperationInfo(
                uuid,
                html);
    }

    @NonNull
    public String getUuid() {
        return mUuid;
    }

    @NonNull
    public String getHtml() {
        return mHtml;
    }


}
