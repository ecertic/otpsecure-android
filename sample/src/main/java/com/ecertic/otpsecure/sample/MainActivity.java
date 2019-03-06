package com.ecertic.otpsecure.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecertic.otpsecure.sample.module.DependencyHandler;

public class MainActivity extends AppCompatActivity {

    private static final String OPERATION_TOKEN = "<INSERT_OPERATION_TOKEN_HERE>";

    private DependencyHandler mDependencyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final TextView htmlTextView = findViewById(R.id.html_textView);
        final EditText otpInputText = findViewById(R.id.otpinput_editText);
        final Button retrieveButton = findViewById(R.id.retrieve_button);
        final Button validateButton = findViewById(R.id.validate_button);

        mDependencyHandler = new DependencyHandler(
                this,
                htmlTextView,
                otpInputText,
                retrieveButton,
                validateButton,
                OPERATION_TOKEN);

        mDependencyHandler.clearReferences();

        // Asynchronous retrieve OperationInfo with AsyncTask
        mDependencyHandler.attachAsyncTaskOperationInfoController(retrieveButton);

        // Asynchronous validates Token with AsyncTask
        mDependencyHandler.attachAsyncTaskValidationController(validateButton);

        /** Synchronous samples.
        // Synchronous retrieve OperationInfo with Service

        mDependencyHandler.attachIntentServiceOperationInfoController(this,
                retrieveButton);

        // Synchronous validates token with Service

        mDependencyHandler.attachIntentServiceValidationController(this,
                validateButton);
         */
    }

}
