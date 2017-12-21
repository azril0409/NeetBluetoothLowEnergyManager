package library.neetoffice.com.bluetoothmanager.util;

/**
 * Created by Deo-chainmeans on 2017/11/22.
 */

public class DistanceUtil {
    public static final float IDEAL_SPACE = 2;

    /**
     * Calculates the distance of an RSSI reading.
     * <p/>
     * The code was taken from  http://qiita.com/shu223/items/7c4e87c47eca65724305
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
}
