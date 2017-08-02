package library.neetoffice.com.bluetoothmanager;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

/**
 * Created by Deo-chainmeans on 2017/5/31.
 */

public class IBeaconFilter extends ScanFilter<IBeaconDevice> {
    @Override
    public boolean filter(BluetoothLeDevice bluetoothLeDevice) {
        return IBeaconUtils.isThisAnIBeacon(bluetoothLeDevice);
    }

    @Override
    public IBeaconDevice onSerializable(BluetoothLeDevice bluetoothLeDevice) {
        return new IBeaconDeviceImpl(bluetoothLeDevice);
    }
}
