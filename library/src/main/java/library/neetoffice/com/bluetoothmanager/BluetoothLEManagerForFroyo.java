package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Deo on 2016/4/27.
 */
public class BluetoothLEManagerForFroyo extends BluetoothLEManagerImpl {
    private static final String TAG = BluetoothLEManagerForFroyo.class.getSimpleName();
    private final Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean run = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                postBluetoothDevice(device);
            }
        }
    };

    public BluetoothLEManagerForFroyo(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothAdapter != null || !mBluetoothAdapter.isEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    @Deprecated
    public boolean startScan(String[] uuidFilters, ScanCallback scanCallback) {
        return startScan(scanCallback);
    }

    @Override
    public boolean startScan(ScanCallback scanCallback) {
        super.startScan(scanCallback);
        final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        try {
            context.registerReceiver(mReceiver, filter);
            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.startDiscovery();
            }
            run = true;
        } catch (Exception e) {
            if (BugConfig.isDebuggable(context)) {
                Log.d(TAG, e.toString());
            }
        }
        return run;
    }

    @Override
    public boolean stopScan() {
        super.stopScan();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
            run = false;
        }
        try {
            context.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            if (BugConfig.isDebuggable(context)) {
                Log.d(TAG, e.toString());
            }
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
