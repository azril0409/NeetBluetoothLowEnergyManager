package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import library.neetoffice.com.bluetoothmanager.BluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.NeetBluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.ScanCallback;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDeviceImpl;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDeviceImpl;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, ScanCallback {
    Toolbar toolbar;
    ListView listView;
    BluetoothLEManager manager;
    Adapter adapter = new Adapter(this);
    ArrayMap<String, BluetoothModel> arrayMap = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listView);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);
        listView.setAdapter(adapter);
        manager = NeetBluetoothLEManager.getInstance(this);
        manager.onCreate();
    }

    @Override
    protected void onDestroy() {
        manager.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (R.id.start == item.getItemId()) {
            manager.startScan(this);
            return true;
        } else if (R.id.stop == item.getItemId()) {
            manager.stopScan();
            return true;
        }
        return false;
    }

    @Override
    public void onScanResult(BluetoothDevice bluetoothDevice) {
        arrayMap.put(bluetoothDevice.getAddress(), new BluetoothModel(bluetoothDevice));
        adapter.setAll(arrayMap.values());
    }

    @Override
    public void onScanResult(BluetoothLeDevice bluetoothLeDevice) {
        arrayMap.put(bluetoothLeDevice.getAddress(), new BluetoothModel(bluetoothLeDevice));
        adapter.setAll(arrayMap.values());
    }

    @Override
    public void onScanResult(IBeaconDevice iBeaconDevice) {
        arrayMap.put(iBeaconDevice.getAddress(), new BluetoothModel(iBeaconDevice));
        adapter.setAll(arrayMap.values());
    }
}
