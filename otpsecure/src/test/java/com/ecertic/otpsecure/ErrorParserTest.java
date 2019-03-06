package com.ecertic.otpsecure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link ErrorParser}.
 */
@RunWith(RobolectricTestRunner.class)

public class ErrorParserTest {

    private static final String RAW_INVALID_REQUEST_ERROR = "" +
            "{\n" +
            "  \"error\": {\n" +
            "    \"message\" : \"The OtpSecure API is only accessible over HTTPS.\",\n" +
            "    \"type\": \"invalid_request_error\"\n" +
            "  }\n" +
            "}";

    private static final String RAW_INCORRECT_FORMAT_ERROR = "" +
            "{\n" +
            "    \"message\" : \"The OtpSecure API is only accessible over HTTPS.\",\n" +
            "    \"type\": \"invalid_request_error\"\n" +
            "}";

    private static final String RAW_ERROR_WITH_ALL_FIELDS = "" +
            "{\n" +
            "  \"error\": {\n" +
            "    \"code\": \"code_value\",\n" +
            "    \"param\": \"param_value\",\n" +
            "    \"message\": \"Required param.\",\n" +
            "    \"type\": \"invalid_request_error\"\n" +
            "  }\n" +
            "}";

    @Test
    public void parseError_withInvalidRequestError_createsCorrectObject() {
        final OtpSecureError parsedOtpSecureError =
                ErrorParser.parseError(RAW_INVALID_REQUEST_ERROR);
        String errorMessage = "The OtpSecure API is only accessible over HTTPS.";
        assertEquals(errorMessage, parsedOtpSecureError.message);
        assertEquals("invalid_request_error", parsedOtpSecureError.type);
        assertEquals("", parsedOtpSecureError.param);
    }

    @Test
    public void parseError_withNoErrorMessage_addsInvalidResponseMessage() {
        final OtpSecureError badOtpSecureError =
                ErrorParser.parseError(RAW_INCORRECT_FORMAT_ERROR);
        assertEquals(ErrorParser.MALFORMED_RESPONSE_MESSAGE, badOtpSecureError.message);
        assertNull(badOtpSecureError.type);
    }

    @Test
    public void parseError_withAllFields_parsesAllFields() {
        final OtpSecureError error = ErrorParser.parseError(RAW_ERROR_WITH_ALL_FIELDS);
        assertEquals("code_value", error.code);
        assertEquals("param_value", error.param);
        assertEquals("invalid_request_error", error.type);
    }

}
