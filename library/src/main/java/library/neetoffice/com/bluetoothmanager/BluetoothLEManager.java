package library.neetoffice.com.bluetoothmanager;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public interface BluetoothLEManager {
    void onCreate();

    boolean startScan(String[] uuidFilters, ScanCallback scanCallback);

    boolean startScan(ScanCallback scanCallback);

    boolean stopScan();

    boolean isRun();

    void onDestroy();
}
