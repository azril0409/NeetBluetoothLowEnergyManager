package library.neetoffice.com.bluetoothmanager.device;

import android.os.Parcel;

import library.neetoffice.com.bluetoothmanager.device.mfdata.IBeaconManufacturerData;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

public interface IBeaconDevice extends BluetoothLeDevice {
    Creator<IBeaconDevice> CREATOR = new Creator<IBeaconDevice>() {
        public IBeaconDevice createFromParcel(Parcel in) {
            return new IBeaconDeviceImpl(in);
        }

        public IBeaconDevice[] newArray(int size) {
            return new IBeaconDevice[size];
        }
    };

    /**
     * Gets the estimated Accuracy of the reading in meters based on
     * a simple running average of the last {@link #MAX_RSSI_LOG_SIZE}
     * samples.
     *
     * @return the accuracy in meters
     */
    double getAccuracy();

    /**
     * Gets the estimated Distance of the reading in meters based on
     * a simple running average of the last {@link #MAX_RSSI_LOG_SIZE}
     * samples.
     *
     * @return the accuracy in meters
     */
    double getDistance(float coefficient);

    /**
     * Gets the calibrated TX power of the iBeacon device as reported.
     *
     * @return the calibrated TX power
     */
    int getCalibratedTxPower();

    /**
     * Gets the iBeacon company identifier.
     *
     * @return the company identifier
     */
    int getCompanyIdentifier();

    /**
     * Gets the estimated Distance descriptor.
     *
     * @return the distance descriptor
     */
    IBeaconUtils.IBeaconDistanceDescriptor getDistanceDescriptor(float coefficient);

    /**
     * Gets the iBeacon manufacturing data.
     *
     * @return the iBeacon data
     */
    IBeaconManufacturerData getIBeaconData();

    /**
     * Gets the iBeacon name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the iBeacon Major value.
     *
     * @return the Major value
     */
    int getMajor();

    /**
     * Gets the iBeacon Minor value.
     *
     * @return the Minor value
     */
    int getMinor();

    /**
     * Gets the iBeacon UUID.
     *
     * @return the UUID
     */
    String getUUID();

}
