package library.neetoffice.com.bluetoothmanager;


import android.annotation.TargetApi;
import android.os.Build;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public interface ScanCallback<T extends BluetoothLeDevice> {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void onScanResult(T bluetoothLeDevice);

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    void onLost(T bluetoothLeDevice);
}
