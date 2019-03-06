package com.ecertic.otpsecure.sample.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecertic.otpsecure.sample.controller.AsyncTaskOperationInfoController;
import com.ecertic.otpsecure.sample.controller.AsyncTaskValidationController;
import com.ecertic.otpsecure.sample.controller.DialogHandler;
import com.ecertic.otpsecure.sample.controller.IntentServiceOperationInfoController;
import com.ecertic.otpsecure.sample.controller.IntentServiceValidationController;
import com.ecertic.otpsecure.sample.controller.OtpValidateViewController;
import com.ecertic.otpsecure.sample.controller.ProgressDialogController;

public class DependencyHandler {

    private AsyncTaskOperationInfoController mAsyncTaskOperationInfoController;
    private AsyncTaskValidationController mAsyncTaskValidationController;
    private final Context mContext;
    private final ProgressDialogController mProgresDialogController;
    private final DialogHandler mDialogHandler;
    private IntentServiceOperationInfoController mIntentServiceOperationInfoController;
    private IntentServiceValidationController mIntentServiceValidationController;
    private final OtpValidateViewController mOtpValidateViewController;
    private final String mTheToken;

    public DependencyHandler(
            AppCompatActivity activity,
            TextView htmltextView,
            EditText otpeditText,
            Button retrieveButton,
            Button validateButton,
            String theToken) {

        mTheToken = theToken;

        mContext = activity.getApplicationContext();

        mProgresDialogController =
                new ProgressDialogController(activity.getSupportFragmentManager());

        mOtpValidateViewController = new OtpValidateViewController(htmltextView, otpeditText, retrieveButton, validateButton);

        mDialogHandler = new DialogHandler(activity.getSupportFragmentManager());
    }

    /**
     * Attach a listener that retrieves a token using the {@link android.os.AsyncTask}-based method.
     * Only gets attached once, unless you call {@link #clearReferences()}.
     *
     * @param button a button that, when clicked, gets a token.
     * @return a reference to the {@link AsyncTaskOperationInfoController}
     */
    public void attachAsyncTaskOperationInfoController(Button button) {
        if (mAsyncTaskOperationInfoController == null) {
            mAsyncTaskOperationInfoController = new AsyncTaskOperationInfoController(
                    button,
                    mContext,
                    mDialogHandler,
                    mOtpValidateViewController,
                    mProgresDialogController,
                    mTheToken);
        }
    }

    /**
     * Attach a listener that validates a token using the {@link android.os.AsyncTask}-based method.
     * Only gets attached once, unless you call {@link #clearReferences()}.
     *
     * @param button a button that, when clicked, validates a token with the user input.
     * @return a reference to the {@link AsyncTaskOperationInfoController}
     */
    public void attachAsyncTaskValidationController(Button button) {
        if (mAsyncTaskValidationController == null) {
            mAsyncTaskValidationController = new AsyncTaskValidationController(
                    button,
                    mContext,
                    mDialogHandler,
                    mOtpValidateViewController,
                    mProgresDialogController,
                    mTheToken);
        }
    }


    public IntentServiceOperationInfoController attachIntentServiceOperationInfoController(
            AppCompatActivity appCompatActivity,
            Button button) {
        if (mIntentServiceOperationInfoController == null) {
            mIntentServiceOperationInfoController = new IntentServiceOperationInfoController(
                    appCompatActivity,
                    button,
                    mDialogHandler,
                    mOtpValidateViewController,
                    mProgresDialogController,
                    mTheToken);
        }
        return mIntentServiceOperationInfoController;
    }

    public IntentServiceValidationController attachIntentServiceValidationController(
            AppCompatActivity appCompatActivity,
            Button button) {
        if (mIntentServiceValidationController == null) {
            mIntentServiceValidationController = new IntentServiceValidationController(
                    appCompatActivity,
                    button,
                    mDialogHandler,
                    mOtpValidateViewController,
                    mProgresDialogController,
                    mTheToken);
        }
        return mIntentServiceValidationController;
    }

    /**
     * Clear all the references so that we can start over again.
     */
    public void clearReferences() {

        if (mIntentServiceOperationInfoController != null) {
            mIntentServiceOperationInfoController.detach();
        }

        mAsyncTaskOperationInfoController = null;
        mAsyncTaskValidationController = null;
        mIntentServiceOperationInfoController = null;
    }
}
