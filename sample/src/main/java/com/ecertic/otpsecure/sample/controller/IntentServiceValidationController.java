package com.ecertic.otpsecure.sample.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ecertic.otpsecure.sample.service.ValidationIntentService;

/**
 * Example class showing how to validate an otp with its associated token with an {@link android.app.IntentService} doing your
 * background I/O work.
 */
public class IntentServiceValidationController {

    private final DialogHandler mDialogHandler;
    private final OtpValidateViewController mOtpValidateViewController;
    private final ProgressDialogController mProgressDialogController;
    private final String mTheToken;

    private Activity mActivity;
    private TokenBroadcastReceiver mTokenBroadcastReceiver;

    public IntentServiceValidationController(
            AppCompatActivity appCompatActivity,
            Button button,
            DialogHandler dialogHandler,
            OtpValidateViewController outputOtpValidateController,
            ProgressDialogController progressDialogController,
            String theToken) {

        mTheToken = theToken;
        mActivity = appCompatActivity;
        mDialogHandler = dialogHandler;
        mOtpValidateViewController = outputOtpValidateController;
        mProgressDialogController = progressDialogController;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputOtp();
            }
        });
        registerBroadcastReceiver();
    }

    /**
     * Unregister the {@link BroadcastReceiver}.
     */
    public void detach() {
        if (mTokenBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity)
                    .unregisterReceiver(mTokenBroadcastReceiver);
            mTokenBroadcastReceiver = null;
            mActivity = null;
        }
    }

    private void registerBroadcastReceiver() {
        mTokenBroadcastReceiver = new TokenBroadcastReceiver();
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(
                mTokenBroadcastReceiver,
                new IntentFilter(ValidationIntentService.VALIDATION_ACTION));
    }

    private void validateInputOtp() {
        final Intent tokenServiceIntent = ValidationIntentService.requestValidationIntent(
                mActivity, mTheToken, mOtpValidateViewController.getInputOtp());
        mProgressDialogController.startProgress();
        mActivity.startService(tokenServiceIntent);
    }

    private class TokenBroadcastReceiver extends BroadcastReceiver {

        // Prevent instantiation of a local broadcast receiver outside this class.
        private TokenBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressDialogController.finishProgress();

            if (intent == null) {
                return;
            }

            if (intent.hasExtra(ValidationIntentService.OTPSECURE_ERROR_MESSAGE)) {
                mDialogHandler.showMsg(
                        intent.getStringExtra(ValidationIntentService.OTPSECURE_ERROR_MESSAGE));
                return;
            }

            if (intent.hasExtra(ValidationIntentService.OTPSECURE_STATUS)) {
                mDialogHandler.showMsg(
                        intent.getStringExtra(ValidationIntentService.OTPSECURE_RESULT));
                return;
            }

        }
    }

}
