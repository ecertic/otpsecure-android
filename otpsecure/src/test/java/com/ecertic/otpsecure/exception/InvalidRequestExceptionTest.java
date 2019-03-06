package com.ecertic.otpsecure.exception;

import com.ecertic.otpsecure.OtpSecureErrorFixtures;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class InvalidRequestExceptionTest {

    @Test
    public void getOtpSecureError_shouldReturnOtpSecureError() {
        final OtpSecureException otpsecureException = new InvalidRequestException(null, null, null,
                null, null, OtpSecureErrorFixtures.INVALID_REQUEST_ERROR, null);
        assertEquals(OtpSecureErrorFixtures.INVALID_REQUEST_ERROR, otpsecureException.getOtpSecureError());
    }
}
