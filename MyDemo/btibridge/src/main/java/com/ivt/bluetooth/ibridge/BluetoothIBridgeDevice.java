package com.ivt.bluetooth.ibridge;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Administrator on 2016/10/11.
 */
public class BluetoothIBridgeDevice implements Parcelable {
    static final UUID SPPUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothDevice mDevice;
    private String mDeviceAddress;
    private String mDeviceName;
    private boolean mIsConnected;
    private Direction mDirection;
    private ConnectStatus mConnectStatus;
    private BondStatus mBondStatus;
    private int mDeviceClass;
    private static boolean tvi = true;
    static final String EXTRA_PAIRING_VARIANT = BluetoothDevice.EXTRA_PAIRING_VARIANT;
    static final String EXTRA_PAIRING_KEY = BluetoothDevice.EXTRA_PAIRING_KEY;
    static final String ACTION_PAIRING_REQUEST = BluetoothDevice.ACTION_PAIRING_REQUEST;
    static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";
    static final int PAIRING_VARIANT_PIN = 0;
    static final int PAIRING_VARIANT_PASSKEY = 1;
    static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    static final int PAIRING_VARIANT_CONSENT = 3;
    static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
    static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
    static final int PAIRING_VARIANT_OOB_CONSENT = 6;
    byte[] buffer;
    int length;

    public static final Creator<BluetoothIBridgeDevice> CREATOR = new Creator<BluetoothIBridgeDevice>() {
        @Override
        public BluetoothIBridgeDevice createFromParcel(Parcel source) {
            return new BluetoothIBridgeDevice(source);
        }

        @Override
        public BluetoothIBridgeDevice[] newArray(int size) {
            return new BluetoothIBridgeDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDeviceName);
        dest.writeString(this.mDeviceAddress);
        dest.writeInt(this.mDeviceClass);
        dest.writeByte(this.mIsConnected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mDirection == null ? -1 : this.mDirection.ordinal());
        dest.writeInt(this.mConnectStatus == null ? -1 : this.mConnectStatus.ordinal());
        dest.writeInt(this.mBondStatus == null ? -1 : this.mBondStatus.ordinal());
    }

    protected BluetoothIBridgeDevice(Parcel in) {
        this.mDeviceName = in.readString();
        this.mDeviceAddress = in.readString();
        this.mDeviceClass = in.readInt();
        this.mIsConnected = in.readByte() != 0;
        int tmpMDirection = in.readInt();
        this.mDirection = tmpMDirection == -1 ? null : Direction.values()[tmpMDirection];
        int tmpMConnectStatus = in.readInt();
        this.mConnectStatus = tmpMConnectStatus == -1 ? null : ConnectStatus.values()[tmpMConnectStatus];
        int tmpMBondStatus = in.readInt();
        this.mBondStatus = tmpMBondStatus == -1 ? null : BondStatus.values()[tmpMBondStatus];
    }

    /** @deprecated */
    @Deprecated
    public BluetoothIBridgeDevice(String address) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        mDeviceAddress = address;
        mDevice = adapter.getRemoteDevice(address);
        mDeviceName = mDevice.getName();
        BluetoothClass bluetoothClass = mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            mDeviceClass = mDevice.getBluetoothClass().getDeviceClass();
        } else {
            mDeviceClass = -1;
        }
    }

    public BluetoothIBridgeDevice(BluetoothDevice device) {
        this.mDevice = device;
        mDeviceAddress = device.getAddress();
        mDeviceName = device.getName();
        BluetoothClass bluetoothClass = null;
        try {
            bluetoothClass = mDevice.getBluetoothClass();
        } catch (Exception e) {
        }
        if (bluetoothClass != null) {
            mDeviceClass = mDevice.getBluetoothClass().getDeviceClass();
        } else {
            mDeviceClass = -1;
        }
    }

    /**
     * 对象
     * @param address       设备地址
     */
    public static BluetoothIBridgeDevice createBluetoothIBirdgeDevice(String address) {
        BluetoothIBridgeDeviceFactory factory = BluetoothIBridgeDeviceFactory.getDefaultFactory();
        return factory.createDevice(address);
    }

    /**
     * 获取设备名称
     */
    public String getDeviceName() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = adapter.getRemoteDevice(mDeviceAddress);
        mDeviceName = mDevice.getName();
        return mDeviceName;
    }

    /**
     * 获取设备地址
     */
    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    private int getDeviceClass() {
        return mDeviceClass;
    }

    /**
     * 是否是通过验证的设备
     */
    boolean isValidDevice() {
        return tvi ? true : getDeviceAddress().startsWith("00:15:83:") || this.getDeviceAddress().startsWith("00:13:8A:");
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof BluetoothIBridgeDevice)) {
            return false;
        } else {
            String addr = mDeviceAddress == null ? "00:00:00:00:00:00" : mDeviceAddress;
            BluetoothIBridgeDevice dev = (BluetoothIBridgeDevice) o;
            String another = dev.mDeviceAddress == null ? "00:00:00:00:00:00" : dev.mDeviceAddress;
            return addr.equals(another);
        }
    }

    @Override
    public String toString() {
        String name = mDeviceName == null ? "Device" : mDeviceName;
        String addr = mDeviceAddress == null ? "00:00:00:00:00:00" : mDeviceAddress;
        return super.toString() + " [" + name + " - " + addr + "]";
    }

    /**
     *
     * @return
     */
    BluetoothSocket createSocket() {
        BluetoothSocket socket = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1 && !SystemPropertiesProxy.isMediatekPlatform()) {
            Class e = BluetoothDevice.class;
            Method m = null;
            try {
                m = e.getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }

            if (m != null) {
                try {
                    socket = (BluetoothSocket) m.invoke(mDevice, new Object[]{SPPUUID});
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                socket = mDevice.createRfcommSocketToServiceRecord(SPPUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    BluetoothSocket createSocketWithChannel(int channel) {
        BluetoothSocket socket = null;
        Class cls = BluetoothDevice.class;
        Method m = null;

        try {
            m = cls.getMethod("createRfcommSocket", new Class[]{Integer.TYPE});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (m != null) {
            try {
                socket = (BluetoothSocket) m.invoke(mDevice, new Object[]{Integer.valueOf(channel)});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }

    /**
     * 设置设备连接状态
     */
    void connected(boolean connected) {
        mIsConnected = connected;
    }

    /**
     * 获取设备连接状态
     */
    public boolean isConnected() {
        return mIsConnected;
    }

    void setConnectionDirection(Direction d) {
        mDirection = d;
    }

    Direction connectionDirection() {
        return mDirection;
    }

    void setConnectstatus(ConnectStatus d) {
        mConnectStatus = d;
    }

    public ConnectStatus getConnectStatus() {
        return mConnectStatus;
    }

    /**
     * 设置绑定状态
     */
    void setBondStatus() {
        if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            mBondStatus = BondStatus.STATE_BONDED;
        }
        if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
            mBondStatus = BondStatus.STATE_BONDING;
        }
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
            mBondStatus = BondStatus.STATE_BONDNONE;
        }
    }

    void setBondStatus(BondStatus d) {
        mBondStatus = d;
    }

    /**
     * 获取绑定状态
     */
    public BondStatus getBondStatus() {
        return mBondStatus;
    }

    /**
     * 绑定设备
     */
    public void craeteBond() {
        try {
            mDevice.getClass().getMethod("createBond", (Class[]) null).invoke(this.mDevice, new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void createBond(BluetoothDevice device) {
        try {
            BluetoothDevice.class.getMethod("createBond", (Class[]) null).invoke(device, new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }





    public enum BondStatus {
        STATE_BONDED,
        STATE_BONDING,
        STATE_BONDNONE,
        STATE_BONDFAILED,
        STATE_BOND_OVERTIME,
        STATE_BOND_CANCLED;
    }

    public enum ConnectStatus {
        STATUS_DISCONNECTED,
        STATUS_CONNECTED,
        STATUS_DISCONNECTTING,
        STATUS_CONNECTTING,
        STATUS_CONNECTFAILED,
        STATE_BONDED,
        STATE_BONDING,
        STATE_BONDNONE;
    }

    public enum Direction {
        DIRECTION_NONE,
        DIRECTION_FORWARD,
        DIRECTION_BACKWARD;
    }


}
