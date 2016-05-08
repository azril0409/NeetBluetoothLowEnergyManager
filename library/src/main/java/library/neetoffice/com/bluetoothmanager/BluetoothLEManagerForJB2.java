package library.neetoffice.com.bluetoothmanager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.UUID;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;


/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLEManagerForJB2 extends BluetoothLEManagerImpl {
    private static final String TAG = BluetoothLEManagerForJB2.class.getSimpleName();
    private final Context context;
    private final android.bluetooth.BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean run = false;
    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            final BluetoothLeDeviceImpl bluetoothLeDeviceImpl = new BluetoothLeDeviceImpl(device, rssi, scanRecord, Calendar.getInstance().getTimeInMillis());
            postBluetoothLeDevice(bluetoothLeDeviceImpl);
        }
    };

    public BluetoothLEManagerForJB2(@NonNull Context context) {
        this.context = context;
        bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Override
    public void onCreate() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public boolean startScan(String[] uuidFilters, ScanCallback scanCallback) {
        super.startScan(uuidFilters, scanCallback);
        if (mBluetoothAdapter != null) {
            final UUID[] uuids = new UUID[uuidFilters.length];
            for (int i = 0; i < uuidFilters.length; i++) {
                uuids[i] = UUID.fromString(uuidFilters[i]);
            }
            mBluetoothAdapter.startLeScan(uuids, leScanCallback);
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
    public boolean startScan(ScanCallback scanCallback) {
        super.startScan(scanCallback);
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
        NeetBluetoothLEManager.onDestroy(context);
    }
}
