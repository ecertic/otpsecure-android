package com.ecertic.otpsecure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for {@link OtpSecureTextUtils}.
 */
@RunWith(RobolectricTestRunner.class)
public class OtpSecureTextUtilsTest {

    @Test
    public void hasAnyPrefixShouldFailIfNull() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix(null));
    }

    @Test
    public void hasAnyPrefixShouldFailIfEmpty() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix(""));
    }

    @Test
    public void hasAnyPrefixShouldFailWithNullAndEmptyPrefix() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix(null, ""));
    }

    @Test
    public void hasAnyPrefixShouldFailWithNullAndSomePrefix() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix(null, "1"));
    }

    @Test
    public void hasAnyPrefixShouldMatch() {
        assertTrue(OtpSecureTextUtils.hasAnyPrefix("1234", "12"));
    }

    @Test
    public void hasAnyPrefixShouldMatchMultiple() {
        assertTrue(OtpSecureTextUtils.hasAnyPrefix("1234", "12", "1"));
    }

    @Test
    public void hasAnyPrefixShouldMatchSome() {
        assertTrue(OtpSecureTextUtils.hasAnyPrefix("abcd", "bc", "ab"));
    }

    @Test
    public void hasAnyPrefixShouldNotMatch() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix("1234", "23"));
    }

    @Test
    public void hasAnyPrefixShouldNotMatchWithSpace() {
        assertFalse(OtpSecureTextUtils.hasAnyPrefix("xyz", " x"));
    }

    @Test
    public void testNullIfBlankNullShouldBeNull() {
        assertNull(OtpSecureTextUtils.nullIfBlank(null));
    }

    @Test
    public void testNullIfBlankEmptyShouldBeNull() {
        assertNull(OtpSecureTextUtils.nullIfBlank(""));
    }

    @Test
    public void testNullIfBlankSpaceShouldBeNull() {
        assertNull(OtpSecureTextUtils.nullIfBlank(" "));
    }

    @Test
    public void testNullIfBlankSpacesShouldBeNull() {
        assertNull(OtpSecureTextUtils.nullIfBlank("     "));
    }

    @Test
    public void testNullIfBlankTabShouldBeNull() {
        assertNull(OtpSecureTextUtils.nullIfBlank("	"));
    }

    @Test
    public void testNullIfBlankNumbersShouldNotBeNull() {
        assertEquals("0", OtpSecureTextUtils.nullIfBlank("0"));
    }

    @Test
    public void testNullIfBlankLettersShouldNotBeNull() {
        assertEquals("abc", OtpSecureTextUtils.nullIfBlank("abc"));
    }

    @Test
    public void isBlankShouldPassIfNull() {
        assertTrue(OtpSecureTextUtils.isBlank(null));
    }

    @Test
    public void isBlankShouldPassIfEmpty() {
        assertTrue(OtpSecureTextUtils.isBlank(""));
    }

    @Test
    public void isBlankShouldPassIfSpace() {
        assertTrue(OtpSecureTextUtils.isBlank(" "));
    }

    @Test
    public void isBlankShouldPassIfSpaces() {
        assertTrue(OtpSecureTextUtils.isBlank("     "));
    }

    @Test
    public void isBlankShouldPassIfTab() {
        assertTrue(OtpSecureTextUtils.isBlank("	"));
    }

    @Test
    public void isBlankShouldFailIfNumber() {
        assertFalse(OtpSecureTextUtils.isBlank("0"));
    }

    @Test
    public void isBlankShouldFailIfLetters() {
        assertFalse(OtpSecureTextUtils.isBlank("abc"));
    }

    @Test
    public void shaHashInput_withNullInput_returnsNull() {
        assertNull(OtpSecureTextUtils.shaHashInput("  "));
    }

    @Test
    public void shaHashInput_withText_returnsDifferentText() {
        String unhashedText = "iamtheveryverymodelofamodernmajorgeneral";
        String hashedText = OtpSecureTextUtils.shaHashInput(unhashedText);
        assertNotEquals(unhashedText, hashedText);
    }

}
