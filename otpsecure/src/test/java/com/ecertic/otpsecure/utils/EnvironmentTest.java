package com.ecertic.otpsecure.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowTelephonyManager;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link Environment}.
 */
//TODO: Implement ShadowFusedLocationClient to allow location testing.
@RunWith(RobolectricTestRunner.class)
public class EnvironmentTest {

    private ShadowApplication application;
    private ShadowTelephonyManager telephonyManager;

    @Before
    public void setup_EnvironmentTest() {
        application = Shadows.shadowOf((Application) ApplicationProvider.getApplicationContext());
        telephonyManager = Shadows.shadowOf(
                (TelephonyManager) ApplicationProvider.getApplicationContext()
                        .getSystemService(Context.TELEPHONY_SERVICE));

        //configure telephony manager
        telephonyManager.setDeviceId("fakeImeId");
        telephonyManager.setSimSerialNumber("fakeIccid");
        telephonyManager.setNetworkOperatorName("pacomeycenafone");

        //Mimic Secure.ANDROID_ID
        Settings.Secure.putString(ApplicationProvider.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID, "fake-android-id");

    }

    @Test
    public void testEnvironmentBuilder_withFullPermission() {

        //add permission
        String[] permissions = {Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
        application.grantPermissions(permissions);

        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNotEquals(env.getIp(), null);
        assertNotEquals(env.getMac(), null);
        assertNotEquals(env.getIccid(), null);
        assertNotEquals(env.getImei(), null);
        assertNotEquals(env.getCarrier(), null);
        assertNotEquals(env.getDevice(), null);
        assertNotEquals(env.getOs(), null);
        assertNotEquals(env.getDevUuid(), null);

        //TODO: update when ShadowFusedLocationClient is implemented.
        assertNull(env.getLocalization());
    }

    @Test
    public void testEnvironmentBuilder_withAnyPermission() {
        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNotEquals(env.getIp(), null);
        assertNotEquals(env.getMac(), null);
        assertNull(env.getIccid());
        assertNull(env.getImei());
        assertNotEquals(env.getCarrier(), null);
        assertNotEquals(env.getDevice(), null);
        assertNotEquals(env.getOs(), null);
        assertNotEquals(env.getDevUuid(), null);
        assertNull(env.getLocalization());
    }

    @Test
    public void testEnvironmentBuilder_withNoReadPhoneState() {
        String[] permissions = {Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION};
        application.grantPermissions(permissions);

        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNull(env.getIccid());
        assertNull(env.getImei());
    }

    @Test
    public void testEnvironmentBuilder_withNoInternetPermission() {
        String[] permissions = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION};
        application.grantPermissions(permissions);
        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNotEquals(env.getIp(), null);
        assertNotEquals(env.getMac(), null);
    }


    @Test
    public void testEnvironmentBuilder_withNoAccessFinePermission() {
        String[] permissions = {Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE};
        application.grantPermissions(permissions);
        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNull(env.getLocalization());
    }
}
