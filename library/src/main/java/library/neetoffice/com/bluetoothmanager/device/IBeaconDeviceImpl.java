package library.neetoffice.com.bluetoothmanager.device;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;

import library.neetoffice.com.bluetoothmanager.device.mfdata.IBeaconManufacturerData;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

public class IBeaconDeviceImpl extends BluetoothLeDeviceImpl implements IBeaconDevice {

    /**
     * The m iBeacon data.
     */
    private final IBeaconManufacturerData mIBeaconData;

    /**
     * Instantiates a new iBeacon device.
     *
     * @param device     the device
     * @param rssi       the RSSI value
     * @param scanRecord the scanRecord
     * @throws IllegalArguementException if the passed device is not an iBecon
     */
    public IBeaconDeviceImpl(BluetoothDevice device, int rssi, byte[] scanRecord) {
        super(device, rssi, scanRecord, 0);
        validate();
        mIBeaconData = new IBeaconManufacturerData(this);
    }

    /**
     * Instantiates a new iBeacon device.
     *
     * @param device     the device
     * @param rssi       the RSSI value of the RSSI measurement
     * @param scanRecord the scan record
     * @param timestamp  the timestamp of the RSSI measurement
     * @throws IllegalArguementException if the passed device is not an iBecon
     */
    public IBeaconDeviceImpl(BluetoothDevice device, int rssi, byte[] scanRecord, long timestamp) {
        super(device, rssi, scanRecord, timestamp);
        validate();
        mIBeaconData = new IBeaconManufacturerData(this);
    }

    /**
     * Will try to convert a {@link BluetoothLeDeviceImpl} into an
     * iBeacon Device.
     *
     * @param device the device
     * @throws IllegalArguementException if the passed device is not an iBecon
     */
    public IBeaconDeviceImpl(BluetoothLeDeviceImpl device) {
        super(device);
        validate();
        mIBeaconData = new IBeaconManufacturerData(this);
    }

    private IBeaconDeviceImpl(Parcel in) {
        super(in);
        validate();
        mIBeaconData = new IBeaconManufacturerData(this);
    }

    /**
     * Gets the estimated Accuracy of the reading in meters based on
     * a simple running average of the last {@link #MAX_RSSI_LOG_SIZE}
     * samples.
     *
     * @return the accuracy in meters
     */
    @Override
    public double getAccuracy() {
        return IBeaconUtils.calculateAccuracy(getCalibratedTxPower(), getRunningAverageRssi());
    }

    /**
     * Gets the calibrated TX power of the iBeacon device as reported.
     *
     * @return the calibrated TX power
     */
    @Override
    public int getCalibratedTxPower() {
        return getIBeaconData().getCalibratedTxPower();
    }

    /**
     * Gets the iBeacon company identifier.
     *
     * @return the company identifier
     */
    @Override
    public int getCompanyIdentifier() {
        return getIBeaconData().getCompanyIdentifier();
    }

    /**
     * Gets the estimated Distance descriptor.
     *
     * @return the distance descriptor
     */
    @Override
    public IBeaconUtils.IBeaconDistanceDescriptor getDistanceDescriptor() {
        return IBeaconUtils.getDistanceDescriptor(getAccuracy());
    }

    /**
     * Gets the iBeacon manufacturing data.
     *
     * @return the iBeacon data
     */
    public IBeaconManufacturerData getIBeaconData() {
        return mIBeaconData;
    }

    /**
     * Gets the iBeacon Major value.
     *
     * @return the Major value
     */
    @Override
    public int getMajor() {
        return getIBeaconData().getMajor();
    }

    /**
     * Gets the iBeacon Minor value.
     *
     * @return the Minor value
     */
    @Override
    public int getMinor() {
        return getIBeaconData().getMinor();
    }

    /**
     * Gets the iBeacon UUID.
     *
     * @return the UUID
     */
    @Override
    public String getUUID() {
        return getIBeaconData().getUUID();
    }

    private void validate() {
        if (!IBeaconUtils.isThisAnIBeacon(this)) {
            throw new IllegalArgumentException("Device " + getDevice() + " is not an iBeacon.");
        }
    }


}
