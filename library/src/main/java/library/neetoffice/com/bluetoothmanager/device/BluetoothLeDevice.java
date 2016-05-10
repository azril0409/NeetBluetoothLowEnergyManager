package library.neetoffice.com.bluetoothmanager.device;

import android.bluetooth.BluetoothDevice;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import library.neetoffice.com.bluetoothmanager.device.adrecord.AdRecordStore;
import library.neetoffice.com.bluetoothmanager.resolvers.BluetoothClassResolver;

/**
 * Created by Mac on 2016/05/08.
 */
public interface BluetoothLeDevice extends Parcelable {

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    int describeContents();

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    boolean equals(Object obj);

    /**
     * Gets the address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Gets the bluetooth device bond state.
     *
     * @return the bluetooth device bond state
     */
    String getBluetoothDeviceBondState();

    /**
     * Gets the bluetooth device class name.
     *
     * @return the bluetooth device class name
     */

    String getBluetoothDeviceClassName();

    /**
     * Gets the device.
     *
     * @return the device
     */
    BluetoothDevice getDevice();

    /**
     * Gets the first rssi.
     *
     * @return the first rssi
     */
    int getFirstRssi();

    /**
     * Gets the first timestamp.
     *
     * @return the first timestamp
     */
    long getFirstTimestamp();

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the rssi.
     *
     * @return the rssi
     */
    int getRssi();

    /**
     * Gets the rssi log.
     *
     * @return the rssi log
     */
    Map<Long, Integer> getRssiLog();

    /**
     * Gets the running average rssi.
     *
     * @return the running average rssi
     */
    double getRunningAverageRssi();

    double getRunningMedianRssi();

    /**
     * Gets the scan record.
     *
     * @return the scan record
     */
    byte[] getScanRecord();

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    long getTimestamp();


    /**
     * Update rssi reading.
     *
     * @param timestamp   the timestamp
     * @param rssiReading the rssi reading
     */
    void updateRssiReading(long timestamp, int rssiReading);

    /**
     * Gets the ad record store.
     *
     * @return the ad record store
     */
    AdRecordStore getAdRecordStore();
}
