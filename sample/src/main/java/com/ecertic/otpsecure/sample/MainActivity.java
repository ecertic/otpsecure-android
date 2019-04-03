package com.ecertic.otpsecure.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecertic.otpsecure.sample.module.DependencyHandler;

public class MainActivity extends AppCompatActivity {

    private static final String OPERATION_TOKEN = "f699b33ca5d99b37ccd9d872fabd95d05554584550a9afc890c6c88b5201fe4d";

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

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //  gps functionality
        }
    }

}
