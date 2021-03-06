package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;

import library.chainmeans.com.scanner.support.ibeacon.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo on 2016/4/27.
 */
public class BluetoothModel {
    BluetoothDevice bluetoothDevice;
    BluetoothLeDevice bluetoothLeDevice;
    IBeaconDevice iBeaconDevice;


    public BluetoothModel(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothModel(BluetoothLeDevice bluetoothLeDevice) {
        this.bluetoothLeDevice = bluetoothLeDevice;
    }

    public BluetoothModel(IBeaconDevice iBeaconDevice) {
        this.iBeaconDevice = iBeaconDevice;
    }
}
