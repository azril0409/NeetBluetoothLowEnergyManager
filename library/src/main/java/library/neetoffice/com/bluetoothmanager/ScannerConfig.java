package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.le.ScanSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;

/**
 * Created by Deo-chainmeans on 2017/5/23.
 */

public class ScannerConfig {
    public static final int SCAN_MODE_OPPORTUNISTIC = ScanSettings.SCAN_MODE_OPPORTUNISTIC;
    public static final int SCAN_MODE_LOW_POWER = ScanSettings.SCAN_MODE_LOW_POWER;
    public static final int SCAN_MODE_BALANCED = ScanSettings.SCAN_MODE_BALANCED;
    public static final int SCAN_MODE_LOW_LATENCY = ScanSettings.SCAN_MODE_LOW_LATENCY;
    final int scanMode;
    final HashSet<String> addresses;
    final HashMap<ScanFilter, ScanCallback> map;
    final ScanCallback simpleScanCallback;

    private ScannerConfig(int scanMode, HashSet<String> addresses, HashMap<ScanFilter, ScanCallback> map, ScanCallback simpleScanCallback) {
        this.scanMode = scanMode;
        this.addresses = addresses;
        this.map = map;
        this.simpleScanCallback = simpleScanCallback;
    }

    public static class Builder {
        private int scanMode;
        private HashSet<String> addresses = new HashSet<>();
        private HashMap<ScanFilter, ScanCallback> map = new HashMap<>();
        private ScanCallback simpleScanCallback;

        public Builder() {
            scanMode = SCAN_MODE_LOW_LATENCY;
        }

        public Builder setDeviceAddress(String address) {
            addresses.add(address);
            return this;
        }

        public Builder setDeviceAddress(String... address) {
            addresses.addAll(Arrays.asList(address));
            return this;
        }

        public Builder setBluetoothLeDevice(BluetoothLeDevice bluetoothLeDevice) {
            addresses.add(bluetoothLeDevice.getAddress());
            return this;
        }

        public Builder setBluetoothLeDevices(BluetoothLeDevice... bluetoothLeDevices) {
            for (BluetoothLeDevice bluetoothLeDevice : bluetoothLeDevices) {
                addresses.add(bluetoothLeDevice.getAddress());
            }
            return this;
        }

        public <T extends BluetoothLeDevice> Builder addScanFilter(ScanFilter<T> scanFilter, ScanCallback<T> scanCallback) {
            if (scanFilter instanceof SimpleFilter) {
                simpleScanCallback = scanCallback;
            } else {
                map.put(scanFilter, scanCallback);
            }
            return this;
        }

        public Builder setScanMode(int scanMode) {
            this.scanMode = scanMode;
            return this;
        }

        public ScannerConfig build() {
            return new ScannerConfig(scanMode, addresses, map, simpleScanCallback);
        }
    }
}
