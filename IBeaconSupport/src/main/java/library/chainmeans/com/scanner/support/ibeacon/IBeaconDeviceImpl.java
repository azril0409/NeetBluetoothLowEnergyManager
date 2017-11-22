package library.chainmeans.com.scanner.support.ibeacon;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;

/**
 * Created by Deo-chainmeans on 2017/11/20.
 * Parses the Manufactured Data field of an iBeacon
 * <p>
 * The parsing is based on the following schema:
 * <pre>
 * Byte|Value
 * -------------------------------------------------
 * 0	4C - Byte 1 (LSB) of Company identifier code
 * 1	00 - Byte 0 (MSB) of Company identifier code (0x004C == Apple)
 * 2	02 - Byte 0 of iBeacon advertisement indicator
 * 3	15 - Byte 1 of iBeacon advertisement indicator
 * 4	        e2 |\
 * 5	        c5 |\\
 * 6	        6d |#\\
 * 7	        b5 |##\\
 * 8    	df |###\\
 * 9          fb |####\\
 * 10	48 |#####\\
 * 11	d2 |#####|| iBeacon
 * 12	b0 |#####|| Proximity UUID
 * 13	60 |#####//
 * 14	d0 |####//
 * 15	f5 |###//
 * 16	a7 |##//
 * 17	10 |#//
 * 18	96 |//
 * 19	e0 |/
 * 20	00 - major
 * 21	00
 * 22	00 - minor
 * 23	00
 * 24	c5 - The 2's complement of the calibrated Tx Power
 */

public class IBeaconDeviceImpl extends BluetoothLeDeviceImpl implements IBeaconDevice {
    private final IBeaconManufacturerData iBeaconData;

    public IBeaconDeviceImpl(BluetoothDevice device, int rssi, byte[] scanRecord, long timestamp) {
        super(device, rssi, scanRecord, timestamp);
        iBeaconData = new IBeaconManufacturerData(this);
    }

    public IBeaconDeviceImpl(BluetoothLeDevice device) {
        super(device);
        iBeaconData = new IBeaconManufacturerData(this);
    }

    protected IBeaconDeviceImpl(Parcel in) {
        super(in);
        iBeaconData = new IBeaconManufacturerData(in);
    }

    @Override
    public double getAccuracy() {
        return IBeaconUtil.calculateAccuracy(getCalibratedTxPower(), getRunningAverageRssi());
    }

    @Override
    public double getDistance(float coefficient) {
        return IBeaconUtil.calculateDistance(getCalibratedTxPower(), getRunningAverageRssi(), coefficient);
    }

    @Override
    public int getCalibratedTxPower() {
        return iBeaconData.calibratedTxPower;
    }

    @Override
    public int getCompanyIdentifier() {
        return iBeaconData.companyIdentidier;
    }

    @Override
    public IBeaconDistanceDescriptor getDistanceDescriptor() {
        return IBeaconUtil.getDistanceDescriptor(getAccuracy());
    }

    @Override
    public IBeaconManufacturerData getIBeaconData() {
        return iBeaconData;
    }

    @Override
    public int getMajor() {
        return iBeaconData.major;
    }

    @Override
    public int getMinor() {
        return iBeaconData.minor;
    }

    @Override
    public String getUUID() {
        return iBeaconData.mUUID;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeParcelable(iBeaconData, flags);
    }
}
