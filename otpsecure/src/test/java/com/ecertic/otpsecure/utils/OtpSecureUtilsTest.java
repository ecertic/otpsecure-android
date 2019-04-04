package com.ecertic.otpsecure.utils;

import com.ecertic.otpsecure.utils.OtpSecureUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link OtpSecureUtils}
 */
@RunWith(RobolectricTestRunner.class)
public class OtpSecureUtilsTest {

    @Test
    public void removeNullAndEmptyParams_removesNullParams() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("a", null);
        testMap.put("b", "not null");
        OtpSecureUtils.removeNullAndEmptyParams(testMap);
        assertEquals(1, testMap.size());
        assertTrue(testMap.containsKey("b"));
    }

    @Test
    public void removeNullAndEmptyParams_removesEmptyStringParams() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("a", "fun param");
        testMap.put("b", "not null");
        testMap.put("c", "");
        OtpSecureUtils.removeNullAndEmptyParams(testMap);
        assertEquals(2, testMap.size());
        assertTrue(testMap.containsKey("a"));
        assertTrue(testMap.containsKey("b"));
    }

    @Test
    public void removeNullAndEmptyParams_removesNestedEmptyParams() {
        Map<String, Object> testMap = new HashMap<>();
        Map<String, Object> firstNestedMap = new HashMap<>();
        Map<String, Object> secondNestedMap = new HashMap<>();
        testMap.put("a", "fun param");
        testMap.put("b", "not null");
        firstNestedMap.put("1a", "something");
        firstNestedMap.put("1b", null);
        secondNestedMap.put("2a", "");
        secondNestedMap.put("2b", "hello world");
        firstNestedMap.put("1c", secondNestedMap);
        testMap.put("c", firstNestedMap);

        OtpSecureUtils.removeNullAndEmptyParams(testMap);
        assertEquals(3, testMap.size());
        assertTrue(testMap.containsKey("a"));
        assertTrue(testMap.containsKey("b"));
        assertTrue(testMap.containsKey("c"));
        assertEquals(2, firstNestedMap.size());
        assertTrue(firstNestedMap.containsKey("1a"));
        assertTrue(firstNestedMap.containsKey("1c"));
        assertEquals(1, secondNestedMap.size());
        assertTrue(secondNestedMap.containsKey("2b"));
    }

}
