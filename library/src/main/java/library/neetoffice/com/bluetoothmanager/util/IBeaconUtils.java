package library.neetoffice.com.bluetoothmanager.util;


import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.adrecord.AdRecord;

public class IBeaconUtils {
    public static final float IDEAL_SPACE = 2;
    private static final double DISTANCE_THRESHOLD_WTF = 0.0;
    private static final double DISTANCE_THRESHOLD_IMMEDIATE = 0.5;
    private static final double DISTANCE_THRESHOLD_NEAR = 3.0;

    private static final byte[] MANUFACTURER_DATA_IBEACON_PREFIX = new byte[]{0x4C, 0x00, 0x02, 0x15};

    public static IBeaconDistanceDescriptor getDistanceDescriptor(double distance) {
        if (distance < DISTANCE_THRESHOLD_WTF) {
            return IBeaconDistanceDescriptor.UNKNOWN;
        }

        if (distance < DISTANCE_THRESHOLD_IMMEDIATE) {
            return IBeaconDistanceDescriptor.IMMEDIATE;
        }

        if (distance < DISTANCE_THRESHOLD_NEAR) {
            return IBeaconDistanceDescriptor.NEAR;
        }

        return IBeaconDistanceDescriptor.FAR;
    }

    /**
     * Calculates the accuracy of an RSSI reading.
     * <p/>
     * The code was taken from {@link http://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing}
     *
     * @param txPower the calibrated TX power of an iBeacon
     * @param rssi    the RSSI value of the iBeacon
     * @return
     */
    public static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            final double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    /**
     * Calculates the distance of an RSSI reading.
     * <p/>
     * The code was taken from {@link http://qiita.com/shu223/items/7c4e87c47eca65724305}
     *
     * @param txPower the calibrated TX power of an iBeacon
     * @param rssi    the RSSI value of the iBeacon
     * @return
     */
    public static double calculateDistance(int txPower, double rssi, float coefficient) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        return Math.pow(10d, ((double) txPower - rssi) / (10 * coefficient));
    }

    public static void main(String[] args){
        System.out.println(""+calculateDistance(0,-61,4));
    }


    /**
     * Ascertains whether a {@link BluetoothLeDevice} is an iBeacon;
     *
     * @param device a {@link BluetoothLeDevice} device.
     * @return
     */
    public static boolean isThisAnIBeacon(BluetoothLeDevice device) {
        return isThisAnIBeacon(device.getAdRecordStore().getRecordDataAsString(AdRecord.TYPE_MANUFACTURER_SPECIFIC_DATA).getBytes());
    }

    /**
     * Ascertains whether a Manufacturer Data byte array belongs to an iBeacon;
     *
     * @param manufacturerData a Bluetooth LE device's manufacturerData.
     * @return
     */
    public static boolean isThisAnIBeacon(byte[] manufacturerData) {
        if (manufacturerData == null) {
            return false;
        }

        // An iBeacon record must be at least 25 chars long
        if (!(manufacturerData.length >= 25)) {
            return false;
        }

        if (ByteUtils.doesArrayBeginWith(manufacturerData, MANUFACTURER_DATA_IBEACON_PREFIX)) {
            return true;
        }

        return false;
    }

    public enum IBeaconDistanceDescriptor {
        IMMEDIATE,
        NEAR,
        FAR,
        UNKNOWN,
    }
}
