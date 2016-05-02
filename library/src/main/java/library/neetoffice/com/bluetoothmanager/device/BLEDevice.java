package library.neetoffice.com.bluetoothmanager.device;

import android.bluetooth.BluetoothDevice;

public class BLEDevice {
	final BluetoothDevice device;
	final int rssi;
	final byte[] scanRecord;
	public BLEDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
		this.device = device;
		this.rssi = rssi;
		this.scanRecord = scanRecord;
	}
	
	
}
