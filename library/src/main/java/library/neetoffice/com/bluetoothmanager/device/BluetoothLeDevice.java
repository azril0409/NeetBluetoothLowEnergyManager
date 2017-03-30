package library.neetoffice.com.bluetoothmanager.device;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import library.neetoffice.com.bluetoothmanager.device.adrecord.AdRecordStore;

// TODO: Auto-generated Javadoc

/**
 * This is a wrapper around the default BluetoothDevice object
 * As BluetoothDevice is final it cannot be extended, so to get it you
 * need to call {@link #getDevice()} method.
 *
 * @author Alexandros Schillings
 */
public interface BluetoothLeDevice extends Parcelable {
    Creator<BluetoothLeDevice> CREATOR = new Creator<BluetoothLeDevice>() {
        public BluetoothLeDevice createFromParcel(Parcel in) {
            return new BluetoothLeDeviceImpl(in);
        }

        public BluetoothLeDeviceImpl[] newArray(int size) {
            return new BluetoothLeDeviceImpl[size];
        }
    };


    /**
     * Update rssi reading.
     *
     * @param timestamp   the timestamp
     * @param rssiReading the rssi reading
     */
    public void updateRssiReading(long timestamp, int rssiReading);

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
     * Gets the address.
     *
     * @return the address
     */
    String getAddress();

    /**
     * Gets the ad record store.
     *
     * @return the ad record store
     */
    AdRecordStore getAdRecordStore();

    /**
     * Gets the bluetooth device bond state.
     *
     * @return the bluetooth device bond state
     */
    String getBluetoothDeviceBondState();

    /**
     * Gets the device.
     *
     * @return the device
     */
    BluetoothDevice getDevice();

    /**
     * Gets the running average rssi.
     *
     * @return the running average rssi
     */
    double getRunningAverageRssi();


    /**
     * Gets the running median rssi.
     *
     * @return the running median rssi
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    String toString();
}
