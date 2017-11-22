package library.chainmeans.com.scanner.support.ibeacon;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.adrecord.AdRecord;
import uk.co.alt236.bluetoothlelib.util.ByteUtils;

/**
 * Created by Deo-chainmeans on 2017/11/20.
 */

public abstract class IBeaconUtil {
    private static final byte[] MANUFACTURER_DATA_IBEACON_PREFIX = {0x4C, 0x00, 0x02, 0x15};
    private static final double DISTANCE_THRESHOLD_WTF = 0.0;
    private static final double DISTANCE_THRESHOLD_IMMEDIATE = 0.5;
    private static final double DISTANCE_THRESHOLD_NEAR = 3.0;

    /**
     * Calculates the accuracy of an RSSI reading.
     * <p/>
     * The code was taken from <a href="http://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing" /a>
     *
     * @param txPower the calibrated TX power of an iBeacon
     * @param rssi    the RSSI value of the iBeacon
     * @return the calculated Accuracy
     */
    public static double calculateAccuracy(final int txPower, final double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        final double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }


    public static IBeaconDistanceDescriptor getDistanceDescriptor(final double accuracy) {
        if (accuracy < DISTANCE_THRESHOLD_WTF) {
            return IBeaconDistanceDescriptor.UNKNOWN;
        }

        if (accuracy < DISTANCE_THRESHOLD_IMMEDIATE) {
            return IBeaconDistanceDescriptor.IMMEDIATE;
        }

        if (accuracy < DISTANCE_THRESHOLD_NEAR) {
            return IBeaconDistanceDescriptor.NEAR;
        }

        return IBeaconDistanceDescriptor.FAR;
    }

    public static String calculateUuidString(final byte[] uuid) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < uuid.length; i++) {
            if (i == 4) {
                sb.append('-');
            }
            if (i == 6) {
                sb.append('-');
            }
            if (i == 8) {
                sb.append('-');
            }
            if (i == 10) {
                sb.append('-');
            }

            final int intFromByte = ByteUtils.getIntFromByte(uuid[i]);
            if (intFromByte <= 0xF) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(intFromByte));
        }


        return sb.toString();
    }


    public static boolean isThisAnIBeacon(BluetoothLeDevice bluetoothLeDevice) {
        return isThisAnIBeacon(bluetoothLeDevice.getAdRecordStore().getRecordDataAsString(AdRecord.TYPE_MANUFACTURER_SPECIFIC_DATA).getBytes());
    }

    public static boolean isThisAnIBeacon(byte[] manufacturerData) {
        if (manufacturerData == null) {
            return false;
        }
        if (!(manufacturerData.length >= 25)) {
            return false;
        }

        if (ByteUtils.doesArrayBeginWith(manufacturerData, MANUFACTURER_DATA_IBEACON_PREFIX)) {
            return true;
        }
        return false;
    }

    public static double calculateDistance(int txPower, double rssi, float coefficient) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        return Math.pow(10d, ((double) txPower - rssi) / (10 * coefficient));
    }
}
