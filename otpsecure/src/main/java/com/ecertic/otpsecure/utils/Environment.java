package com.ecertic.otpsecure.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

import androidx.annotation.Nullable;

/**
 * Class to handle environment data that contains the audit trail.
 *
 * @author jgarcia@ecertic.com
 */
public class Environment {

    private String ip;
    private String mac;
    private String iccid;
    private String imei;
    private String carrier;
    private String device;
    private String ooss;
    private String localization;

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public String getIccid() {
        return iccid;
    }

    public String getImei() {
        return imei;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getDevice() {
        return device;
    }

    public String getOoss() {
        return ooss;
    }

    public String getLocalization() {
        return localization;
    }

    //immutable
    private Environment(String ip, String mac, String iccid, String imei,
                        String carrier, String device, String ooss, String localization) {
        this.ip = ip;
        this.mac = mac;
        this.iccid = iccid;
        this.imei = imei;
        this.carrier = carrier;
        this.device = device;
        this.ooss = ooss;
        this.localization = localization;
    }

    public static class Builder {

        @Nullable
        //nullable by api contract (json)
        private static final String PERMISSION_DENIED = null;

        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }


        public Environment build() {

            return new Environment(getIp(), getMAC(),
                    "iccdid", "imei", "carrier", "device", "android26", "localization");
        }


        private final boolean checkPermission(String permission) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                return context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                return PermissionChecker.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
            }

        }

        private final String getIp() {
            if (checkPermission(android.Manifest.permission.INTERNET)) {
                return "127.0.0.1";
            }
            return PERMISSION_DENIED;
        }

        private final String getMAC() {
            if (checkPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                return "00-14-22-01-23-45";
            }
            return PERMISSION_DENIED;
        }


    }


}
