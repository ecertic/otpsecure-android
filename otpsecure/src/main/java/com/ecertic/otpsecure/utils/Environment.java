package com.ecertic.otpsecure.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ecertic.otpsecure.BuildConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Class to handle environment data that contains the audit trail.
 *
 * @author jgarcia@ecertic.com
 */
public class Environment {

    private static final String TAG = Environment.class.getSimpleName();

    //immutable
    private Environment(String ip, String mac, String iccid, String imei,
                        String carrier, String device, String os, String androidId,
                        String localization) {
        this.ip = ip;
        this.mac = mac;
        this.iccid = iccid;
        this.imei = imei;
        this.carrier = carrier;
        this.device = device;
        this.os = os;
        this.devUuid = androidId;
        this.localization = localization;
    }

    private String ip;
    private String mac;
    private String iccid;
    private String imei;
    private String carrier;
    private String device;
    private String os;
    private String devUuid;
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

    public String getOs() {
        return os;
    }

    public String getDevUuid() {
        return devUuid;
    }

    public String getLocalization() {
        return localization;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Environment{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", mac='").append(mac).append('\'');
        sb.append(", iccid='").append(iccid).append('\'');
        sb.append(", imei='").append(imei).append('\'');
        sb.append(", carrier='").append(carrier).append('\'');
        sb.append(", device='").append(device).append('\'');
        sb.append(", os='").append(os).append('\'');
        sb.append(", devUuid='").append(devUuid).append('\'');
        sb.append(", localization='").append(localization).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static final class Builder {

        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public final Environment build() {
            //set androidId as devUuid
            Environment env = new Environment(getIp(), getMAC(), getSimSerialNumber(), getImei(),
                    getCarrier(), getDeviceModel(), getOs(), getAndroidId(), getLocation());
            if (BuildConfig.DEBUG) {
                Log.d(TAG, env.toString());
            }

            return env;
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

        private static final String getIp() {
            Inet4Address inet4Address = getCurrentInet4Address();
            return (inet4Address != null) ? inet4Address.getHostAddress() : null;

        }

        //only returns MAC if network technology is IEEE 802 standard
        private static final String getMAC() {
            Inet4Address inet4Address = getCurrentInet4Address();
            try {
                NetworkInterface network = NetworkInterface.getByInetAddress(inet4Address);
                byte[] mac = network.getHardwareAddress();
                //mac is null for non IEEE 802 standard network interfaces
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            } catch (SocketException e) {
                Log.e(TAG, "error getting MAC", e);
            }
            return null;
        }


        private static final Inet4Address getCurrentInet4Address() {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();

                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                         enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && inetAddress instanceof Inet4Address) {
                            return (Inet4Address) inetAddress;
                        }
                    }
                }

            } catch (SocketException e) {
                Log.e(TAG, "error getting inetAddress", e);
            }
            return null;
        }

        private final String getSimSerialNumber() {
            if (checkPermission(Manifest.permission.READ_PHONE_STATE)) {
                return getTelephonyManager().getSimSerialNumber();
            }
            return null;

        }

        private final String getImei() {
            if (checkPermission(Manifest.permission.READ_PHONE_STATE)) {
                return getTelephonyManager().getDeviceId();
            }
            return null;

        }

        private final String getCarrier() {
            return getTelephonyManager().getNetworkOperatorName();
        }

        private final TelephonyManager getTelephonyManager() {
            return (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
        }

        private static final String getDeviceModel() {
            return Build.DEVICE;
        }

        private static final String getOs() {
            return "Android " + Build.VERSION.RELEASE;
        }

        private final String getAndroidId() {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        private final String getLocation() {
            final ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<String> result = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        FusedLocationProviderClient fusedLocationClient = LocationServices
                                .getFusedLocationProviderClient(context);
                        Task<Location> lastLocation = fusedLocationClient.getLastLocation();
                        try {
                            Tasks.await(lastLocation, 500, TimeUnit.MILLISECONDS);
                            if (lastLocation.isSuccessful()) {
                                Location loc = lastLocation.getResult();
                                // Got last known location. In some rare situations this can be null
                                if (loc != null) {
                                    return new StringBuilder()
                                            .append(loc.getLatitude())
                                            .append(",")
                                            .append(loc.getLongitude())
                                            .toString();
                                }
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            Log.e(TAG, "failing retrieving task location", e);
                        }

                    }
                    return null;
                }
            });

            try {
                return result.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}

