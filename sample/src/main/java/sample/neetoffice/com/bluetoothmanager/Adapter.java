package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;
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
        tag.library_mac_textView = (TextView) view.findViewById(R.id.library_mac_textView);
        tag.library_uuid_textView = (TextView) view.findViewById(R.id.library_uuid_textView);
        tag.library_rssi_textView = (TextView) view.findViewById(R.id.library_rssi_textView);
        tag.library_major_textView = (TextView) view.findViewById(R.id.library_major_textView);
        tag.library_minor_textView = (TextView) view.findViewById(R.id.library_minor_textView);
        tag.library_timestamp_textView = (TextView) view.findViewById(R.id.library_timestamp_textView);
        tag.library_calibratedtxpower_textView = (TextView) view.findViewById(R.id.library_calibratedtxpower_textView);
        tag.library_descriptor_textView = (TextView) view.findViewById(R.id.library_descriptor_textView);
        tag.library_distance_textView = (TextView) view.findViewById(R.id.library_distance_textView);
        return tag;
    }

    @Override
    public void onBind(Tag tag, int i) {
        final BluetoothModel bluetoothModel = getItem(i);
        if (bluetoothModel.bluetoothDevice != null) {
            final BluetoothDevice bluetoothDevice = bluetoothModel.bluetoothDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, bluetoothDevice.getAddress()));
            tag.library_uuid_textView.setText("");
            tag.library_rssi_textView.setText("");
            tag.library_major_textView.setText("");
            tag.library_minor_textView.setText("");
            tag.library_timestamp_textView.setText("");
            tag.library_calibratedtxpower_textView.setText("");
            tag.library_descriptor_textView.setText("");
            tag.library_distance_textView.setText("");
        } else if (bluetoothModel.bluetoothLeDevice != null) {
            final BluetoothLeDevice bluetoothLeDevice = bluetoothModel.bluetoothLeDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, bluetoothLeDevice.getAddress()));
            tag.library_uuid_textView.setText("");
            tag.library_rssi_textView.setText(getString(R.string.library_rssi_text, bluetoothLeDevice.getRunningMedianRssi()));
            tag.library_major_textView.setText("");
            tag.library_minor_textView.setText("");
            tag.library_timestamp_textView.setText(getString(R.string.library_timestamp_text, bluetoothLeDevice.getTimestamp()));
            tag.library_calibratedtxpower_textView.setText("");
            tag.library_descriptor_textView.setText("");
            tag.library_distance_textView.setText("");
        } else if (bluetoothModel.iBeaconDevice != null) {
            final IBeaconDevice iBeaconDevice = bluetoothModel.iBeaconDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, iBeaconDevice.getAddress()));
            tag.library_uuid_textView.setText(getString(R.string.library_uuid_text, iBeaconDevice.getUUID()));
            tag.library_rssi_textView.setText(getString(R.string.library_rssi_text, iBeaconDevice.getRunningMedianRssi()));
            tag.library_major_textView.setText(getString(R.string.library_major_text, iBeaconDevice.getMajor()));
            tag.library_minor_textView.setText(getString(R.string.library_minor_text, iBeaconDevice.getMinor()));
            tag.library_timestamp_textView.setText(getString(R.string.library_timestamp_text, iBeaconDevice.getTimestamp()));
            tag.library_calibratedtxpower_textView.setText(getString(R.string.library_calibratedtxpower_text, iBeaconDevice.getCalibratedTxPower()));
            tag.library_descriptor_textView.setText(getString(R.string.library_descriptor_text, IBeaconUtils.calculateAccuracy(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningMedianRssi())));
            tag.library_distance_textView.setText(getString(R.string.library_distance_text, iBeaconDevice.getDistanceDescriptor()));
        }
    }

    private String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }
}
