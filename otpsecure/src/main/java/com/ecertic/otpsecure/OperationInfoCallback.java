package com.ecertic.otpsecure;

import com.ecertic.otpsecure.model.OperationInfo;

/**
 * An interface representing a callback to be notified about the results of a
 * {@link OperationInfo} retrieves
 */
public interface OperationInfoCallback {

    /**
     * Error callback method.
     *
     * @param error the error that occurred.
     */
    void onError(Exception error);

    /**
     * Success callback method.
     *
     * @param operationInfo the {@link OperationInfo} that was retrieved.
     */
    void onSuccess(OperationInfo operationInfo);
}
