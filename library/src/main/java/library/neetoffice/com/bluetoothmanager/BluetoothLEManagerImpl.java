package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl.LOG_INVALIDATION_THRESHOLD;

/**
 * Created by Deo on 2016/4/27.
 */
public abstract class BluetoothLEManagerImpl implements BluetoothLEManager {
    static final HashMap<String, BluetoothLeDevice> map = new HashMap<>();
    protected final Context context;
    private final Handler handler = new Handler();
    private final List<String> macFilters = new ArrayList<>();
    private final HashMap<ScanFilter, ScanCallback> callbackHashMap = new HashMap<>();
    private ScanCallback simpleScanCallback;
    private boolean isStartScan = false;
    private boolean isLive = false;

    protected BluetoothLEManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public Set<BluetoothLeDevice> getBondedDevices() {
        return new LinkedHashSet<>(map.values());
    }

    @Override
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN})
    public void onCreate() {
        if (!isLive) {
            isLive = true;
            new CheckRemoveTask().start();
        }
    }

    @Override
    public boolean startScan(ScannerConfig config) {
        synchronized (macFilters) {
            macFilters.clear();
            macFilters.addAll(config.addresses);
        }
        synchronized (callbackHashMap) {
            callbackHashMap.clear();
            callbackHashMap.putAll(config.map);
        }
        if (simpleScanCallback != null) {
            synchronized (simpleScanCallback) {
                simpleScanCallback = config.simpleScanCallback;
            }
        } else {
            simpleScanCallback = config.simpleScanCallback;
        }
        final long time = Calendar.getInstance().getTimeInMillis();
        synchronized (map) {
            for (Map.Entry<String, BluetoothLeDevice> entry : map.entrySet()) {
                final BluetoothLeDevice bluetoothLeDevice = entry.getValue();
                bluetoothLeDevice.updateRssiReading(time, bluetoothLeDevice.getRssi());
            }
        }
        isStartScan = true;
        return isStartScan;
    }

    @Override
    public boolean stopScan() {
        if (simpleScanCallback != null) {
            synchronized (simpleScanCallback) {
                simpleScanCallback = null;
            }
        }
        synchronized (callbackHashMap) {
            callbackHashMap.clear();
        }
        isStartScan = false;
        return !isStartScan;
    }

    @Override
    public void onDestroy() {
        isLive = false;
    }

    protected final void postBluetoothLeDevice(BluetoothLeDeviceImpl bluetoothLeDevice) {
        if (bluetoothLeDevice == null) {
            return;
        }
        final Map.Entry<ScanFilter, ScanCallback> scanEntry = returnScanEntry(bluetoothLeDevice);
        final BluetoothLeDevice device;
        if (map.containsKey(bluetoothLeDevice.getAddress())) {
            device = map.get(bluetoothLeDevice.getAddress());
            device.updateRssiReading(bluetoothLeDevice.getTimestamp(), bluetoothLeDevice.getRssi());
            device.updateScanRecord(bluetoothLeDevice.getScanRecord());
        } else if (scanEntry != null) {
            final ScanFilter filter = scanEntry.getKey();
            device = filter.onSerializable(bluetoothLeDevice);
            map.put(bluetoothLeDevice.getAddress(), device);
        } else {
            device = bluetoothLeDevice;
            map.put(bluetoothLeDevice.getAddress(), device);
        }
        if (scanEntry != null) {
            postResultTask(device, scanEntry.getValue());
        } else if (simpleScanCallback != null) {
            postResultTask(device, simpleScanCallback);
        }
    }

    private void postResultTask(BluetoothLeDevice bluetoothLeDevice, ScanCallback callback) {
        if (macFilters.isEmpty() || macFilters.contains(bluetoothLeDevice.getAddress())) {
            handler.post(new ResultTask(context, bluetoothLeDevice, callback));
        }
    }

    private class CheckRemoveTask extends Thread {
        @Override
        public void run() {
            while (isLive) {
                synchronized (map) {
                    final Iterator<Map.Entry<String, BluetoothLeDevice>> iterator = map.entrySet().iterator();
                    final long nowTime = Calendar.getInstance().getTimeInMillis();
                    while (iterator.hasNext()) {
                        final BluetoothLeDevice device = iterator.next().getValue();
                        if (nowTime - device.getTimestamp() > LOG_INVALIDATION_THRESHOLD) {
                            iterator.remove();
                            final Map.Entry<ScanFilter, ScanCallback> scanEntry = returnScanEntry(device);
                            if (scanEntry != null) {
                                handler.post(new LostTask(context, device, scanEntry.getValue()));
                            } else if (simpleScanCallback != null) {
                                handler.post(new LostTask(context, device, simpleScanCallback));
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(LOG_INVALIDATION_THRESHOLD / 2);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private Map.Entry<ScanFilter, ScanCallback> returnScanEntry(BluetoothLeDevice bluetoothLeDevice) {
        Map.Entry<ScanFilter, ScanCallback> entry = null;
        synchronized (callbackHashMap) {
            for (Map.Entry<ScanFilter, ScanCallback> e : callbackHashMap.entrySet()) {
                final ScanFilter filter = e.getKey();
                if (filter.filter(bluetoothLeDevice)) {
                    entry = e;
                    break;
                }
            }
        }
        return entry;
    }
}
