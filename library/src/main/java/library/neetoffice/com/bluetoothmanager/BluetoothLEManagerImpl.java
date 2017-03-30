package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

/**
 * Created by Deo on 2016/4/27.
 */
public abstract class BluetoothLEManagerImpl implements BluetoothLEManager {
    static final HashMap<String, BluetoothLeDevice> map = new HashMap<>();
    private final Handler handler = new Handler();
    private ScanCallback scanCallback;
    private boolean isStartScan = false;

    @Override
    public Set<BluetoothLeDevice> getBondedDevices() {
        return new LinkedHashSet<>(map.values());
    }

    @Override
    public boolean startScan(String[] uuidFilters, ScanCallback scanCallback) {
        this.scanCallback = scanCallback;
        final long time = Calendar.getInstance().getTimeInMillis();
        for (Map.Entry<String, BluetoothLeDevice> entry : map.entrySet()) {
            final BluetoothLeDevice bluetoothLeDevice = entry.getValue();
            bluetoothLeDevice.updateRssiReading(time, bluetoothLeDevice.getRssi());
        }
        if (!isStartScan) {
            new CheckRemoveTask().start();
        }
        isStartScan = true;
        return isStartScan;
    }

    @Override
    public boolean startScan(ScanCallback scanCallback) {
        return startScan(null, scanCallback);
    }

    @Override
    public boolean stopScan() {
        scanCallback = null;
        isStartScan = false;
        return !isStartScan;
    }

    @Override
    public void onDestroy() {
    }

    protected final void postBluetoothLeDevice(BluetoothLeDeviceImpl bluetoothLeDevice) {
        if (bluetoothLeDevice == null) {
            return;
        }
        Log.d(BluetoothLEManager.class.getSimpleName(), "" + bluetoothLeDevice.getAddress() + "," + bluetoothLeDevice.getRssi());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothLeDeviceImpl device = null;
            if (map.containsKey(bluetoothLeDevice.getAddress())) {
                device = (BluetoothLeDeviceImpl) map.get(bluetoothLeDevice.getAddress());
                device.updateRssiReading(bluetoothLeDevice.getTimestamp(), bluetoothLeDevice.getRssi());
                device.updateScanRecord(bluetoothLeDevice.getScanRecord());
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

    private class CheckRemoveTask extends Thread {
        @Override
        public void run() {
            while (isStartScan) {
                synchronized (map) {
                    final Iterator<Map.Entry<String, BluetoothLeDevice>> iterator = map.entrySet().iterator();
                    final long nowTime = Calendar.getInstance().getTimeInMillis();
                    while (iterator.hasNext()) {
                        final Map.Entry<String, BluetoothLeDevice> entry = iterator.next();
                        final BluetoothLeDevice bluetoothLeDevice = entry.getValue();
                        if (nowTime - bluetoothLeDevice.getTimestamp() > BluetoothLeDeviceImpl.LOG_INVALIDATION_THRESHOLD) {
                            iterator.remove();
                            handler.post(new LostTask(bluetoothLeDevice));
                        }
                    }
                }
                try {
                    Thread.sleep(BluetoothLeDeviceImpl.LOG_INVALIDATION_THRESHOLD / 2);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private class LostTask implements Runnable {
        private final BluetoothLeDevice bluetoothLeDevice;

        private LostTask(BluetoothLeDevice bluetoothLeDevice) {
            this.bluetoothLeDevice = bluetoothLeDevice;
        }

        @Override
        public void run() {
            if (scanCallback != null) {
                synchronized (scanCallback) {
                    if (bluetoothLeDevice instanceof IBeaconDevice) {
                        final IBeaconDevice iBeaconDevice = (IBeaconDevice) bluetoothLeDevice;
                        scanCallback.onLost(iBeaconDevice);
                    } else {
                        scanCallback.onLost(bluetoothLeDevice);
                    }
                }
            }
        }
    }

    private class Task implements Runnable {
        private final BluetoothLeDevice bluetoothLeDevice;

        private Task(BluetoothLeDevice bluetoothLeDevice) {
            this.bluetoothLeDevice = bluetoothLeDevice;
        }

        @Override
        public void run() {
            if (scanCallback != null) {
                synchronized (scanCallback) {
                    if (bluetoothLeDevice != null) {
                        if (bluetoothLeDevice instanceof IBeaconDevice) {
                            final IBeaconDevice iBeaconDevice = (IBeaconDevice) bluetoothLeDevice;
                            scanCallback.onScanResult(iBeaconDevice);
                        } else {
                            scanCallback.onScanResult(bluetoothLeDevice);
                        }
                    }
                }
            }
        }
    }
}
