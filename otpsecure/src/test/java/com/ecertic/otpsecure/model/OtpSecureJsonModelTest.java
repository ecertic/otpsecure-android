package com.ecertic.otpsecure.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link OtpSecureJsonModel}.
 */
@RunWith(RobolectricTestRunner.class)
public class OtpSecureJsonModelTest {

    @Test
    public void equals_whenEquals_returnsTrue() {
        assertTrue(OtpSecureJsonModel.class.isAssignableFrom(OperationInfo.class));

        OperationInfo firstOperationInfo = OperationInfo.fromString(OperationInfoTest.RAW_TOKEN);
        OperationInfo secondOperationInfo = OperationInfo.fromString(OperationInfoTest.RAW_TOKEN);

        assertEquals(firstOperationInfo, secondOperationInfo);
        // Just confirming for sanity
        assertNotSame(firstOperationInfo, secondOperationInfo);
    }

    @Test
    public void hashCode_whenEquals_returnsSameValue() {
        assertTrue(OtpSecureJsonModel.class.isAssignableFrom(OperationInfo.class));

        OperationInfo firstOperationInfo = OperationInfo.fromString(OperationInfoTest.RAW_TOKEN);
        OperationInfo secondOperationInfo = OperationInfo.fromString(OperationInfoTest.RAW_TOKEN);
        assertNotNull(firstOperationInfo);
        assertNotNull(secondOperationInfo);

        assertEquals(firstOperationInfo.hashCode(), secondOperationInfo.hashCode());
    }

}
