package library.neetoffice.com.bluetoothmanager;

import android.content.Context;
import android.os.Build;

import java.util.HashMap;

/**
 * Created by Deo-chainmeans on 2015/10/8.
 */
public abstract class BluetoothLEScanner {
    private static final HashMap<Integer, BluetoothLEManager> bluetoothLEManagers = new HashMap<>();

    public static final BluetoothLEManager getInstance(Context context) {
        if (bluetoothLEManagers.containsKey(context.hashCode())) {
            return bluetoothLEManagers.get(context.hashCode());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForLOLLIPOP(context);
            bluetoothLEManagers.put(context.hashCode(), bluetoothManager);
            return bluetoothManager;
        } else {
            final BluetoothLEManager bluetoothManager = new BluetoothLEManagerForJB2(context);
            bluetoothLEManagers.put(context.hashCode(), bluetoothManager);
            return bluetoothManager;
        }
    }

    static void onDestroy(Context context) {
        bluetoothLEManagers.remove(context.hashCode());
        if (bluetoothLEManagers.isEmpty()) {
            BluetoothLEManagerImpl.map.clear();
        }
    }
}
