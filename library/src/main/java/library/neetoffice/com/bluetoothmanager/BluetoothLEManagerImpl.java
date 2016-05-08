package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

/**
 * Created by Deo on 2016/4/27.
 */
public abstract class BluetoothLEManagerImpl implements BluetoothLEManager {
    private final Handler handler = new Handler();
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

    protected final void postBluetoothLeDevice(BluetoothLeDeviceImpl bluetoothLeDeviceImpl) {
        handler.post(new Task(bluetoothLeDeviceImpl));
    }

    protected final void postBluetoothDevice(BluetoothDevice bluetoothDevice) {
        handler.post(new Task(bluetoothDevice));
    }

    private class Task implements Runnable {
        private final BluetoothLeDeviceImpl bluetoothLeDeviceImpl;
        private final BluetoothDevice bluetoothDevice;

        private Task(BluetoothLeDeviceImpl bluetoothLeDeviceImpl) {
            this.bluetoothLeDeviceImpl = bluetoothLeDeviceImpl;
            bluetoothDevice = null;
        }


        public Task(BluetoothDevice bluetoothDevice) {
            bluetoothLeDeviceImpl = null;
            this.bluetoothDevice = bluetoothDevice;
        }

        @Override
        public void run() {
            if (scanCallback != null) {
                synchronized (scanCallback) {
                    if (bluetoothLeDeviceImpl != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (IBeaconUtils.isThisAnIBeacon(bluetoothLeDeviceImpl)) {
                            final IBeaconDeviceImpl iBeaconDevice = new IBeaconDeviceImpl(bluetoothLeDeviceImpl);
                            scanCallback.onScanResult(iBeaconDevice);
                        } else {
                            scanCallback.onScanResult(bluetoothLeDeviceImpl);
                        }
                    }
                    if (bluetoothDevice != null) {
                        scanCallback.onScanResult(bluetoothLeDeviceImpl);
                    }
                }
            }
        }
    }
}
