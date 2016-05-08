package library.neetoffice.com.bluetoothmanager.device;

import library.neetoffice.com.bluetoothmanager.device.mfdata.IBeaconManufacturerData;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

/**
 * Created by Mac on 2016/05/08.
 */
public interface IBeaconDevice extends BluetoothLeDevice {


    /**
     * Gets the estimated Accuracy of the reading in meters based on
     * a simple running average of the last {@link #MAX_RSSI_LOG_SIZE}
     * samples.
     *
     * @return the accuracy in meters
     */
    double getAccuracy();

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
    IBeaconUtils.IBeaconDistanceDescriptor getDistanceDescriptor();

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
