package library.neetoffice.com.bluetoothmanager;

import android.content.Context;
import android.os.Build;

import java.util.HashMap;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public abstract class NeetBluetoothLEManager {
    private static final HashMap<String, BluetoothLEManager> bluetoothLEManagers = new HashMap<>();

    public static final BluetoothLEManager getInstance(Context context) {
        if (bluetoothLEManagers.containsKey(context.getClass().getName())) {
            return bluetoothLEManagers.get(context.getClass().getName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForLOLLIPOP(context);
            bluetoothLEManagers.put(context.getClass().getName(), bluetoothManager);
            return bluetoothManager;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForJB2(context);
            bluetoothLEManagers.put(context.getClass().getName(), bluetoothManager);
            return bluetoothManager;
        } else {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForFroyo(context);
            bluetoothLEManagers.put(context.getClass().getName(), bluetoothManager);
            return bluetoothManager;
        }
    }

    static void onDestroy(Context context) {
        bluetoothLEManagers.remove(context.getClass().getName());
    }
}
