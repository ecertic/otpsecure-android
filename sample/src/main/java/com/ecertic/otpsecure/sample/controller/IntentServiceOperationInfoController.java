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

import com.ecertic.otpsecure.sample.service.OperationInfoIntentService;


/**
 * Example class showing how to retrieve an otp with its associated token with an {@link android.app.IntentService} doing your
 * background I/O work.
 */
public class IntentServiceOperationInfoController {

    private final DialogHandler mDialogHandler;
    private final OtpValidateViewController mOtpValidateViewController;
    private final ProgressDialogController mProgressDialogController;
    private final String mTheToken;

    private Activity mActivity;
    private OperationInfoBroadcastReceiver mOperationInfoBroadcastReceiver;

    public IntentServiceOperationInfoController(
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
                retrieveOtp();
            }
        });
        registerBroadcastReceiver();
    }

    /**
     * Unregister the {@link BroadcastReceiver}.
     */
    public void detach() {
        if (mOperationInfoBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mActivity)
                    .unregisterReceiver(mOperationInfoBroadcastReceiver);
            mOperationInfoBroadcastReceiver = null;
            mActivity = null;
        }
    }

    private void registerBroadcastReceiver() {
        mOperationInfoBroadcastReceiver = new OperationInfoBroadcastReceiver();
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(
                mOperationInfoBroadcastReceiver,
                new IntentFilter(OperationInfoIntentService.TOKEN_ACTION));
    }

    private void retrieveOtp() {
        final Intent tokenServiceIntent = OperationInfoIntentService.createOperationInfoIntent(
                mActivity, mTheToken);
        mProgressDialogController.startProgress();
        mActivity.startService(tokenServiceIntent);
    }

    private class OperationInfoBroadcastReceiver extends BroadcastReceiver {

        // Prevent instantiation of a local broadcast receiver outside this class.
        private OperationInfoBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mProgressDialogController.finishProgress();

            if (intent == null) {
                return;
            }

            if (intent.hasExtra(OperationInfoIntentService.OTPSECURE_ERROR_MESSAGE)) {
                mDialogHandler.showMsg(
                        intent.getStringExtra(OperationInfoIntentService.OTPSECURE_ERROR_MESSAGE));
                return;
            }

            if (intent.hasExtra(OperationInfoIntentService.OTPSECURE_UUID) &&
                    intent.hasExtra(OperationInfoIntentService.OTPSECURE_HTML)) {
                mOtpValidateViewController.setView(
                        intent.getStringExtra(OperationInfoIntentService.OTPSECURE_HTML)
                );
                return;
            }
        }
    }
}
