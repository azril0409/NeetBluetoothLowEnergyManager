package sample.neetoffice.com.bluetoothmanager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import library.neetoffice.com.genericadapter.base.GenericAdapter;

/**
 * Created by Deo on 2016/4/27.
 */
public class Adapter extends GenericAdapter<BluetoothModel, Tag> {
    public Adapter(Context context) {
        super(context, new ArrayList<BluetoothModel>(), R.layout.cell_bluetoothmodel);
    }

    @Override
    public Tag onCreateTag(View view) {
        final Tag tag = new Tag();
        tag.textView = (TextView) view.findViewById(R.id.textView);
        return tag;
    }

    @Override
    public void onBind(Tag tag, int i) {
        final BluetoothModel bluetoothModel = getItem(i);
        if (bluetoothModel.bluetoothDevice != null) {

        } else if (bluetoothModel.bluetoothLeDevice != null) {

        } else if (bluetoothModel.iBeaconDevice != null) {

        }
    }
}
