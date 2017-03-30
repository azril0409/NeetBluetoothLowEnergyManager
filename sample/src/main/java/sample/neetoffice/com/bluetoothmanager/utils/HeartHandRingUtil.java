package sample.neetoffice.com.bluetoothmanager.utils;

import java.nio.ByteBuffer;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2016/12/8.
 */

public class HeartHandRingUtil {
    public static boolean isThisAHeartHandRing(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return isThisAHeartHandRing(scanRecord);
    }

    public static boolean isThisAHeartHandRing(byte[] manufacturerData) {
        if (manufacturerData == null) {
            return false;
        }
        // An iBeacon record must be at least 25 chars long
        if (!(manufacturerData.length >= 25)) {
            return false;
        }
        return manufacturerData[7] == (byte) 0xFF && manufacturerData[8] == (byte) 0xF1;
    }

    public static short getHeartRateValue(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return getHeartRateValue(scanRecord);
    }

    public static short getHeartRateValue(byte[] manufacturerData) {
        return ByteBuffer.wrap(manufacturerData, 14, 16).getShort();
    }

    public static int getBattery(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return getBattery(scanRecord);
    }

    public static int getBattery(byte[] manufacturerData) {
        return manufacturerData[16];
    }

    public static int getTxPowerlevel(BluetoothLeDevice device) {
        final byte[] scanRecord = device.getScanRecord();
        return getTxPowerlevel(scanRecord);
    }

    public static int getTxPowerlevel(byte[] manufacturerData) {
        return manufacturerData[17];
    }
}
