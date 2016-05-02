package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;

/**
 * Created by Deo on 2016/4/27.
 */
public class BluetoothModel {
    BluetoothDevice bluetoothDevice;
    BluetoothLeDevice bluetoothLeDevice;
    IBeaconDevice iBeaconDevice;


    public BluetoothModel(BluetoothDevice bluetoothDevice) {
    }

    public BluetoothModel(BluetoothLeDevice bluetoothLeDevice) {
    }

    public BluetoothModel(IBeaconDevice iBeaconDevice) {
    }
}
