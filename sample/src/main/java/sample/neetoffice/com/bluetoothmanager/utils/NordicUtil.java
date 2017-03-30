package sample.neetoffice.com.bluetoothmanager.utils;

import android.util.Log;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2016/12/5.
 */

public class NordicUtil {
    public static boolean isThisANordicBLE(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return isThisANordicBLE(scanRecord);
    }

    public static boolean isThisANordicBLE(byte[] manufacturerData) {
        if (manufacturerData == null) {
            return false;
        }
        // An iBeacon record must be at least 25 chars long
        if (!(manufacturerData.length >= 25)) {
            return false;
        }
        return manufacturerData[4] == (byte) 0xFF && manufacturerData[5] == (byte) 0x59;
    }

    public static boolean isClicking(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return isClicking(scanRecord);
    }

    public static boolean isClicking(byte[] manufacturerData) {
        return isThisANordicBLE(manufacturerData) && manufacturerData[17] == (byte) 0x01;
    }
}
