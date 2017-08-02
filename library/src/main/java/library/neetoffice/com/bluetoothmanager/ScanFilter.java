package library.neetoffice.com.bluetoothmanager;

import android.widget.Filter;

import java.io.Serializable;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2017/5/31.
 */

public abstract class ScanFilter<T extends BluetoothLeDevice> implements Serializable {
    public abstract boolean filter(BluetoothLeDevice bluetoothLeDevice);

    public abstract T onSerializable(BluetoothLeDevice bluetoothLeDevice);
}
