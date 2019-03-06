package com.ecertic.otpsecure.sample.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ecertic.otpsecure.OtpSecure;
import com.ecertic.otpsecure.exception.OtpSecureException;
import com.ecertic.otpsecure.model.OperationInfo;

/**
 * An {@link IntentService} subclass for retrieve a {@link OperationInfo} from a tokenId.
 */
public class OperationInfoIntentService extends IntentService {

    public static final String TOKEN_ACTION = "com.ecertic.otpsecure.sample.service.tokenAction";
    public static final String OTPSECURE_UUID = "com.ecertic.otpsecure.sample.service.uuid";
    public static final String OTPSECURE_HTML = "com.ecertic.otpsecure.sample.service.html";
    public static final String OTPSECURE_ERROR_MESSAGE = "com.ecertic.otpsecure.sample.service.errorMessage";

    private static final String EXTRA_TOKEN_ID = "com.otpsecure.sample.service.extra.tokenId";

    public static Intent createOperationInfoIntent(
            Activity launchingActivity,
            String tokenId) {
        return new Intent(launchingActivity, OperationInfoIntentService.class)
                .putExtra(EXTRA_TOKEN_ID, tokenId);
    }

    public OperationInfoIntentService() {
        super("OperationInfoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = null;
        OperationInfo operationInfo = null;

        if (intent != null) {
            final String tokenId = intent.getStringExtra(EXTRA_TOKEN_ID);

            final OtpSecure otpsecure = new OtpSecure(getApplicationContext());
            try {
                operationInfo = otpsecure.retrieveOperationInfoByTokenSynchronous(tokenId);
            } catch (OtpSecureException otpsecureEx) {
                errorMessage = otpsecureEx.getLocalizedMessage();
            }
        }

        final Intent localIntent = new Intent(TOKEN_ACTION);
        if (operationInfo != null) {
            localIntent.putExtra(OTPSECURE_UUID, operationInfo.getUuid());
            localIntent.putExtra(OTPSECURE_HTML, operationInfo.getHtml());
        }

        if (errorMessage != null) {
            localIntent.putExtra(OTPSECURE_ERROR_MESSAGE, errorMessage);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }
}
