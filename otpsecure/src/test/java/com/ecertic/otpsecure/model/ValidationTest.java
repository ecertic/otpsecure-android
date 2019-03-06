package com.ecertic.otpsecure.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link Validation}.
 */
@RunWith(RobolectricTestRunner.class)
public class ValidationTest {

    private static final String RAW_VALIDATION = "{\n" +
            "  \"msg\": \"PIN INCORRECTO!!!!\",\n" +
            "  \"status\": \"OTP_NOK\"\n" +
            "}";

    private static final String RAW_VALIDATION_NO_MSG = "{\n" +
            "  \"status\": \"OTP_NOK\"\n" +
            "}";

    private static final String RAW_VALIDATION_NO_STATUS = "{\n" +
            "  \"msg\": \"PIN INCORRECTO!!!!\"\n" +
            "}";

    @Test
    public void parseValidation_readsObject() {
        Validation partialExpectedValidation = new Validation(
                "OTP_NOK",
                "PIN INCORRECTO!!!!");
        Validation answerValidation = Validation.fromString(RAW_VALIDATION);
        assertNotNull(answerValidation);
        assertEquals(partialExpectedValidation.getMsg(), answerValidation.getMsg());
        assertEquals(partialExpectedValidation.getStatus(), answerValidation.getStatus());
    }

    @Test
    public void parseValidation_whenNullString_returnsNull() {
        Validation parsedValidation = Validation.fromString(null);
        assertNull(parsedValidation);
    }

    @Test
    public void parseValidation_withoutMsg_returnsNull() {
        Validation token = Validation.fromString(RAW_VALIDATION_NO_MSG);
        assertNull(token);
    }

    @Test
    public void parseValidation_withoutStatus_returnsNull() {
        Validation token = Validation.fromString(RAW_VALIDATION_NO_STATUS);
        assertNull(token);
    }

}
