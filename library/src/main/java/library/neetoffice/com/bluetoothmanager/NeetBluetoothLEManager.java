package library.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public class NeetBluetoothLEManager {
    private static final HashMap<String, BluetoothLEManager> bluetoothLEManagers = new HashMap<>();

    public static final BluetoothLEManager getInstance(Context context) {
        if (bluetoothLEManagers.containsKey(context.getClass().getSimpleName())) {
            return bluetoothLEManagers.get(context.getClass().getSimpleName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForLOLLIPOP(context);
            bluetoothLEManagers.put(context.getClass().getSimpleName(), bluetoothManager);
            return bluetoothManager;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForJB2(context);
            bluetoothLEManagers.put(context.getClass().getSimpleName(), bluetoothManager);
            return bluetoothManager;
        } else {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForFroyo(context);
            bluetoothLEManagers.put(context.getClass().getSimpleName(), bluetoothManager);
            return bluetoothManager;
        }
    }

    static void onDestroy(Context context) {
        bluetoothLEManagers.remove(context.getClass().getSimpleName());
    }
}
