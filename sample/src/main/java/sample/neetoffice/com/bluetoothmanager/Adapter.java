package sample.neetoffice.com.bluetoothmanager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import library.neetoffice.com.bluetoothmanager.device.IBeaconDevice;
import library.neetoffice.com.bluetoothmanager.util.IBeaconUtils;
import library.neetoffice.com.genericadapter.base.GenericAdapter;
import sample.neetoffice.com.bluetoothmanager.utils.HeartHandRingUtil;
import sample.neetoffice.com.bluetoothmanager.utils.NordicUtil;

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
        tag.library_median_rssi_textView = (TextView) view.findViewById(R.id.library_median_rssi_textView);
        tag.library_average_rssi_textView = (TextView) view.findViewById(R.id.library_average_rssi_textView);
        tag.library_major_textView = (TextView) view.findViewById(R.id.library_major_textView);
        tag.library_minor_textView = (TextView) view.findViewById(R.id.library_minor_textView);
        tag.library_timestamp_textView = (TextView) view.findViewById(R.id.library_timestamp_textView);
        tag.library_calibratedtxpower_textView = (TextView) view.findViewById(R.id.library_calibratedtxpower_textView);
        tag.library_descriptor_textView_m = (TextView) view.findViewById(R.id.library_descriptor_textView_m);
        tag.library_distance_textView_m = (TextView) view.findViewById(R.id.library_distance_textView_m);
        tag.library_accuracy_textView_m = (TextView) view.findViewById(R.id.library_accuracy_textView_m);
        tag.library_descriptor_textView_a = (TextView) view.findViewById(R.id.library_descriptor_textView_a);
        tag.library_distance_textView_a = (TextView) view.findViewById(R.id.library_distance_textView_a);
        tag.library_accuracy_textView_a = (TextView) view.findViewById(R.id.library_accuracy_textView_a);
        return tag;
    }

    @Override
    public void onBind(Tag tag, int i) {
        final BluetoothModel bluetoothModel = getItem(i);
        if (bluetoothModel.bluetoothDevice != null) {
            final BluetoothDevice bluetoothDevice = bluetoothModel.bluetoothDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, bluetoothDevice.getAddress()));
            tag.library_uuid_textView.setText(""+bluetoothDevice.getUuids().length);
            tag.library_median_rssi_textView.setText("");
            tag.library_average_rssi_textView.setText("");
            tag.library_major_textView.setText("");
            tag.library_minor_textView.setText("");
            tag.library_timestamp_textView.setText("");
            tag.library_calibratedtxpower_textView.setText("");
            tag.library_descriptor_textView_m.setText("");
            tag.library_distance_textView_m.setText("");
            tag.library_accuracy_textView_m.setText("");
            tag.library_descriptor_textView_a.setText("");
            tag.library_distance_textView_a.setText("");
            tag.library_accuracy_textView_a.setText("");
        } else if (bluetoothModel.bluetoothLeDevice != null) {
            final BluetoothLeDevice bluetoothLeDevice = bluetoothModel.bluetoothLeDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, bluetoothLeDevice.getDevice().getName(), bluetoothLeDevice.getAddress()));
            if (NordicUtil.isThisANordicBLE(bluetoothLeDevice)) {
                tag.library_uuid_textView.setText("Nordic BLE" + (NordicUtil.isClicking(bluetoothLeDevice) ? " ( clicking )" : ""));
            } else if (HeartHandRingUtil.isThisAHeartHandRing(bluetoothLeDevice)) {
                tag.library_uuid_textView.setText("心率手環");
            } else {
                tag.library_uuid_textView.setText("");
            }
            final int size = bluetoothLeDevice.getRssiLog().size();
            tag.library_median_rssi_textView.setText(getString(R.string.library_median_rssi_text, bluetoothLeDevice.getRunningMedianRssi(), size));
            tag.library_average_rssi_textView.setText(getString(R.string.library_rssi_text, bluetoothLeDevice.getRunningAverageRssi(), size));
            if (HeartHandRingUtil.isThisAHeartHandRing(bluetoothLeDevice)) {
                tag.library_major_textView.setText("心率 = " + HeartHandRingUtil.getHeartRateValue(bluetoothLeDevice));
            } else {
                tag.library_major_textView.setText("");
            }
            tag.library_minor_textView.setText("");
            tag.library_timestamp_textView.setText(getString(R.string.library_timestamp_text, DATEFORMAT.format(new Date(bluetoothLeDevice.getTimestamp()))));
            if (HeartHandRingUtil.isThisAHeartHandRing(bluetoothLeDevice)) {
                tag.library_calibratedtxpower_textView.setText("電量 = " + HeartHandRingUtil.getBattery(bluetoothLeDevice) + "%");
            } else {
                tag.library_calibratedtxpower_textView.setText("");
            }
            tag.library_descriptor_textView_m.setText("");
            tag.library_distance_textView_m.setText("");
            tag.library_accuracy_textView_m.setText("");
            tag.library_descriptor_textView_a.setText("");
            tag.library_distance_textView_a.setText("");
            tag.library_accuracy_textView_a.setText("");
        } else if (bluetoothModel.iBeaconDevice != null) {
            final IBeaconDevice iBeaconDevice = bluetoothModel.iBeaconDevice;
            tag.library_mac_textView.setText(getString(R.string.library_mac_text, iBeaconDevice.getDevice().getName(), iBeaconDevice.getAddress()));
            tag.library_uuid_textView.setText(getString(R.string.library_uuid_text, iBeaconDevice.getUUID()));
            final int size = iBeaconDevice.getRssiLog().size();
            tag.library_median_rssi_textView.setText(getString(R.string.library_median_rssi_text, iBeaconDevice.getRunningMedianRssi(), size));
            tag.library_average_rssi_textView.setText(getString(R.string.library_rssi_text, iBeaconDevice.getRunningAverageRssi(), size));
            tag.library_major_textView.setText(getString(R.string.library_major_text, iBeaconDevice.getMajor()));
            tag.library_minor_textView.setText(getString(R.string.library_minor_text, iBeaconDevice.getMinor()));
            tag.library_timestamp_textView.setText(getString(R.string.library_timestamp_text, DATEFORMAT.format(new Date(iBeaconDevice.getTimestamp()))));
            tag.library_calibratedtxpower_textView.setText(getString(R.string.library_calibratedtxpower_text, iBeaconDevice.getCalibratedTxPower()));
            tag.library_descriptor_textView_m.setText(getString(R.string.library_distance_text, FORMAT.format(IBeaconUtils.calculateDistance(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningMedianRssi(), 5))));
            tag.library_distance_textView_m.setText(getString(R.string.library_descriptor_text, IBeaconUtils.getDistanceDescriptor(IBeaconUtils.calculateAccuracy(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningMedianRssi()))));
            tag.library_accuracy_textView_m.setText(getString(R.string.library_accuracy_text, FORMAT.format(IBeaconUtils.calculateAccuracy(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningMedianRssi()))));
            tag.library_descriptor_textView_a.setText(getString(R.string.library_distance_text, FORMAT.format(IBeaconUtils.calculateDistance(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningAverageRssi(), 5))));
            tag.library_distance_textView_a.setText(getString(R.string.library_descriptor_text, IBeaconUtils.getDistanceDescriptor(IBeaconUtils.calculateAccuracy(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningAverageRssi()))));
            tag.library_accuracy_textView_a.setText(getString(R.string.library_accuracy_text, FORMAT.format(IBeaconUtils.calculateAccuracy(iBeaconDevice.getCalibratedTxPower(), iBeaconDevice.getRunningAverageRssi()))));
        }
    }

    private static final NumberFormat FORMAT = new DecimalFormat("0.00");
    private static final DateFormat DATEFORMAT = new SimpleDateFormat("HH:mm");

    private String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }
}
