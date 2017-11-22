package library.chainmeans.com.scanner.support.ibeacon;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import library.neetoffice.com.bluetoothmanager.device.BluetoothLeDevice;
import uk.co.alt236.bluetoothlelib.device.adrecord.AdRecord;
import uk.co.alt236.bluetoothlelib.util.ByteUtils;

/**
 * Created by Deo-chainmeans on 2017/11/20.
 */

class IBeaconManufacturerData implements Parcelable {
    final int calibratedTxPower;
    final int companyIdentidier;
    final int iBeaconAdvertisment;
    final int major;
    final int minor;
    final String mUUID;

    IBeaconManufacturerData(final BluetoothLeDevice device) {
        this(device.getAdRecordStore().getRecord(AdRecord.TYPE_MANUFACTURER_SPECIFIC_DATA).getData());
    }

    IBeaconManufacturerData(final byte[] manufacturerData) {
        final byte[] intArray = Arrays.copyOfRange(manufacturerData, 0, 2);
        ByteUtils.invertArray(intArray);
        companyIdentidier = ByteUtils.getIntFrom2ByteArray(intArray);
        iBeaconAdvertisment = ByteUtils.getIntFrom2ByteArray(Arrays.copyOfRange(manufacturerData, 2, 4));
        mUUID = IBeaconUtil.calculateUuidString(Arrays.copyOfRange(manufacturerData, 4, 20));
        major = ByteUtils.getIntFrom2ByteArray(Arrays.copyOfRange(manufacturerData, 20, 22));
        minor = ByteUtils.getIntFrom2ByteArray(Arrays.copyOfRange(manufacturerData, 22, 24));
        calibratedTxPower = manufacturerData[24];
    }

    protected IBeaconManufacturerData(Parcel in) {
        calibratedTxPower = in.readInt();
        companyIdentidier = in.readInt();
        iBeaconAdvertisment = in.readInt();
        major = in.readInt();
        minor = in.readInt();
        mUUID = in.readString();
    }

    public static final Creator<IBeaconManufacturerData> CREATOR = new Creator<IBeaconManufacturerData>() {
        @Override
        public IBeaconManufacturerData createFromParcel(Parcel in) {
            return new IBeaconManufacturerData(in);
        }

        @Override
        public IBeaconManufacturerData[] newArray(int size) {
            return new IBeaconManufacturerData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(calibratedTxPower);
        dest.writeInt(companyIdentidier);
        dest.writeInt(iBeaconAdvertisment);
        dest.writeInt(major);
        dest.writeInt(minor);
        dest.writeString(mUUID);
    }
}
