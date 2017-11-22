package library.chainmeans.com.scanner.support.ibeacon;


import library.neetoffice.com.bluetoothmanager.ScanFilter;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2017/11/20.
 */

public class IBeaconFilter extends ScanFilter<IBeaconDevice> {
    @Override
    public boolean filter(BluetoothLeDevice bluetoothLeDevice) {
        return IBeaconUtil.isThisAnIBeacon(bluetoothLeDevice);
    }

    @Override
    public IBeaconDevice onReEncapsulation(BluetoothLeDevice bluetoothLeDevice) {
        return new IBeaconDeviceImpl(bluetoothLeDevice);
    }
}
