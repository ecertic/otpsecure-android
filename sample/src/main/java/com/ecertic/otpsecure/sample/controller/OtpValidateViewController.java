package com.ecertic.otpsecure.sample.controller;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OtpValidateViewController {

    private final TextView mHtmlTextView;
    private final EditText mOtpEditText;
    private final Button mRetrieveButton;
    private final Button mValidateButton;

    public OtpValidateViewController(TextView htmltextView, EditText otpeditText, Button retrieveButton, Button validateButton) {
        mHtmlTextView = htmltextView;
        mOtpEditText = otpeditText;
        mRetrieveButton = retrieveButton;
        mValidateButton = validateButton;
    }

    public void setView(String html) {
        mHtmlTextView.setText(html);
        mOtpEditText.setVisibility(View.VISIBLE);
        mRetrieveButton.setVisibility(View.GONE);
        mValidateButton.setVisibility(View.VISIBLE);
    }

    public String getInputOtp() {
        return mOtpEditText.getText().toString();
    }

}
