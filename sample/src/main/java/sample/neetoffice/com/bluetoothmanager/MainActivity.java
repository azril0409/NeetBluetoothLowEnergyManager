package sample.neetoffice.com.bluetoothmanager;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import library.neetoffice.com.bluetoothmanager.BluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.NeetBluetoothLEManager;
import library.neetoffice.com.bluetoothmanager.ScanCallback;
import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.util.ByteUtils;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;

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
        if (checkSelfPermission()) {
            manager.onCreate();
        }
    }

    private boolean checkSelfPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
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

    @Override
    public void onLost(BluetoothLeDevice bluetoothLeDevice) {
        arrayMap.remove(bluetoothLeDevice.getAddress());
        adapter.setAll(arrayMap.values());

    }

    @Override
    public void onLost(IBeaconDevice iBeaconDevice) {
        arrayMap.remove(iBeaconDevice.getAddress());
        adapter.setAll(arrayMap.values());
    }
}
