package library.neetoffice.com.bluetoothmanager;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2017/5/31.
 */

public final class SimpleFilter extends ScanFilter<BluetoothLeDevice> {
    @Override
    public boolean filter(BluetoothLeDevice bluetoothLeDevice) {
        return true;
    }

    @Override
    public BluetoothLeDevice onReEncapsulation(BluetoothLeDevice bluetoothLeDevice) {
        return bluetoothLeDevice;
    }
}
