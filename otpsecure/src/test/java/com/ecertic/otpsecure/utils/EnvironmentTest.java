package com.ecertic.otpsecure.utils;

import android.Manifest;
import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertNotEquals;

/**
 * Test class for {@link Environment}.
 */
@RunWith(RobolectricTestRunner.class)
public class EnvironmentTest {

    private static final String PERMISSION_DENIED = null;

    private ShadowApplication application;

    @Before
    public void setup_EnvironmentTest() {
        application = Shadows.shadowOf((Application) ApplicationProvider.getApplicationContext());

    }

    @Test
    public void testEnvironmentBuilder() {
        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.RECORD_AUDIO};
        application.grantPermissions(permissions);
        Environment env = new Environment.Builder(ApplicationProvider.getApplicationContext()).build();
        assertNotEquals(env.getIp(), PERMISSION_DENIED);
        assertNotEquals(env.getMac(), PERMISSION_DENIED);
    }
}
