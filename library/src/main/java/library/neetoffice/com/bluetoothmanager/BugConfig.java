package library.neetoffice.com.bluetoothmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by Deo-chainmeans on 2015/10/12.
 */
public class BugConfig {

    public static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
    }
}
