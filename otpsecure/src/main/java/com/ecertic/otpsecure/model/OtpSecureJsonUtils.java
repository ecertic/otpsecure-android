package com.ecertic.otpsecure.model;

import com.ecertic.otpsecure.OtpSecureTextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

class OtpSecureJsonUtils {

    private static final String EMPTY = "";
    private static final String NULL = "null";

    /**
     * Calls through to {@link JSONObject#getString(String)} while safely
     * converting the raw string "null" and the empty string to {@code null}.
     *
     * @param jsonObject the input object
     * @param fieldName  the required field name
     * @return the value stored in the requested field
     * @throws JSONException if the field does not exist
     */
    @Nullable
    static String getString(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) throws JSONException {
        return nullIfNullOrEmpty(jsonObject.getString(fieldName));
    }

    /**
     * Calls through to {@link JSONObject#optString(String)} while safely
     * converting the raw string "null" and the empty string to {@code null}. Will not throw
     * an exception if the field isn't found.
     *
     * @param jsonObject the input object
     * @param fieldName  the optional field name
     * @return the value stored in the field, or {@code null} if the field isn't present
     */
    @Nullable
    static String optString(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName) {
        return nullIfNullOrEmpty(jsonObject.optString(fieldName));
    }

    /**
     * Convert a {@link JSONObject} to a {@link Map}.
     *
     * @param jsonObject a {@link JSONObject} to be converted
     * @return a {@link Map} representing the input, or {@code null} if the input is {@code null}
     */
    @Nullable
    static Map<String, Object> jsonObjectToMap(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = jsonObject.opt(key);
            if (NULL.equals(value) || value == null) {
                continue;
            }

            if (value instanceof JSONObject) {
                map.put(key, jsonObjectToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(key, jsonArrayToList((JSONArray) value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * Convert a {@link JSONObject} to a flat, string-keyed and string-valued map. All values
     * are recorded as strings.
     *
     * @param jsonObject the input {@link JSONObject} to be converted
     * @return a {@link Map} representing the input, or {@code null} if the input is {@code null}
     */
    @Nullable
    static Map<String, String> jsonObjectToStringMap(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = jsonObject.opt(key);
            if (NULL.equals(value) || value == null) {
                continue;
            }

            map.put(key, value.toString());
        }

        return map;
    }

    /**
     * Converts a {@link JSONArray} to a {@link List}.
     *
     * @param jsonArray a {@link JSONArray} to be converted
     * @return a {@link List} representing the input, or {@code null} if said input is {@code null}
     */
    @Nullable
    static List<Object> jsonArrayToList(@Nullable JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object ob = jsonArray.get(i);
                if (ob instanceof JSONArray) {
                    objectList.add(jsonArrayToList((JSONArray) ob));
                } else if (ob instanceof JSONObject) {
                    Map<String, Object> objectMap = jsonObjectToMap((JSONObject) ob);
                    if (objectMap != null) {
                        objectList.add(objectMap);
                    }
                } else {
                    if (NULL.equals(ob)) {
                        continue;
                    }
                    objectList.add(ob);
                }
            } catch (JSONException ignored) {
                // Nothing to do in this case.
            }
        }
        return objectList;
    }

    /**
     * Converts a string-keyed {@link Map} into a {@link JSONObject}. This will cause a
     * {@link ClassCastException} if any sub-map has keys that are not {@link String Strings}.
     *
     * @param mapObject the {@link Map} that you'd like in JSON form
     * @return a {@link JSONObject} representing the input map, or {@code null} if the input
     * object is {@code null}
     */
    @Nullable
    @SuppressWarnings("unchecked")
    static JSONObject mapToJsonObject(@Nullable Map<String, ? extends Object> mapObject) {
        if (mapObject == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (String key : mapObject.keySet()) {
            Object value = mapObject.get(key);
            if (value == null) {
                continue;
            }

            try {
                if (value instanceof Map<?, ?>) {
                    try {
                        Map<String, Object> mapValue = (Map<String, Object>) value;
                        jsonObject.put(key, mapToJsonObject(mapValue));
                    } catch (ClassCastException classCastException) {
                        // We don't include the item in the JSONObject if the keys are not Strings.
                    }
                } else if (value instanceof List<?>) {
                    jsonObject.put(key, listToJsonArray((List<Object>) value));
                } else if (value instanceof Number || value instanceof Boolean) {
                    jsonObject.put(key, value);
                } else {
                    jsonObject.put(key, value.toString());
                }
            } catch (JSONException jsonException) {
                // Simply skip this value
            }
        }
        return jsonObject;
    }

    /**
     * Converts a String-String {@link Map} into a {@link JSONObject}.
     *
     * @param stringStringMap the input map
     * @return a {@link JSONObject} with the same key-value pairings
     */
    @Nullable
    static JSONObject stringHashToJsonObject(@Nullable Map<String, String> stringStringMap) {
        if (stringStringMap == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        for (String key : stringStringMap.keySet()) {
            try {
                jsonObject.put(key, stringStringMap.get(key));
            } catch (JSONException jsonException) {
                // simply skip this value
            }
        }
        return jsonObject;
    }

    /**
     * Converts a {@link List} into a {@link JSONArray}. A {@link ClassCastException} will be
     * thrown if any object in the list (or any sub-list or sub-map) is a {@link Map} whose keys
     * are not {@link String Strings}.
     *
     * @param values a {@link List} of values to be put in a {@link JSONArray}
     * @return a {@link JSONArray}, or {@code null} if the input was {@code null}
     */
    @Nullable
    @SuppressWarnings("unchecked")
    static JSONArray listToJsonArray(@Nullable List values) {
        if (values == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();
        for (Object object : values) {
            if (object instanceof Map<?, ?>) {
                try {
                    Map<String, Object> mapObject = (Map<String, Object>) object;
                    jsonArray.put(mapToJsonObject(mapObject));
                } catch (ClassCastException classCastException) {
                    // We don't include the item in the array if the keys are not Strings.
                }
            } else if (object instanceof List<?>) {
                jsonArray.put(listToJsonArray((List) object));
            } else if (object instanceof Number || object instanceof Boolean) {
                jsonArray.put(object);
            } else {
                jsonArray.put(object.toString());
            }
        }
        return jsonArray;
    }

    /**
     * Util function for putting a string value into a {@link JSONObject} if that
     * string is not null or empty. This ignores any {@link JSONException} that may be thrown
     * due to insertion.
     *
     * @param jsonObject the {@link JSONObject} into which to put the field
     * @param fieldName  the field name
     * @param value      the potential field value
     */
    static void putStringIfNotNull(
            @NonNull JSONObject jsonObject,
            @NonNull @Size(min = 1) String fieldName,
            @Nullable String value) {
        if (!OtpSecureTextUtils.isBlank(value)) {
            try {
                jsonObject.put(fieldName, value);
            } catch (JSONException ignored) {
            }
        }
    }

    @Nullable
    static String nullIfNullOrEmpty(@Nullable String possibleNull) {
        return NULL.equals(possibleNull) || EMPTY.equals(possibleNull)
                ? null
                : possibleNull;
    }
}
