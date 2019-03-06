package com.ecertic.otpsecure.sample.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ecertic.otpsecure.OtpSecure;
import com.ecertic.otpsecure.exception.OtpSecureException;
import com.ecertic.otpsecure.model.Validation;

/**
 * An {@link IntentService} subclass to request a {@link Validation} of a OperationInfo with the user provided otp.
 */
public class ValidationIntentService extends IntentService {

    public static final String VALIDATION_ACTION = "com.ecertic.otpsecure.sample.service.validationAction";
    public static final String OTPSECURE_STATUS = "com.ecertic.otpsecure.sample.service.status";
    public static final String OTPSECURE_RESULT = "com.ecertic.otpsecure.sample.service.result";
    public static final String OTPSECURE_ERROR_MESSAGE = "com.ecertic.otpsecure.sample.service.errorMessage";

    private static final String EXTRA_TOKEN_ID = "com.ecertic.otpsecure.sample.service.extra.tokenId";
    private static final String EXTRA_INPUT_OTP = "com.ecertic.otpsecure.sample.service.extra.inputOtp";

    public static Intent requestValidationIntent(
            Activity launchingActivity,
            String tokenId,
            String inputOtp) {
        return new Intent(launchingActivity, ValidationIntentService.class)
                .putExtra(EXTRA_TOKEN_ID, tokenId)
                .putExtra(EXTRA_INPUT_OTP, inputOtp);
    }

    public ValidationIntentService() {
        super("ValidationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = null;
        Validation validation = null;

        if (intent != null) {
            final String tokenId = intent.getStringExtra(EXTRA_TOKEN_ID);
            final String inputOtp = intent.getStringExtra(EXTRA_INPUT_OTP);

            final OtpSecure otpsecure = new OtpSecure(getApplicationContext());
            try {
                validation = otpsecure.validateTokenSynchronous(tokenId, inputOtp);
            } catch (OtpSecureException otpsecureEx) {
                errorMessage = otpsecureEx.getLocalizedMessage();
            }
        }

        final Intent localIntent = new Intent(VALIDATION_ACTION);
        if (validation != null) {
            localIntent.putExtra(OTPSECURE_STATUS, validation.getStatus());
            localIntent.putExtra(OTPSECURE_RESULT, validation.getMsg());
        }

        if (errorMessage != null) {
            localIntent.putExtra(OTPSECURE_ERROR_MESSAGE, errorMessage);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

}
