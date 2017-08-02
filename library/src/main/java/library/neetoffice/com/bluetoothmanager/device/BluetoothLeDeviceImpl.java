package library.neetoffice.com.bluetoothmanager.device;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import library.neetoffice.com.bluetoothmanager.device.adrecord.AdRecordStore;
import library.neetoffice.com.bluetoothmanager.resolvers.BluetoothClassResolver;
import library.neetoffice.com.bluetoothmanager.util.AdRecordUtils;
import library.neetoffice.com.bluetoothmanager.util.ByteUtils;
import library.neetoffice.com.bluetoothmanager.util.LimitedLinkHashMap;

/**
 * Created by Deo-chainmeans on 2016/9/10.
 */
public class BluetoothLeDeviceImpl implements BluetoothLeDevice {
    static final int MAX_RSSI_LOG_SIZE = 5;
    public static final Creator<BluetoothLeDeviceImpl> CREATOR = new Creator<BluetoothLeDeviceImpl>() {
        public BluetoothLeDeviceImpl createFromParcel(Parcel in) {
            return new BluetoothLeDeviceImpl(in);
        }

        public BluetoothLeDeviceImpl[] newArray(int size) {
            return new BluetoothLeDeviceImpl[size];
        }
    };
    public static final long LOG_INVALIDATION_THRESHOLD = 10 * 1000;
    private static final String PARCEL_EXTRA_BLUETOOTH_DEVICE = "bluetooth_device";
    private static final String PARCEL_EXTRA_CURRENT_RSSI = "current_rssi";
    private static final String PARCEL_EXTRA_CURRENT_TIMESTAMP = "current_timestamp";
    private static final String PARCEL_EXTRA_DEVICE_RSSI_LOG = "device_rssi_log";
    private static final String PARCEL_EXTRA_DEVICE_SCANRECORD = "device_scanrecord";
    private static final String PARCEL_EXTRA_DEVICE_SCANRECORD_STORE = "device_scanrecord_store";
    private static final String PARCEL_EXTRA_FIRST_RSSI = "device_first_rssi";
    private static final String PARCEL_EXTRA_FIRST_TIMESTAMP = "first_timestamp";
    private final AdRecordStore mRecordStore;
    private final BluetoothDevice mDevice;
    private final Map<Long, Integer> mRssiLog;
    private final int mFirstRssi;//最初Rssi
    private final long mFirstTimestamp;//最初Rssi Time
    private byte[] mScanRecord;
    private int mCurrentRssi;//最新Rssi
    private long mCurrentTimestamp;//最新Rssi Time

    /**
     * Instantiates a new Bluetooth LE device.
     *
     * @param device     a standard android Bluetooth device
     * @param rssi       the RSSI value of the Bluetooth device
     * @param scanRecord the scan record of the device
     * @param timestamp  the timestamp of the RSSI reading
     */
    public BluetoothLeDeviceImpl(BluetoothDevice device, int rssi, byte[] scanRecord, long timestamp) {
        mDevice = device;
        mFirstRssi = rssi;
        mFirstTimestamp = timestamp;
        mRecordStore = new AdRecordStore(AdRecordUtils.parseScanRecordAsSparseArray(scanRecord));
        mScanRecord = scanRecord;
        mRssiLog = new LimitedLinkHashMap<Long, Integer>(MAX_RSSI_LOG_SIZE);
        updateRssiReading(timestamp, rssi);
    }

    /**
     * Instantiates a new Bluetooth LE device.
     *
     * @param device the device
     */
    public BluetoothLeDeviceImpl(BluetoothLeDevice device) {
        mCurrentRssi = device.getRssi();
        mCurrentTimestamp = device.getTimestamp();
        mDevice = device.getDevice();
        mFirstRssi = device.getFirstRssi();
        mFirstTimestamp = device.getFirstTimestamp();
        mRecordStore = new AdRecordStore(AdRecordUtils.parseScanRecordAsSparseArray(device.getScanRecord()));
        mRssiLog = device.getRssiLog();
        mScanRecord = device.getScanRecord();
    }

    /**
     * Instantiates a new bluetooth le device.
     *
     * @param in the in
     */
    @SuppressWarnings("unchecked")
    protected BluetoothLeDeviceImpl(Parcel in) {
        final Bundle b = in.readBundle(getClass().getClassLoader());
        mCurrentRssi = b.getInt(PARCEL_EXTRA_CURRENT_RSSI, 0);
        mCurrentTimestamp = b.getLong(PARCEL_EXTRA_CURRENT_TIMESTAMP, 0);
        mDevice = b.getParcelable(PARCEL_EXTRA_BLUETOOTH_DEVICE);
        mFirstRssi = b.getInt(PARCEL_EXTRA_FIRST_RSSI, 0);
        mFirstTimestamp = b.getLong(PARCEL_EXTRA_FIRST_TIMESTAMP, 0);
        mRecordStore = b.getParcelable(PARCEL_EXTRA_DEVICE_SCANRECORD_STORE);
        mRssiLog = (Map<Long, Integer>) b.getSerializable(PARCEL_EXTRA_DEVICE_RSSI_LOG);
        mScanRecord = b.getByteArray(PARCEL_EXTRA_DEVICE_SCANRECORD);
    }

    /**
     * Resolve bonding state.
     *
     * @param bondState the bond state
     * @return the string
     */
    private static String resolveBondingState(int bondState) {
        switch (bondState) {
            case BluetoothDevice.BOND_BONDED:
                return "Paired";
            case BluetoothDevice.BOND_BONDING:
                return "Pairing";
            case BluetoothDevice.BOND_NONE:
                return "Unbonded";
            default:
                return "Unknown";
        }
    }

    /**
     * Adds the to rssi log.
     *
     * @param timestamp   the timestamp
     * @param rssiReading the rssi reading
     */
    private void addToRssiLog(long timestamp, int rssiReading) {
        synchronized (mRssiLog) {
            mCurrentRssi = rssiReading;
            mCurrentTimestamp = timestamp;
            mRssiLog.put(timestamp, rssiReading);
        }
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BluetoothLeDeviceImpl other = (BluetoothLeDeviceImpl) obj;
        if (mCurrentRssi != other.mCurrentRssi)
            return false;
        if (mCurrentTimestamp != other.mCurrentTimestamp)
            return false;
        if (mDevice == null) {
            if (other.mDevice != null)
                return false;
        } else if (!mDevice.equals(other.mDevice))
            return false;
        if (mFirstRssi != other.mFirstRssi)
            return false;
        if (mFirstTimestamp != other.mFirstTimestamp)
            return false;
        if (mRecordStore == null) {
            if (other.mRecordStore != null)
                return false;
        } else if (!mRecordStore.equals(other.mRecordStore))
            return false;
        if (mRssiLog == null) {
            if (other.mRssiLog != null)
                return false;
        } else if (!mRssiLog.equals(other.mRssiLog))
            return false;
        if (!Arrays.equals(mScanRecord, other.mScanRecord))
            return false;
        return true;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return mDevice.getName();
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    @Override
    public String getAddress() {
        return mDevice.getAddress();
    }

    /**
     * Gets the ad record store.
     *
     * @return the ad record store
     */
    public AdRecordStore getAdRecordStore() {
        return mRecordStore;
    }

    /**
     * Gets the bluetooth device bond state.
     *
     * @return the bluetooth device bond state
     */
    public String getBluetoothDeviceBondState() {
        return resolveBondingState(mDevice.getBondState());
    }

    /**
     * Gets the bluetooth device class name.
     *
     * @return the bluetooth device class name
     */
    public String getBluetoothDeviceClassName() {
        return BluetoothClassResolver.resolveDeviceClass(mDevice.getBluetoothClass().getDeviceClass());
    }

    /**
     * Gets the device.
     *
     * @return the device
     */
    public BluetoothDevice getDevice() {
        return mDevice;
    }

    /**
     * Gets the first rssi.
     *
     * @return the first rssi
     */
    public int getFirstRssi() {
        return mFirstRssi;
    }

    /**
     * Gets the first timestamp.
     *
     * @return the first timestamp
     */
    public long getFirstTimestamp() {
        return mFirstTimestamp;
    }

    /**
     * Gets the rssi.
     *
     * @return the rssi
     */
    public int getRssi() {
        return mCurrentRssi;
    }

    /**
     * Gets the rssi log.
     *
     * @return the rssi log
     */
    public Map<Long, Integer> getRssiLog() {
        synchronized (mRssiLog) {
            return mRssiLog;
        }
    }

    /**
     * Gets the running average rssi.
     *
     * @return the running average rssi
     */
    public double getRunningAverageRssi() {
        int sum = 0;
        int count = 0;
        synchronized (mRssiLog) {
            for (final Map.Entry<Long, Integer> e : mRssiLog.entrySet()) {
                if (mCurrentTimestamp - e.getKey() > LOG_INVALIDATION_THRESHOLD) {
                    continue;
                }
                count++;
                sum += e.getValue();
            }
        }
        if (count > 0) {
            return sum / count;
        } else {
            return 0;
        }

    }


    /**
     * Gets the running median rssi.
     *
     * @return the running median rssi
     */
    public double getRunningMedianRssi() {
        final ArrayList<Integer> values = new ArrayList<>();
        synchronized (mRssiLog) {
            for (final Map.Entry<Long, Integer> e : mRssiLog.entrySet()) {
                if (mCurrentTimestamp - e.getKey() > LOG_INVALIDATION_THRESHOLD) {
                    continue;
                }
                values.add(e.getValue());
            }
        }
        if (values.size() > 0) {
            final Integer[] array = values.toArray(new Integer[values.size()]);
            Arrays.sort(array, new Comparator<Integer>() {
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return lhs - rhs;
                }
            });
            if (array.length % 2 == 0) {
                return array[array.length / 2 - 1] / 2 + array[array.length / 2] / 2;
            } else {
                return array[(array.length - 1) / 2];
            }
        } else {
            return 0;
        }
    }

    /**
     * Gets the scan record.
     *
     * @return the scan record
     */
    public byte[] getScanRecord() {
        return mScanRecord;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return mCurrentTimestamp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mCurrentRssi;
        result = prime * result + (int) (mCurrentTimestamp ^ (mCurrentTimestamp >>> 32));
        result = prime * result + ((mDevice == null) ? 0 : mDevice.hashCode());
        result = prime * result + mFirstRssi;
        result = prime * result + (int) (mFirstTimestamp ^ (mFirstTimestamp >>> 32));
        result = prime * result + ((mRecordStore == null) ? 0 : mRecordStore.hashCode());
        result = prime * result + ((mRssiLog == null) ? 0 : mRssiLog.hashCode());
        result = prime * result + Arrays.hashCode(mScanRecord);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BluetoothLeDevice [mDevice=" + mDevice + ", mRssi=" + mFirstRssi + ", mScanRecord=" + ByteUtils.byteArrayToHexString(mScanRecord) + ", mRecordStore=" + mRecordStore + ", getBluetoothDeviceBondState()=" + getBluetoothDeviceBondState() + ", getBluetoothDeviceClassName()=" + getBluetoothDeviceClassName() + "]";
    }

    /**
     * Update rssi reading.
     *
     * @param timestamp   the timestamp
     * @param rssiReading the rssi reading
     */
    public void updateRssiReading(long timestamp, int rssiReading) {
        addToRssiLog(timestamp, rssiReading);
    }

    public void updateScanRecord(byte[] scanRecord) {
        mScanRecord = scanRecord;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
        final Bundle b = new Bundle(getClass().getClassLoader());
        b.putByteArray(PARCEL_EXTRA_DEVICE_SCANRECORD, mScanRecord);
        b.putInt(PARCEL_EXTRA_FIRST_RSSI, mFirstRssi);
        b.putInt(PARCEL_EXTRA_CURRENT_RSSI, mCurrentRssi);
        b.putLong(PARCEL_EXTRA_FIRST_TIMESTAMP, mFirstTimestamp);
        b.putLong(PARCEL_EXTRA_CURRENT_TIMESTAMP, mCurrentTimestamp);
        b.putParcelable(PARCEL_EXTRA_BLUETOOTH_DEVICE, mDevice);
        b.putParcelable(PARCEL_EXTRA_DEVICE_SCANRECORD_STORE, mRecordStore);
        b.putSerializable(PARCEL_EXTRA_DEVICE_RSSI_LOG, (Serializable) mRssiLog);
        parcel.writeBundle(b);
    }
}
