package com.ecertic.otpsecure.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Utility class for common text-related operations on OtpSecure data coming from the service.
 */
public class OtpSecureTextUtils {
    /**
     * Util Array for converting bytes to a hex string.
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Swap {@code null} for blank text values.
     *
     * @param value an input string that may or may not be entirely whitespace
     * @return {@code null} if the string is entirely whitespace, otherwise the input value
     */
    @Nullable
    public static String nullIfBlank(@Nullable String value) {
        if (isBlank(value)) {
            return null;
        }
        return value;
    }

    /**
     * A checker for whether or not the input value is entirely whitespace. This is slightly more
     * aggressive than the android TextUtils#isEmpty method, which only returns true for
     * {@code null} or {@code ""}.
     *
     * @param value a possibly blank input string value
     * @return {@code true} if and only if the value is all whitespace, {@code null}, or empty
     */

    public static boolean isBlank(@Nullable String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Check to see if the input number has any of the given prefixes.
     *
     * @param number   the number to test
     * @param prefixes the prefixes to test against
     * @return {@code true} if number begins with any of the input prefixes
     */
    static boolean hasAnyPrefix(@Nullable String number, @NonNull String... prefixes) {
        if (number == null) {
            return false;
        }

        for (String prefix : prefixes) {
            if (number.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate a hash value of a String input and convert the result to a hex string.
     *
     * @param toHash a value to hash
     * @return a hexadecimal string
     */
    @Nullable
    static String shaHashInput(@Nullable String toHash) {
        if (OtpSecureTextUtils.isBlank(toHash)) {
            return null;
        }

        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            final byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException noSuchAlgorithm) {
            return null;
        } catch (UnsupportedEncodingException unsupportedCoding) {
            return null;
        }
    }

    @NonNull
    private static String bytesToHex(@NonNull byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            final int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
