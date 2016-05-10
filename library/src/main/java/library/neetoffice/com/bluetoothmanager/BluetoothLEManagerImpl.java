package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.HashMap;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

/**
 * Created by Deo on 2016/4/27.
 */
public abstract class BluetoothLEManagerImpl implements BluetoothLEManager {
    private final Handler handler = new Handler();
    private final HashMap<String, BluetoothLeDevice> map = new HashMap<>();
    private ScanCallback scanCallback;

    @Override
    public boolean startScan(String[] uuidFilters, ScanCallback scanCallback) {
        this.scanCallback = scanCallback;
        return false;
    }

    @Override
    public boolean startScan(ScanCallback scanCallback) {
        this.scanCallback = scanCallback;
        return false;
    }

    @Override
    public boolean stopScan() {
        scanCallback = null;
        return false;
    }

    protected final void postBluetoothLeDevice(@NonNull BluetoothLeDevice bluetoothLeDevice) {
        if (bluetoothLeDevice == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothLeDevice device = null;
            if (map.containsKey(bluetoothLeDevice.getAddress())) {
                device = map.get(bluetoothLeDevice.getAddress());
                device.updateRssiReading(bluetoothLeDevice.getTimestamp(), bluetoothLeDevice.getRssi());
            } else {
                if (IBeaconUtils.isThisAnIBeacon(bluetoothLeDevice)) {
                    device = new IBeaconDeviceImpl(bluetoothLeDevice);
                } else {
                    device = bluetoothLeDevice;
                }
                map.put(bluetoothLeDevice.getAddress(), device);
            }
            handler.post(new Task(device));
        } else {
            handler.post(new Task(bluetoothLeDevice));
        }

    }

    protected final void postBluetoothDevice(@NonNull BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return;
        }
        handler.post(new Task(bluetoothDevice));
    }

    private class Task implements Runnable {
        private final BluetoothLeDevice bluetoothLeDevice;
        private final BluetoothDevice bluetoothDevice;

        private Task(BluetoothLeDevice bluetoothLeDevice) {
            this.bluetoothLeDevice = bluetoothLeDevice;
            bluetoothDevice = null;
        }


        public Task(BluetoothDevice bluetoothDevice) {
            bluetoothLeDevice = null;
            this.bluetoothDevice = bluetoothDevice;
        }

        @Override
        public void run() {
            if (scanCallback != null) {
                synchronized (scanCallback) {
                    if (bluetoothLeDevice instanceof IBeaconDevice) {
                        final IBeaconDevice iBeaconDevice = (IBeaconDevice) bluetoothLeDevice;
                        scanCallback.onScanResult(iBeaconDevice);
                    } else {
                        scanCallback.onScanResult(bluetoothLeDevice);
                    }
                    if (bluetoothDevice != null) {
                        scanCallback.onScanResult(bluetoothDevice);
                    }
                }
            }
        }
    }
}
