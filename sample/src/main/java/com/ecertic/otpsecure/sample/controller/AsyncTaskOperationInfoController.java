package com.ecertic.otpsecure.sample.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.ecertic.otpsecure.OtpSecure;
import com.ecertic.otpsecure.OperationInfoCallback;
import com.ecertic.otpsecure.model.OperationInfo;

import java.util.concurrent.Executor;

/**
 * Logic needed to retrieve tokens using the {@link android.os.AsyncTask} methods included in the
 * sdk: {@link OtpSecure#retrieveOperationInfoByToken(String, Executor, OperationInfoCallback)}.
 */
public class AsyncTaskOperationInfoController {

    private final Context mContext;
    private final DialogHandler mDialogHandler;
    private final OtpValidateViewController mOtpValidateViewController;
    private final ProgressDialogController mProgressDialogController;
    private final String mTheToken;

    public AsyncTaskOperationInfoController(
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
                retrieveOperationInfoByToken();
            }
        });
    }

    private void retrieveOperationInfoByToken() {
        if (mTheToken == null) {
            mDialogHandler.showMsg("Invalid OperationInfo");
            return;
        }
        mProgressDialogController.startProgress();
        new OtpSecure(mContext).retrieveOperationInfoByToken(
                mTheToken,
                null,
                new OperationInfoCallback() {
                    public void onSuccess(OperationInfo operationInfo) {
                        mOtpValidateViewController.setView(operationInfo.getHtml());
                        mProgressDialogController.finishProgress();
                    }

                    public void onError(Exception error) {
                        mDialogHandler.showMsg(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });
    }

}
