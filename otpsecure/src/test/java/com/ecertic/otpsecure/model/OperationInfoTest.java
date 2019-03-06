package com.ecertic.otpsecure.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link OperationInfo}.
 */
@RunWith(RobolectricTestRunner.class)
public class OperationInfoTest {

    static final String RAW_TOKEN = "{\n" +
            "  \"uuid\": \"BytCT6bSN\",\n" +
            "  \"html\": \"Al introducir el código recibido por SMS, que sustituye a mi firma manuscrita, declaro mi consentimiento y aceptación\"\n" +
            "}";

    private static final String RAW_TOKEN_NO_UUID = "{\n" +
            "  \"html\": \"Al introducir el código recibido por SMS, que sustituye a mi firma manuscrita, declaro mi consentimiento y aceptación\"\n" +
            "}";

    private static final String RAW_TOKEN_NO_HTML = "{\n" +
            "  \"uuid\": \"BytCT6bSN\"\n" +
            "}";

    @Test
    public void parseToken_readsObject() {
        OperationInfo partialExpectedOperationInfo = new OperationInfo(
                "BytCT6bSN",
                "Al introducir el código recibido por SMS, que sustituye a mi firma manuscrita, declaro mi consentimiento y aceptación");
        OperationInfo answerOperationInfo = OperationInfo.fromString(RAW_TOKEN);
        assertNotNull(answerOperationInfo);
        assertEquals(partialExpectedOperationInfo.getUuid(), answerOperationInfo.getUuid());
        assertEquals(partialExpectedOperationInfo.getHtml(), answerOperationInfo.getHtml());
    }

    @Test
    public void parseToken_whenNullString_returnsNull() {
        OperationInfo parsedOperationInfo = OperationInfo.fromString(null);
        assertNull(parsedOperationInfo);
    }

    @Test
    public void parseToken_withoutUuid_returnsNull() {
        OperationInfo operationInfo = OperationInfo.fromString(RAW_TOKEN_NO_UUID);
        assertNull(operationInfo);
    }

    @Test
    public void parseToken_withoutHtml_returnsNull() {
        OperationInfo operationInfo = OperationInfo.fromString(RAW_TOKEN_NO_HTML);
        assertNull(operationInfo);
    }

}
