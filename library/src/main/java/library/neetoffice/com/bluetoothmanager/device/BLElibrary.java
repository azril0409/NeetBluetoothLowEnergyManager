package library.neetoffice.com.bluetoothmanager.device;

import java.util.HashSet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

public class BLElibrary {
	private final Handler handler;
	private HashSet<BLEDevice> devices = new HashSet<BLEDevice>();
	private LeScanCallback callback = new LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			BLEDevice bLEDevice = new BLEDevice(device, rssi, scanRecord);
			devices.add(bLEDevice);
		}
	};
	private final LeScanCallback leScanCallback;
	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (leScanCallback == null)return;
			if(!isRun)return;
			for (BLEDevice device : devices) {
				leScanCallback.onLeScan(device.device, device.rssi,device.scanRecord);
			}
			handler.postDelayed(runnable, 1000);
		}
	};
	private BluetoothAdapter bluetoothAdapter;
	private boolean isRun = false;
	public BLElibrary(Context context, LeScanCallback leScanCallback) {
		this.leScanCallback = leScanCallback;
		handler = new Handler();
		BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
	}

	public void start() {
		try {
			isRun = true;
			handler.post(runnable);
			if (bluetoothAdapter != null) {
				bluetoothAdapter.startLeScan(callback);
			}
		} catch (Exception e) {

		}
	}

	public void stop() {
		try {
			isRun = false;
			if (bluetoothAdapter != null) {
				bluetoothAdapter.stopLeScan(callback);
			}
		} catch (Exception e) {

		}
	}

}
