package com.ecertic.otpsecure;

import android.content.Context;

import com.ecertic.otpsecure.exception.APIConnectionException;
import com.ecertic.otpsecure.exception.APIException;
import com.ecertic.otpsecure.exception.AuthenticationException;
import com.ecertic.otpsecure.exception.InvalidRequestException;
import com.ecertic.otpsecure.exception.OtpSecureException;
import com.ecertic.otpsecure.exception.PermissionException;
import com.ecertic.otpsecure.model.OperationInfo;
import com.ecertic.otpsecure.model.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

/**
 * Handler for calls to the OtpSecure API.
 */
class OtpSecureApiHandler {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GET,
            POST
    })
    @interface RestMethod {
    }

    private static final String GET = "GET";
    private static final String POST = "POST";

    private static final String API_BASE = "https://api.otpsecure.net";
    private static final String CHARSET = "UTF-8";
    private static final String TOKEN = "token";
    private static final String VALIDATION = "validate";
    private static final String OTP = "otp";
    private static final String DNS_CACHE_TTL_PROPERTY_NAME = "networkaddress.cache.ttl";
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = new OtpSecureSSLSocketFactory();

    /**
     * Retrieve an existing {@link OperationInfo} object from the OtpSecure API.
     *
     * @param tokenId the {@link OperationInfo} field for the OperationInfo to query
     * @return a {@link OperationInfo} if one could be retrieved for the input params, or {@code null} if
     * no such OperationInfo could be found.
     * @throws AuthenticationException if there is a problem authenticating to the OtpSecure API
     * @throws InvalidRequestException if one or more of the parameters is incorrect
     * @throws APIConnectionException  if there is a problem connecting to the OtpSecure API
     * @throws APIException            for unknown OtpSecure API errors.
     */
    @Nullable
    static OperationInfo retrieveOperationInfoByToken(
            @NonNull Context context,
            @NonNull String tokenId,
            @Nullable LoggingResponseListener listener)
            throws AuthenticationException,
            InvalidRequestException,
            APIConnectionException,
            APIException {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(TOKEN, tokenId);

        //operation info is retrieved by token endpoint because API definition
        final OtpSecureResponse response =
                requestData(POST, getTokenApiUrl(), paramMap);
        return OperationInfo.fromString(response.getResponseBody());
    }

    /**
     * Validate an existing {@link OperationInfo} by sending the user provided otp to the OtpSecure API.
     *
     * @param tokenId the {@link OperationInfo} field for the OperationInfo to query
     * @return a {@link OperationInfo} if one could be retrieved for the input params, or {@code null} if
     * no such OperationInfo could be found.
     * @throws AuthenticationException if there is a problem authenticating to the OtpSecure API
     * @throws InvalidRequestException if one or more of the parameters is incorrect
     * @throws APIConnectionException  if there is a problem connecting to the OtpSecure API
     * @throws APIException            for unknown OtpSecure API errors.
     */
    @Nullable
    static Validation validateToken(
            @NonNull Context context,
            @NonNull String tokenId,
            @NonNull String inputOtp,
            @Nullable LoggingResponseListener listener)
            throws AuthenticationException,
            InvalidRequestException,
            APIConnectionException,
            APIException {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(TOKEN, tokenId);
        paramMap.put(OTP, inputOtp);

        final OtpSecureResponse response =
                requestData(POST, getValidationApiUrl(), paramMap);
        return Validation.fromString(response.getResponseBody());
    }

    @NonNull
    static String getTokenApiUrl() {
        return String.format(Locale.ENGLISH, "%s/%s", API_BASE, TOKEN);
    }

    @NonNull
    static String getValidationApiUrl() {
        return String.format(Locale.ENGLISH, "%s/%s", API_BASE, VALIDATION);
    }

    @NonNull
    private static java.net.HttpURLConnection createGetConnection(
            @NonNull String url,
            @NonNull String query) throws IOException {
        final HttpURLConnection conn = createOtpSecureConnection(formatURL(url, query));
        conn.setRequestMethod(GET);

        return conn;
    }

    @NonNull
    private static java.net.HttpURLConnection createPostConnection(
            @NonNull String url,
            @Nullable Map<String, Object> params) throws IOException, InvalidRequestException {
        final java.net.HttpURLConnection conn = createOtpSecureConnection(url);

        conn.setDoOutput(true);
        conn.setRequestMethod(POST);
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(getOutputBytes(params));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    @NonNull
    private static java.net.HttpURLConnection createOtpSecureConnection(
            @NonNull String url)
            throws IOException {
        final URL otpsecureURL = new URL(url);
        final HttpURLConnection conn = (HttpURLConnection) otpsecureURL.openConnection();
        conn.setConnectTimeout(30 * 1000);
        conn.setReadTimeout(80 * 1000);
        conn.setUseCaches(false);

        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
        }

        return conn;
    }

    @NonNull
    private static byte[] getOutputBytes(
            @Nullable Map<String, Object> params) throws InvalidRequestException {
        try {
            JSONObject jsonData = mapToJsonObject(params);
            if (jsonData == null) {
                throw new InvalidRequestException("Unable to create JSON data from parameters. ",
                        null, null, 0, null, null, null);
            }
            return jsonData.toString().getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to " + CHARSET + ".",
                    null, null, 0, null, null, e);
        }
    }

    @Nullable
    private static String getResponseBody(@NonNull InputStream responseStream)
            throws IOException {
        final Scanner scanner = new Scanner(responseStream, CHARSET).useDelimiter("\\A");
        final String rBody = scanner.hasNext() ? scanner.next() : null;
        responseStream.close();
        return rBody;
    }

    @NonNull
    private static String createQuery(@Nullable Map<String, Object> params)
            throws UnsupportedEncodingException, InvalidRequestException {
        final StringBuilder queryStringBuffer = new StringBuilder();
        final List<Parameter> flatParams = flattenParams(params);

        for (Parameter flatParam : flatParams) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(flatParam.key, flatParam.value));
        }

        return queryStringBuffer.toString();
    }

    @NonNull
    private static OtpSecureResponse getOtpSecureResponse(
            @RestMethod String method,
            @NonNull String url,
            @Nullable Map<String, Object> params)
            throws InvalidRequestException, APIConnectionException {
        // HTTPSURLConnection verifies SSL cert by default
        java.net.HttpURLConnection conn = null;
        try {
            switch (method) {
                case GET:
                    conn = createGetConnection(url, createQuery(params));
                    break;
                case POST:
                    conn = createPostConnection(url, params);
                    break;
                default:
                    throw new APIConnectionException(
                            String.format(Locale.ENGLISH,
                                    "Unrecognized HTTP method %s. "
                                            + "This indicates a bug in the OtpSecure bindings. ",
                                    method));
            }
            // trigger the request
            final int rCode = conn.getResponseCode();
            final String rBody;
            if (rCode >= 200 && rCode < 300) {
                rBody = getResponseBody(conn.getInputStream());
            } else {
                rBody = getResponseBody(conn.getErrorStream());
            }
            return new OtpSecureResponse(rCode, rBody/*, conn.getHeaderFields()*/);
        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(Locale.ENGLISH,
                            "IOException during API request to OtpSecure (%s): %s "
                                    + "Please check your internet connection and try again. "
                                    + "If this problem persists, let us know at dev@ecertic.com",
                            null, e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static void handleAPIError(@Nullable String responseBody, int responseCode,
                                       @Nullable String requestId)
            throws InvalidRequestException, AuthenticationException, APIException {

        final OtpSecureError otpsecureError = ErrorParser.parseError(responseBody);
        switch (responseCode) {
            case 400:
            case 404: {
                throw new InvalidRequestException(
                        otpsecureError.message,
                        otpsecureError.param,
                        requestId,
                        responseCode,
                        otpsecureError.code,
                        otpsecureError,
                        null);
            }
            case 401: {
                throw new AuthenticationException(otpsecureError.message, requestId, responseCode,
                        otpsecureError);
            }
            case 403: {
                throw new PermissionException(otpsecureError.message, requestId, responseCode,
                        otpsecureError);
            }
            default: {
                throw new APIException(otpsecureError.message, requestId, responseCode,
                        otpsecureError, null);
            }
        }
    }

    @NonNull
    private static OtpSecureResponse requestData(
            @RestMethod String method,
            @NonNull String url,
            @NonNull Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException {

        String originalDNSCacheTTL = null;
        boolean allowedToSetTTL = true;

        try {
            originalDNSCacheTTL = java.security.Security.getProperty(DNS_CACHE_TTL_PROPERTY_NAME);
            // disable DNS cache
            java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME, "0");
        } catch (SecurityException se) {
            allowedToSetTTL = false;
        }

        final OtpSecureResponse response = getOtpSecureResponse(method, url, params);

        final int rCode = response.getResponseCode();
        final String rBody = response.getResponseBody();

        final String requestId = java.util.UUID.randomUUID().toString();


        if (rCode < 200 || rCode >= 300) {
            handleAPIError(rBody, rCode, requestId);
        }

        if (allowedToSetTTL) {
            if (originalDNSCacheTTL == null) {
                // value unspecified by implementation
                // DNS_CACHE_TTL_PROPERTY_NAME of -1 = cache forever
                java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME, "-1");
            } else {
                java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME,
                        originalDNSCacheTTL);
            }
        }
        return response;
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
    private static JSONObject mapToJsonObject(@Nullable Map<String, ?> mapObject) {
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
                        //noinspection unchecked
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
     * Converts a {@link List} into a {@link JSONArray}. A {@link ClassCastException} will be
     * thrown if any object in the list (or any sub-list or sub-map) is a {@link Map} whose keys
     * are not {@link String Strings}.
     *
     * @param values a {@link List} of values to be put in a {@link JSONArray}
     * @return a {@link JSONArray}, or {@code null} if the input was {@code null}
     */
    @Nullable
    @SuppressWarnings("unchecked")
    private static JSONArray listToJsonArray(@Nullable List<?> values) {
        if (values == null) {
            return null;
        }

        final JSONArray jsonArray = new JSONArray();
        for (Object object : values) {
            if (object instanceof Map<?, ?>) {
                final Map<String, Object> mapObject = (Map<String, Object>) object;
                jsonArray.put(mapToJsonObject(mapObject));
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

    @NonNull
    private static String formatURL(@NonNull String url, @NonNull String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            String separator = url.contains("?") ? "&" : "?";
            return String.format(Locale.ROOT, "%s%s%s", url, separator, query);
        }
    }

    @NonNull
    private static List<Parameter> flattenParams(@Nullable Map<String, Object> params)
            throws InvalidRequestException {
        return flattenParamsMap(params, null);
    }

    @NonNull
    private static List<Parameter> flattenParamsList(@NonNull List<?> params,
                                                     @NonNull String keyPrefix)
            throws InvalidRequestException {
        final List<Parameter> flatParams = new LinkedList<>();

        if (params.isEmpty()) {
            flatParams.add(new Parameter(keyPrefix, ""));
        } else {
            final String newPrefix = String.format(Locale.ROOT, "%s[]", keyPrefix);
            for (Object param : params) {
                flatParams.addAll(flattenParamsValue(param, newPrefix));
            }
        }

        return flatParams;
    }

    @NonNull
    private static List<Parameter> flattenParamsMap(@Nullable Map<String, Object> params,
                                                    @Nullable String keyPrefix)
            throws InvalidRequestException {
        final List<Parameter> flatParams = new LinkedList<>();
        if (params == null) {
            return flatParams;
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            final String newPrefix;
            if (keyPrefix != null) {
                newPrefix = String.format(Locale.ROOT, "%s[%s]", keyPrefix, key);
            } else {
                newPrefix = key;
            }

            flatParams.addAll(flattenParamsValue(value, newPrefix));
        }

        return flatParams;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private static List<Parameter> flattenParamsValue(@NonNull Object value,
                                                      @Nullable String keyPrefix)
            throws InvalidRequestException {
        final List<Parameter> flatParams;
        if (value instanceof Map<?, ?>) {
            flatParams = flattenParamsMap((Map<String, Object>) value, keyPrefix);
        } else if (value instanceof List<?>) {
            flatParams = flattenParamsList((List<?>) value, keyPrefix);
        } else if ("".equals(value)) {
            throw new InvalidRequestException("You cannot set '" + keyPrefix + "' to an empty " +
                    "string. " + "We interpret empty strings as null in requests. " + "You may " +
                    "set '" + keyPrefix + "' to null to delete the property.", keyPrefix, null,
                    0, null, null, null);
        } else if (value == null) {
            flatParams = new LinkedList<>();
            flatParams.add(new Parameter(keyPrefix, ""));
        } else {
            flatParams = new LinkedList<>();
            flatParams.add(new Parameter(keyPrefix, value.toString()));
        }

        return flatParams;
    }

    @NonNull
    private static String urlEncodePair(@NonNull String k, @NonNull String v)
            throws UnsupportedEncodingException {
        return String.format(Locale.ROOT, "%s=%s", urlEncode(k), urlEncode(v));
    }

    @Nullable
    private static String urlEncode(@Nullable String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    private static final class Parameter {
        @NonNull
        private final String key;
        @NonNull
        private final String value;

        Parameter(@NonNull String key, @NonNull String value) {
            this.key = key;
            this.value = value;
        }
    }

    interface LoggingResponseListener {
        boolean shouldLogTest();

        void onLoggingResponse(OtpSecureResponse response);

        void onOtpSecureException(OtpSecureException exception);
    }

}
