package com.ecertic.otpsecure.model;

import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Represents a JSON model used in the OtpSecure Api.
 */
abstract class OtpSecureJsonModel {

    @NonNull
    protected abstract Map<String, Object> toMap();

    @NonNull
    protected abstract JSONObject toJson();

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OtpSecureJsonModel)) {
            return false;
        }

        OtpSecureJsonModel otherModel = (OtpSecureJsonModel) obj;
        return this.toString().equals(otherModel.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
