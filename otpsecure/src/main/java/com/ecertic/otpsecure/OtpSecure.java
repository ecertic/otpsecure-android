package com.ecertic.otpsecure;

import android.content.Context;
import android.os.AsyncTask;

import com.ecertic.otpsecure.exception.APIConnectionException;
import com.ecertic.otpsecure.exception.APIException;
import com.ecertic.otpsecure.exception.AuthenticationException;
import com.ecertic.otpsecure.exception.InvalidRequestException;
import com.ecertic.otpsecure.exception.OtpSecureException;
import com.ecertic.otpsecure.model.OperationInfo;
import com.ecertic.otpsecure.model.Validation;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class that handles OtpSecure methods.
 */
public class OtpSecure {

    @NonNull
    private final Context mContext;
    @Nullable
    private OtpSecureApiHandler.LoggingResponseListener mLoggingResponseListener;

    /**
     * A constructor with only context.
     *
     * @param context {@link Context} for resolving resources
     */
    public OtpSecure(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    private final OperationInfoRetriever mOperationInfoRetriever = new OperationInfoRetriever() {
        @Override
        public void retrieve(
                final String tokenId,
                final Executor executor,
                final OperationInfoCallback callback) {
            executeTask(executor, new RetrieveOperationInfoTask(mContext, tokenId,
                    callback, mLoggingResponseListener));
        }
    };

    private final TokenValidator mTokenValidator = new TokenValidator() {
        @Override
        public void validate(
                final String tokenId,
                final String inputOtp,
                final Executor executor,
                final ValidationCallback callback) {
            executeTask(executor, new ValidateTokenTask(mContext, tokenId, inputOtp,
                    callback, mLoggingResponseListener));
        }
    };

    void setLoggingResponseListener(OtpSecureApiHandler.LoggingResponseListener listener) {
        mLoggingResponseListener = listener;
    }

    /**
     * Retrieve an existing {@link OperationInfo} from the OtpSecure API. This
     * runs on the default {@link Executor}
     *
     * @param tokenId the {@link OperationInfo} field of the desired OperationInfo object
     * @return a {@link OperationInfo} if one could be found based on the input params, or {@code null} if
     * no such OperationInfo could be found.
     */
    public void retrieveOperationInfoByToken(
            @NonNull final String tokenId,
            @Nullable Executor executor,
            @NonNull OperationInfoCallback callback) {

        mOperationInfoRetriever.retrieve(
                tokenId,
                executor,
                callback);

    }

    /**
     * Retrieve an existing {@link OperationInfo} from the OtpSecure API. Note that this is a
     * synchronous method, and cannot be called on the main thread. Doing so will cause your app
     * to crash.
     *
     * @param tokenId the {@link OperationInfo} field of the desired OperationInfo object
     * @return a {@link OperationInfo} if one could be found based on the input params, or {@code null} if
     * no such OperationInfo could be found.
     * @throws AuthenticationException failure to properly authenticate yourself (check your key)
     * @throws InvalidRequestException your request has invalid parameters
     * @throws APIConnectionException  failure to connect to OtpSecure API
     * @throws APIException            any other type of problem (for instance, a temporary issue with
     *                                 the OtpSecure Service)
     */
    public OperationInfo retrieveOperationInfoByTokenSynchronous(
            @NonNull String tokenId)
            throws AuthenticationException,
            InvalidRequestException,
            APIConnectionException,
            APIException {
        return OtpSecureApiHandler.retrieveOperationInfoByToken(mContext, tokenId, mLoggingResponseListener);
    }

    /**
     * Validate an existing {@link OperationInfo} by sending the user provided otp to the OtpSecure API. Note that this is a
     * synchronous method, and cannot be called on the main thread. Doing so will cause your app
     * to crash.
     *
     * @param tokenId  the {@link OperationInfo} field of the desired OperationInfo object
     * @param inputOtp the user provided otp
     * @return a {@link OperationInfo} if one could be found based on the input params, or {@code null} if
     * no such OperationInfo could be found.
     */
    public void validateToken(
            @NonNull String tokenId,
            @NonNull String inputOtp,
            @Nullable Executor executor,
            @NonNull ValidationCallback callback) {

        mTokenValidator.validate(
                tokenId,
                inputOtp,
                executor,
                callback);

    }

    /**
     * Validate an existing {@link OperationInfo} by sending the user provided otp to the OtpSecure API. Note that this is a
     * synchronous method, and cannot be called on the main thread. Doing so will cause your app
     * to crash.
     *
     * @param tokenId  the {@link OperationInfo} field of the desired OperationInfo object
     * @param inputOtp the user provided otp
     * @return a {@link OperationInfo} if one could be found based on the input params, or {@code null} if
     * no such OperationInfo could be found.
     * @throws AuthenticationException failure to properly authenticate yourself (check your key)
     * @throws InvalidRequestException your request has invalid parameters
     * @throws APIConnectionException  failure to connect to OtpSecure API
     * @throws APIException            any other type of problem (for instance, a temporary issue with
     *                                 the OtpSecure Service)
     */
    public Validation validateTokenSynchronous(
            @NonNull String tokenId,
            @NonNull String inputOtp)
            throws AuthenticationException,
            InvalidRequestException,
            APIConnectionException,
            APIException {
        return OtpSecureApiHandler.validateToken(mContext, tokenId, inputOtp, mLoggingResponseListener);
    }

    interface OperationInfoRetriever {
        void retrieve(@NonNull String tokenId,
                      @Nullable Executor executor,
                      @NonNull OperationInfoCallback callback);
    }

    interface TokenValidator {
        void validate(@NonNull String tokenId,
                      @NonNull String inputOtp,
                      @Nullable Executor executor,
                      @NonNull ValidationCallback callback);
    }

    private void executeTask(@Nullable Executor executor,
                             @NonNull AsyncTask<Void, Void, ResponseWrapper> task) {
        if (executor != null) {
            task.executeOnExecutor(executor);
        } else {
            task.execute();
        }
    }

    private static class ResponseWrapper {
        @Nullable
        final Validation validation;
        @Nullable
        final OperationInfo operationInfo;
        @Nullable
        final Exception error;

        private ResponseWrapper(@Nullable OperationInfo operationInfo) {
            this.operationInfo = operationInfo;
            this.validation = null;
            this.error = null;
        }

        private ResponseWrapper(@Nullable Validation validation) {
            this.validation = validation;
            this.error = null;
            this.operationInfo = null;
        }

        private ResponseWrapper(@Nullable Exception error) {
            this.error = error;
            this.validation = null;
            this.operationInfo = null;
        }
    }

    private static class RetrieveOperationInfoTask extends AsyncTask<Void, Void, ResponseWrapper> {
        @NonNull
        private final WeakReference<Context> mContextRef;
        @NonNull
        private final String mTokenId;
        @NonNull
        private final WeakReference<OperationInfoCallback> mCallbackRef;
        @Nullable
        private final OtpSecureApiHandler.LoggingResponseListener mLoggingResponseListener;

        RetrieveOperationInfoTask(@NonNull Context context,
                                  @NonNull final String tokenId,
                                  @Nullable final OperationInfoCallback callback,
                                  @Nullable final OtpSecureApiHandler.LoggingResponseListener loggingResponseListener) {
            mContextRef = new WeakReference<>(context);
            mTokenId = tokenId;
            mLoggingResponseListener = loggingResponseListener;
            mCallbackRef = new WeakReference<>(callback);
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            try {
                final OperationInfo operationInfo = OtpSecureApiHandler.retrieveOperationInfoByToken(
                        mContextRef.get(),
                        mTokenId,
                        mLoggingResponseListener);
                return new ResponseWrapper(operationInfo);
            } catch (OtpSecureException e) {
                return new ResponseWrapper(e);
            }
        }

        @Override
        protected void onPostExecute(@NonNull ResponseWrapper result) {
            retrieveOperationInfoTaskPostExecution(result);
        }

        private void retrieveOperationInfoTaskPostExecution(@NonNull ResponseWrapper result) {
            final OperationInfoCallback callback = mCallbackRef.get();
            if (callback != null) {
                if (result.operationInfo != null) {
                    callback.onSuccess(result.operationInfo);
                } else if (result.error != null) {
                    callback.onError(result.error);
                } else {
                    callback.onError(new RuntimeException("Somehow got neither a operationInfo response or"
                            + " an error response"));
                }
            }
        }
    }

    private static class ValidateTokenTask extends AsyncTask<Void, Void, ResponseWrapper> {
        @NonNull
        private final WeakReference<Context> mContextRef;
        @NonNull
        private final String mTokenId;
        @NonNull
        private final String mInputOtp;
        @NonNull
        private final WeakReference<ValidationCallback> mCallbackRef;
        @Nullable
        private final OtpSecureApiHandler.LoggingResponseListener mLoggingResponseListener;

        ValidateTokenTask(@NonNull Context context,
                          @NonNull final String tokenId,
                          @NonNull final String inputOtp,
                          @Nullable final ValidationCallback callback,
                          @Nullable final OtpSecureApiHandler.LoggingResponseListener loggingResponseListener) {
            mContextRef = new WeakReference<>(context);
            mTokenId = tokenId;
            mInputOtp = inputOtp;
            mLoggingResponseListener = loggingResponseListener;
            mCallbackRef = new WeakReference<>(callback);
        }

        @Override
        protected ResponseWrapper doInBackground(Void... params) {
            try {
                final Validation validation = OtpSecureApiHandler.validateToken(
                        mContextRef.get(),
                        mTokenId,
                        mInputOtp,
                        mLoggingResponseListener);
                return new ResponseWrapper(validation);
            } catch (OtpSecureException e) {
                return new ResponseWrapper(e);
            }
        }

        @Override
        protected void onPostExecute(@NonNull ResponseWrapper result) {
            validateTokenTaskPostExecution(result);
        }

        private void validateTokenTaskPostExecution(@NonNull ResponseWrapper result) {
            final ValidationCallback callback = mCallbackRef.get();
            if (callback != null) {
                if (result.validation != null) {
                    callback.onSuccess(result.validation);
                } else if (result.error != null) {
                    callback.onError(result.error);
                } else {
                    callback.onError(new RuntimeException("Somehow got neither a validation response or"
                            + " an error response"));
                }
            }
        }
    }

}