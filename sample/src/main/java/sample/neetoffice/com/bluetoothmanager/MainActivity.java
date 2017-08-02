package sample.neetoffice.com.bluetoothmanager;

import android.Manifest;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import library.neetoffice.com.bluetoothmanager.IBeaconFilter;
import library.neetoffice.com.bluetoothmanager.ScannerConfig;
import library.neetoffice.com.bluetoothmanager.BluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.BLEScanner;
import library.neetoffice.com.bluetoothmanager.ScanCallback;
import library.neetoffice.com.bluetoothmanager.SimpleFilter;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;

public class MainActivity extends AppCompatActivity implements ScanCallback<BluetoothLeDevice>, AdapterView.OnItemClickListener {
    ListView listView;
    BluetoothLEManager manager;
    Adapter adapter = new Adapter(this);
    ArrayMap<String, BluetoothModel> arrayMap = new ArrayMap<>();
    BluetoothLeDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        device = getIntent().getParcelableExtra("BLE");
        manager = BLEScanner.getInstance(this);
        if (checkSelfPermission()) {
            manager.onCreate();
        }
    }

    private boolean checkSelfPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkSelfPermission()) {
            manager.onCreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ScannerConfig config;
        if (device == null) {
            config = new ScannerConfig.Builder().addScanFilter(new IBeaconFilter(), this).addScanFilter(new SimpleFilter(), this).build();
        } else {
            config = new ScannerConfig.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setBluetoothLeDevice(device).addScanFilter(new IBeaconFilter(), this).addScanFilter(new SimpleFilter(), this).build();
        }
        manager.startScan(config);
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
    public void onScanResult(BluetoothLeDevice bluetoothLeDevice) {
        if (arrayMap.containsKey(bluetoothLeDevice.getAddress())) {
            adapter.notifyDataSetChanged();
        } else {
            arrayMap.put(bluetoothLeDevice.getAddress(), new BluetoothModel(bluetoothLeDevice));
            adapter.setAll(arrayMap.values());
        }
    }

    @Override
    public void onLost(BluetoothLeDevice bluetoothLeDevice) {
        arrayMap.remove(bluetoothLeDevice.getAddress());
        adapter.setAll(arrayMap.values());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothModel device = adapter.getItem(position);
        if (device.iBeaconDevice != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("BLE", device.iBeaconDevice);
            startActivity(intent);
        } else if (device.bluetoothLeDevice != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("BLE", device.bluetoothLeDevice);
            startActivity(intent);
        }
    }
}
