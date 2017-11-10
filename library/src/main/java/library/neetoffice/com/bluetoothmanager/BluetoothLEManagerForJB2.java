package library.neetoffice.com.bluetoothmanager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;


/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLEManagerForJB2 extends BluetoothLEManagerImpl {
    private static final String TAG = BluetoothLEManagerForJB2.class.getSimpleName();
    private final android.bluetooth.BluetoothManager bluetoothManager;
    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            final BluetoothLeDeviceImpl bluetoothLeDeviceImpl = new BluetoothLeDeviceImpl(device, rssi, scanRecord, Calendar.getInstance().getTimeInMillis());
            postBluetoothLeDevice(bluetoothLeDeviceImpl);
        }
    };
    private BluetoothAdapter mBluetoothAdapter;
    private boolean run = false;

    public BluetoothLEManagerForJB2(Context context) {
        super(context);
        bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Override
    public void onCreate() throws SecurityException {
        super.onCreate();
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    @Override
    public boolean startScan(ScannerConfig config) {
        super.startScan(config);
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.startLeScan(leScanCallback);
            run = true;
        } else {
            run = false;
            if (BugConfig.isDebuggable(context)) {
                Log.d(TAG, "mBluetoothAdapter is null");
            }
        }
        return run;
    }

    @Override
    public boolean stopScan() {
        super.stopScan();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(leScanCallback);
            run = false;
        }
        return !run;
    }

    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BluetoothLEScanner.onDestroy(context);
    }
}
