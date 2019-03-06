package com.ecertic.otpsecure.sample.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ecertic.otpsecure.OtpSecure;
import com.ecertic.otpsecure.ValidationCallback;
import com.ecertic.otpsecure.model.Validation;

import java.util.concurrent.Executor;

/**
 * Logic needed to validate tokens using the {@link android.os.AsyncTask} methods included in the
 * sdk: {@link OtpSecure#validateToken(String, String, Executor, ValidationCallback)}.
 */
public class AsyncTaskValidationController {

    private final Context mContext;
    private final DialogHandler mDialogHandler;
    private final OtpValidateViewController mOtpValidateViewController;
    private final ProgressDialogController mProgressDialogController;
    private final String mTheToken;

    public AsyncTaskValidationController(
            Button button,
            Context context,
            DialogHandler dialogHandler,
            OtpValidateViewController otpValidateViewController,
            ProgressDialogController progressDialogController,
            String theToken) {
        mContext = context;
        mDialogHandler = dialogHandler;
        mProgressDialogController = progressDialogController;
        mOtpValidateViewController = otpValidateViewController;
        mTheToken = theToken;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateToken();
            }
        });
    }

    private void validateToken() {
        if (mTheToken == null) {
            mDialogHandler.showMsg("Invalid OperationInfo");
            return;
        }
        mProgressDialogController.startProgress();
        new OtpSecure(mContext).validateToken(
                mTheToken,
                mOtpValidateViewController.getInputOtp(),
                null,
                new ValidationCallback() {
                    public void onSuccess(Validation validation) {
                        mDialogHandler.showMsg(validation.getMsg());
                        mProgressDialogController.finishProgress();
                    }

                    public void onError(Exception error) {
                        mDialogHandler.showMsg(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });
    }

}
