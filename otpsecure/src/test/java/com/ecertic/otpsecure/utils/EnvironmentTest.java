package com.ecertic.otpsecure.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowTelephonyManager;
import org.robolectric.shadows.gms.common.ShadowGoogleApiAvailability;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for {@link Environment}.
 */
//TODO: Implement ShadowFusedLocationClient to allow location testing.
@RunWith(RobolectricTestRunner.class)
public class EnvironmentTest {

    private ShadowApplication application;
    private ShadowTelephonyManager telephonyManager;
    private ShadowGoogleApiAvailability googleApiAvailability;
    private FusedLocationProviderClient fusedLocationClient;

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

        //TODO: update when ShadowFusedLocationClient is implemented.
        assertEquals(env.getLocalization(), null);
    }

    @Test
    public void testEnvironmentBuilder_withAnyPermission() {
        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNotEquals(env.getIp(), null);
        assertNotEquals(env.getMac(), null);
        assertEquals(env.getIccid(), null);
        assertEquals(env.getImei(), null);
        assertNotEquals(env.getCarrier(), null);
        assertNotEquals(env.getDevice(), null);
        assertNotEquals(env.getOs(), null);
        assertEquals(env.getLocalization(), null);
    }

    @Test
    public void testEnvironmentBuilder_withNoReadPhoneState() {
        String[] permissions = {Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION};
        application.grantPermissions(permissions);

        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertEquals(env.getIccid(), null);
        assertEquals(env.getImei(), null);
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
        assertEquals(env.getLocalization(), null);
    }
}
