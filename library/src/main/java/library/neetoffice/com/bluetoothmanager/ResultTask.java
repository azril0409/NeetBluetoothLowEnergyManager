package library.neetoffice.com.bluetoothmanager;

import android.content.Context;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2017/5/31.
 */

class ResultTask implements Runnable {
    private final Context context;
    private final BluetoothLeDevice bluetoothLeDevice;
    private final ScanCallback callback;

    ResultTask(Context context, BluetoothLeDevice bluetoothLeDevice, ScanCallback callback) {
        this.context = context;
        this.bluetoothLeDevice = bluetoothLeDevice;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            callback.onScanResult(bluetoothLeDevice);
        } catch (Exception e) {
            if (BugConfig.isDebuggable(context)) {
                e.printStackTrace();
            }
        }
    }
}
