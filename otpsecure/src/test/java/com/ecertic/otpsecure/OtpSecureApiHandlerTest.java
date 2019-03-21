package com.ecertic.otpsecure;

import com.ecertic.otpsecure.exception.AuthenticationException;
import com.ecertic.otpsecure.exception.OtpSecureException;
import com.ecertic.otpsecure.model.OperationInfo;
import com.ecertic.otpsecure.model.Validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for {@link OtpSecureApiHandler}.
 */
@RunWith(RobolectricTestRunner.class)
public class OtpSecureApiHandlerTest {

    private static final Integer NOT_FOUND = 404;
    private static final Integer BAD_REQUEST = 400;

    @Test
    public void testGetRequestTokenApiUrl() {
        String requestApi = OtpSecureApiHandler.getTokenApiUrl();
        assertEquals("https://api.otpsecure.net/token", requestApi);
    }

    @Test
    public void testGetRequestValidationApiUrl() {
        String requestApi = OtpSecureApiHandler.getValidationApiUrl();
        assertEquals("https://api.otpsecure.net/validate", requestApi);
    }

    @Test
    public void testRetrieveOperationInfoByToken_NotFound() {

        testRetrieveOperationInfoByToken(
                "123b1h4v2y3gv4dn13oi12312mx3oi12p3dbchs1231e12312312", NOT_FOUND);

    }

    @Test
    public void testRetrieveOperationInfoByToken_tokenNullEmpty_BadRequest() {

        //test null
        testRetrieveOperationInfoByToken(null, BAD_REQUEST);
        //test empty
        testRetrieveOperationInfoByToken("", BAD_REQUEST);
    }


    private void testRetrieveOperationInfoByToken(String tokenId, Integer expectedHttpStatusCode) {
        try {

            OperationInfo operationInfo = OtpSecureApiHandler.retrieveOperationInfoByToken(
                    ApplicationProvider.getApplicationContext(),
                    tokenId,
                    null);
            fail("Operation must not be found");
        } catch (OtpSecureException e) {
            assertEquals(e.getStatusCode(), expectedHttpStatusCode);
        }
    }


    @Test
    public void testValidateToken_NotFound() {

        testValidateToken("asdmaudfh82374282rf2t19h89e23e", "999099", NOT_FOUND);

    }


    @Test
    public void testValidateToken_tokenOtpNullEmpty_BadRequest() {


        //token = null && otp
        testValidateToken(null, "999099", BAD_REQUEST);


        //token = null && otp.isEmpty
        testValidateToken(null, "", BAD_REQUEST);


        //token = null && otp = null
        testValidateToken(null, null, BAD_REQUEST);


        //token.isEmpty && otp
        testValidateToken("", "999099", BAD_REQUEST);


        //token.isEmpty && otp.isEmpty
        testValidateToken("", "", BAD_REQUEST);


        //token.isEmpty && otp = null
        testValidateToken("", null, BAD_REQUEST);

        //token && otp = null
        testValidateToken("sdaj89da9msd8ajady87dsd78nhsfa8d7n88", null, BAD_REQUEST);

        //token && otp.isEmpty
        testValidateToken("sdaj89da9msd8ajady87dsd78nhsfa8d7n88", "", BAD_REQUEST);
    }


    private void testValidateToken(String tokenId, String otp, Integer expectedHttpStatusCode) {

        try {

            Validation validation = OtpSecureApiHandler.validateToken(
                    ApplicationProvider.getApplicationContext(),
                    tokenId,
                    otp,
                    null);
            fail("Operation must not be found");
        } catch (OtpSecureException e) {
            assertEquals(e.getStatusCode(), expectedHttpStatusCode);
        }
    }

}
