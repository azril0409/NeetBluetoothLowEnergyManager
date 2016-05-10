package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import library.neetoffice.com.bluetoothmanager.BluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.NeetBluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.ScanCallback;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;

public class MainActivity extends AppCompatActivity implements ScanCallback {
    ListView listView;
    BluetoothLEManager manager;
    Adapter adapter = new Adapter(this);
    ArrayMap<String, BluetoothModel> arrayMap = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        manager = NeetBluetoothLEManager.getInstance(this);
        manager.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.startScan(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.stopScan();
    }

    @Override
    protected void onDestroy() {
        manager.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onScanResult(BluetoothDevice bluetoothDevice) {
        if (arrayMap.containsKey(bluetoothDevice.getAddress())) {
            adapter.notifyDataSetChanged();
        } else {
            arrayMap.put(bluetoothDevice.getAddress(), new BluetoothModel(bluetoothDevice));
            adapter.setAll(arrayMap.values());
        }
    }

    @Override
    public void onScanResult(BluetoothLeDevice bluetoothLeDevice) {
        if (arrayMap.containsKey(bluetoothLeDevice.getAddress())) {
            adapter.notifyDataSetChanged();
        } else {
            arrayMap.put(bluetoothLeDevice.getAddress(), new BluetoothModel(bluetoothLeDevice));
            adapter.setAll(arrayMap.values());
        }
    }

    @Override
    public void onScanResult(IBeaconDevice iBeaconDevice) {
        if (arrayMap.containsKey(iBeaconDevice.getAddress())) {
            adapter.notifyDataSetChanged();
        } else {
            arrayMap.put(iBeaconDevice.getAddress(), new BluetoothModel(iBeaconDevice));
            adapter.setAll(arrayMap.values());
        }
    }
}
