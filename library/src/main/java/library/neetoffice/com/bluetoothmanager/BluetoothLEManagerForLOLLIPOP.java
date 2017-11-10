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
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;


/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BluetoothLEManagerForLOLLIPOP extends BluetoothLEManagerImpl {
    private static final String TAG = BluetoothLEManagerForLOLLIPOP.class.getSimpleName();
    private final android.bluetooth.BluetoothManager bluetoothManager;
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
            if (scanRecord != null) {
                final BluetoothLeDeviceImpl bluetoothLeDeviceImpl = new BluetoothLeDeviceImpl(bluetoothDevice, result.getRssi(), scanRecord.getBytes(), Calendar.getInstance().getTimeInMillis());
                postBluetoothLeDevice(bluetoothLeDeviceImpl);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }
    };
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean run = false;

    public BluetoothLEManagerForLOLLIPOP(Context context) {
        super(context);
        bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    @Override
    public void onCreate() throws SecurityException {
        super.onCreate();
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
        }
    }

    @Override
    public boolean startScan(ScannerConfig config) {
        super.startScan(config);
        ArrayList<ScanFilter> scanFilters = null;
        if (config.addresses != null) {
            scanFilters = new ArrayList<>();
            for (String address : config.addresses) {
                final ScanFilter scanFilter = new ScanFilter.Builder().setDeviceAddress(address).build();
                scanFilters.add(scanFilter);
            }
        }
        return startScan(scanFilters, config);
    }

    private boolean startScan(ArrayList<ScanFilter> scanFilters, ScannerConfig config) {
        if (mBluetoothAdapter != null && bluetoothLeScanner == null) {
            bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (bluetoothLeScanner != null) {
            final ScanSettings settings = new ScanSettings.Builder().setScanMode(config.scanMode).build();
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
        super.onDestroy();
        BluetoothLEScanner.onDestroy(context);
    }
}
