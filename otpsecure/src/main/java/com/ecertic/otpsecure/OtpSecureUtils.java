package com.ecertic.otpsecure;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Map;

import androidx.annotation.NonNull;

public class OtpSecureUtils {

    /**
     * Remove null values from a map. This helps with JSON conversion and validation.
     *
     * @param mapToEdit a {@link Map} from which to remove the keys that have {@code null} values
     */
    @SuppressWarnings("unchecked")
    public static void removeNullAndEmptyParams(@NonNull Map<String, Object> mapToEdit) {
        // Remove all null values; they cause validation errors
        for (String key : new HashSet<>(mapToEdit.keySet())) {
            if (mapToEdit.get(key) == null) {
                mapToEdit.remove(key);
            }

            if (mapToEdit.get(key) instanceof CharSequence) {
                CharSequence sequence = (CharSequence) mapToEdit.get(key);
                if (TextUtils.isEmpty(sequence)) {
                    mapToEdit.remove(key);
                }
            }

            if (mapToEdit.get(key) instanceof Map) {
                Map<String, Object> stringObjectMap = (Map<String, Object>) mapToEdit.get(key);
                removeNullAndEmptyParams(stringObjectMap);
            }
        }
    }

}
