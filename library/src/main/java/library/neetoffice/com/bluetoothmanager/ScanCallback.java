package library.neetoffice.com.bluetoothmanager;


import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public interface ScanCallback {
    void onScanResult(BluetoothDevice bluetoothDevice);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void onScanResult(BluetoothLeDevice bluetoothLeDevice);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void onScanResult(IBeaconDevice iBeaconDevice);
}
