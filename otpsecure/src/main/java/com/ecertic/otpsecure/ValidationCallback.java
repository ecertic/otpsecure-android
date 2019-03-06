package com.ecertic.otpsecure;

import com.ecertic.otpsecure.model.Validation;

/**
 * An interface representing a callback to be notified about the results of a operationInfo
 * {@link Validation}
 */
public interface ValidationCallback {

    /**
     * Error callback method.
     *
     * @param error the error that occurred.
     */
    void onError(Exception error);

    /**
     * Success callback method.
     *
     * @param validation the {@link Validation} that was requested.
     */
    void onSuccess(Validation validation);
}
