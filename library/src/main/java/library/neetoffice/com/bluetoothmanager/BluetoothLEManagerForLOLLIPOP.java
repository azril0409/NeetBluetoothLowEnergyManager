package library.neetoffice.com.bluetoothmanager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;


/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BluetoothLEManagerForLOLLIPOP extends BluetoothLEManagerImpl {
    private static final String TAG = BluetoothLEManagerForLOLLIPOP.class.getSimpleName();
    private final Context context;
    private final android.bluetooth.BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean run = false;
    private final android.bluetooth.le.ScanCallback leScanCallback = new android.bluetooth.le.ScanCallback() {

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice bluetoothDevice = result.getDevice();
            final ScanRecord scanRecord = result.getScanRecord();
            if (scanRecord == null) {
                Log.d(TAG, String.format("Null ScanRecord for device : %s", bluetoothDevice.getAddress()));
                postBluetoothDevice(bluetoothDevice);
                return;
            } else {
                final BluetoothLeDevice bluetoothLeDevice = new BluetoothLeDevice(bluetoothDevice, result.getRssi(), scanRecord.getBytes(), Calendar.getInstance().getTimeInMillis());
                postBluetoothLeDevice(bluetoothLeDevice);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }
    };

    public BluetoothLEManagerForLOLLIPOP(@NonNull Context context) {
        this.context = context;
        bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Override
    public void onCreate() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter != null || !mBluetoothAdapter.isEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public boolean startScan(String[] uuidFilters, ScanCallback scanCallback) {
        super.startScan(uuidFilters, scanCallback);
        if (mBluetoothAdapter != null && bluetoothLeScanner == null) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (bluetoothLeScanner != null) {
            final ArrayList<ScanFilter> scanFilters = new ArrayList<>();
            for (String uuidFilter : uuidFilters) {
                final ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(uuidFilter))).build();
                scanFilters.add(scanFilter);
            }
            final ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
            bluetoothLeScanner.startScan(scanFilters, settings, leScanCallback);
            run = true;
        } else {
            run = false;
            if (BugConfig.isDebuggable(context)) {
                Log.d(TAG, "BluetoothLeScanner is null");
            }
        }
        return run;
    }

    @Override
    public boolean startScan(ScanCallback scanCallback) {
        super.startScan(scanCallback);
        if (mBluetoothAdapter != null && bluetoothLeScanner == null) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.startScan(leScanCallback);
            run = true;
        } else {
            run = false;
            if (BugConfig.isDebuggable(context)) {
                Log.d(TAG, "BluetoothLeScanner is null");
            }
        }
        return run;
    }

    @Override
    public boolean stopScan() {
        super.stopScan();
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(leScanCallback);
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
