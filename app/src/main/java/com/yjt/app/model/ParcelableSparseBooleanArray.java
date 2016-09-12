package com.yjt.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

public class ParcelableSparseBooleanArray extends SparseBooleanArray implements Parcelable {

    public ParcelableSparseBooleanArray() {

    }

    public ParcelableSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            this.put(sparseBooleanArray.keyAt(i), sparseBooleanArray.valueAt(i));
        }
    }

    protected ParcelableSparseBooleanArray(Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int[] keys = new int[size()];
        boolean[] values = new boolean[size()];
        for (int i = 0; i < size(); i++) {
            keys[i] = keyAt(i);
            values[i] = valueAt(i);
        }
        dest.writeInt(size());
        dest.writeIntArray(keys);
        dest.writeBooleanArray(values);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableSparseBooleanArray> CREATOR = new Creator<ParcelableSparseBooleanArray>() {
        @Override
        public ParcelableSparseBooleanArray createFromParcel(Parcel in) {
            ParcelableSparseBooleanArray array = new ParcelableSparseBooleanArray();
            int size = in.readInt();
            int[] keys = new int[size];
            boolean[] values = new boolean[size];
            in.readIntArray(keys);
            in.readBooleanArray(values);
            for (int i = 0; i < size; i++) {
                array.put(keys[i], values[i]);
            }
            return array;
        }

        @Override
        public ParcelableSparseBooleanArray[] newArray(int size) {
            return new ParcelableSparseBooleanArray[size];
        }
    };
}
