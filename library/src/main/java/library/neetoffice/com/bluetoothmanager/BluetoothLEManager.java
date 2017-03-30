package library.neetoffice.com.bluetoothmanager;

import android.support.annotation.RequiresPermission;

import java.util.Set;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public interface BluetoothLEManager {
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN})
    void onCreate();

    Set<BluetoothLeDevice> getBondedDevices();

    boolean startScan(String[] uuidFilters, ScanCallback scanCallback);

    boolean startScan(ScanCallback scanCallback);

    boolean stopScan();

    boolean isRun();

    void onDestroy();
}
