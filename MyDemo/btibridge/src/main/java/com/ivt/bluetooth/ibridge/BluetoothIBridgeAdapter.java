package com.ivt.bluetooth.ibridge;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class BluetoothIBridgeAdapter {
    private static final String TAG = BluetoothIBridgeAdapter.class.getSimpleName();

    public static final String CONNECT_TYPE_DEFAULT = "ivt.device.default";
    public static final String CONNECT_TYPE_I482E = "ivt.device.i482e";
    static final String LAST_CONNECTED_DEVICE = "last_connected_device";
    static final String LAST_CONNECTED_DEVICE_NAME = "last_connected_device_name";
    static final String LAST_CONNECTED_DEVICE_ADDRESS = "last_connected_device_address";
    static final boolean D = true;
    static final int MESSAGE_DEVICE_CONNECTED = 1;
    static final int MESSAGE_DEVICE_DISCONNECTED = 2;
    static final int MESSAGE_DEVICE_CONNECT_FAILED = 4;
    static final int MESSAGE_DEVICE_FOUND = 8;
    static final int MESSAGE_DISCOVERY_FINISHED = 16;
    static final int MESSAGE_WRITE_FAILED = 32;
    static final String VERSION_CODE = "iBridge_version1.2.0_2015.01.21";
    private BluetoothAdapter mAdapter;
    private static BluetoothIBridgeAdapter sAdapter;
    private boolean mDiscoveryOnlyBonded;
    private Context mContext;
    private boolean isBtEnable = false;
    private boolean isAutoWritePincode = false;
    private BluetoothIBridgeConnManager mConnManager = null;
    private ArrayList<BluetoothIBridgeAdapter.EventReceiver> mEventReceivers;
    LocationManager mLocationManager;
    double mLatitude = 0.0D;
    double mLongitude = 0.0D;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String exceptionMessage = null;
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                exceptionMessage = bundle.getString("exception");
            }
            Log.i(TAG, "broadcast message:" + action.toString());
            BluetoothDevice dev;
            //发现设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!dev.getAddress().endsWith("83:15:00") && !dev.getAddress().startsWith("C0:15:83")) {
                    onEventReceived(MESSAGE_DEVICE_FOUND, BluetoothIBridgeDeviceFactory.getDefaultFactory().createDevice(dev), exceptionMessage);
                }
            }

            //搜索设备结束
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                onEventReceived(MESSAGE_DISCOVERY_FINISHED, null, exceptionMessage);
            }

            //蓝牙状态改变
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                    BluetoothIBridgeAdapter.this.isBtEnable = true;
                    BluetoothIBridgeAdapter.this.mConnManager.start();
                }

                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                    BluetoothIBridgeAdapter.this.isBtEnable = false;
                    if (BluetoothIBridgeAdapter.this.mConnManager != null) {
                        BluetoothIBridgeAdapter.this.mConnManager.stop();
                    }
                }
            }

            //蓝牙绑定状态改变
            BluetoothIBridgeDevice device;
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                device = BluetoothIBridgeDeviceFactory.getDefaultFactory().createDevice(dev);
                if (device != null) {
                    device.setBondStatus();
                }
            }

            //蓝牙请求配对
            if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                device = BluetoothIBridgeDeviceFactory.getDefaultFactory().createDevice(dev);
                int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, Integer.MIN_VALUE);
                int pairingKey = 0;
                if (type == 2 || type == 4 || type == 5) {
                    pairingKey = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_KEY, Integer.MIN_VALUE);
                }

                if (BluetoothIBridgeAdapter.this.isAutoWritePincode) {
                    BluetoothIBridgeAdapter.this.mConnManager.onPairingRequested(device, type, pairingKey);
                }
            }
        }
    };

    private Location location;

    /**
     * 添加事件监听者
     * @param receiver
     */
    public void registerEventRecevier(EventReceiver receiver) {
        Log.i(TAG, "registerEventReceiver " + receiver + "...");
        if (receiver == null) {
            Log.e(TAG, "receiver is null");
        }

        if (mEventReceivers == null) {
            mEventReceivers = new ArrayList<>();
        }

        if (!mEventReceivers.contains(receiver)) {
            mEventReceivers.add(receiver);
        }
        Log.i(TAG, "registerEventReceiver");
    }

    /**
     * 移除事件监听者
     * @param receiver
     */
    public void unregisterEventReceiver(EventReceiver receiver) {
        if (mEventReceivers == null) {
            Log.e(TAG, "no recevier list");
        } else {
            this.mEventReceivers.remove(receiver);
            Log.i(TAG, "unregisterEventReceiver");
        }
    }

    /**
     * 注册数据接收者
     * @param receiver
     */
    public void registerDataReceiver(DataReceiver receiver) {
        Log.i(TAG, "registerDataReciver " + receiver + "...");
        if (receiver == null) {
            Log.e(TAG, "receiver is null");
        }
        if (mConnManager != null) {
            mConnManager.registerDataReceiver(receiver);
        } else {
            Log.e(TAG, "connection manager is null");
        }
        Log.i(TAG, "registerDataReceiver");
    }

    /**
     * 注销数据接收者
     * @param receiver
     */
    public void unregisterDataReceiver(DataReceiver receiver) {
        Log.i(TAG, "unregisterDataReceiver " + receiver + "...");
        if (mConnManager != null) {
            mConnManager.unregisterDataReceiver(receiver);
        } else {
            Log.e(TAG, "connection manager is null");
        }
        Log.i(TAG, "unregisterDataReceiver");
    }

    /**
     * 开始搜索设备
     * @param onlyBonded    仅搜索绑定设备
     */
    public void startDiscovery(boolean onlyBonded) {
        Log.i(TAG, "startDiscovery...");
        if (isEnabled()) {
            mDiscoveryOnlyBonded = onlyBonded;
            if (mAdapter.isDiscovering()) { //若蓝牙正在搜索设备，则停止搜索
                Log.i(TAG, "stop previous discovering");
                mAdapter.cancelDiscovery();
            }

            if (onlyBonded) {
                Log.i(TAG, "startDiscovery only bonded");
            } else {
                Log.i(TAG, "startDiscovery");
            }
            mAdapter.startDiscovery();
        } else {
            Log.e(TAG, "bluetooth is not enabled");
        }
        Log.i(TAG, "startDiscovery");
    }

    public void startDiscovery() {
        startDiscovery(false);
    }

    /**
     * 停止搜索设备
     */
    public void stopDiscovery() {
        Log.i(TAG, "stopDiscovery ...");
        if (isEnabled()) {
            mAdapter.cancelDiscovery();
        } else {
            Log.e(TAG, "bluetooth is not enabled");
        }
        Log.i(TAG, "stopDiscovery");
    }


    /**
     * 蓝牙是否打开
     */
    public boolean isEnabled() {
        if (mAdapter != null) {
            isBtEnable = mAdapter.isEnabled();
        }
        return isBtEnable;
    }

    public void setEnabled(boolean enabled) {
        Log.i(TAG, "setEnabled to " + enabled + "...");
        if (isEnabled() == enabled) {
            Log.i(TAG, "bluetooth already enabled");
        } else {
            if (mAdapter == null) {
                Log.e(TAG, "bluetooth adapter is null");
            }
            if (enabled) {
                Log.i(TAG, "enable bluetooth");
                mAdapter.enable();
            } else {
                Log.i(TAG, "disable bluetooth");
                mAdapter.disable();
            }
            Log.i(TAG, "setEnabled");
        }
    }

    public BluetoothIBridgeAdapter(Context context) {
        this.mContext = context;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mConnManager = new BluetoothIBridgeConnManager(new MyHandler(this));
        if (this.isEnabled()) {
            mConnManager.start();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        mContext.registerReceiver(mReceiver, filter);
        if (sAdapter != null) {
            sAdapter.clean();
        }

        sAdapter = this;
    }

    /**
     * 从服务器获取消息
     * @param name
     * @return
     */
    private String getInformationFromServer(String name) {
        String information = null;
        try {
            String e = "http://122.115.50.191/Message.aspx?type=2&name=" + URLEncoder.encode(name, "UTF-8");
            URL url = new URL(e);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            information = bufferedReader.readLine();
            if (information.equals("0")) {
                information = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return information;
    }

    /**
     * 保存信息到服务器
     */
    private boolean saveInformationToServer(String name, String information) {
        boolean result = false;

        try {
            String e = "http://122.115.50.191/Message.aspx?type=1&name=" + URLEncoder.encode(name, "UTF-8") + "&" + "location=" + URLEncoder.encode(information, "UTF-8");
            URL url = new URL(e);

            URLConnection e1 = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) e1;
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            information = bufferedReader.readLine();
            if (information.equals("OK")) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 启动定位
     */
    private void locationManagerStart() {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    BluetoothIBridgeAdapter.this.mLatitude = location.getLatitude();
                    BluetoothIBridgeAdapter.this.mLongitude = location.getLongitude();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        LocationManager locationManager = mLocationManager;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000L, 0.0F, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000L, 0.0F, locationListener);
    }

    /**
     * 获取位置信息
     * @return
     */
    private String getInformation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (mLatitude == 0 && mLongitude == 0) {
            try {
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Location e = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (e != null) {
                        mLatitude = e.getLatitude();
                        mLongitude = e.getLongitude();
                    }
                }
            } catch (Exception e) {
            }
        }

        if (mLatitude == 0 && mLongitude == 0) {
            try {
                if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                    }
                }
            } catch (Exception e) {
            }
        }

        return mLatitude + "/" + mLongitude;
    }

    /**
     * 获取已连接的设备
     * @return
     */
    public List<BluetoothIBridgeDevice> getCurrentConnectedDevice() {
        Log.i(TAG, "getCurrentConnectedDevice...");
        List devicesList = null;
        devicesList = mConnManager.getCurrentConnectedDevice();
        Log.i(TAG, devicesList.size() + " devices got");
        Log.i(TAG, "getCurrentConnectedDevice.");
        return devicesList;
    }

    /**
     * 获取上次连接的设备
     * @return
     */
    public BluetoothIBridgeDevice getLastConnectedDevice() {
        Log.i(TAG, "getLastConnectedDevice...");
        BluetoothIBridgeDevice device = null;
        SharedPreferences sp = mContext.getSharedPreferences(LAST_CONNECTED_DEVICE, Context.MODE_PRIVATE);
        if (sp != null) {
            String deviceName = sp.getString(LAST_CONNECTED_DEVICE_NAME, "");
            String deviceAddress = sp.getString(LAST_CONNECTED_DEVICE_ADDRESS, "");
            if (!TextUtils.isEmpty(deviceAddress) && deviceAddress != " ") {
                device = BluetoothIBridgeDevice.createBluetoothIBridgeDevice(deviceAddress);
            }
        }

        if (device == null) {
            Log.i(TAG, "no device found");
        } else {
            Log.i(TAG, "name:" + device.getDeviceName() + "/" + "address:" + device.getDeviceAddress());
        }
        Log.i(TAG, "getLastConnnectedDevice");
        return device;
    }

    public boolean setLastConnectedDevice(BluetoothIBridgeDevice device) {
        Log.i(TAG, "setLastConnectedDevice");
        if(device == null) {
            Log.i(TAG, "device is null");
            return false;
        } else {
            Log.i(TAG, "name:" + device.getDeviceName() + "/" + "address:" + device.getDeviceAddress());
        }
        SharedPreferences sp = mContext.getSharedPreferences(LAST_CONNECTED_DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LAST_CONNECTED_DEVICE_NAME, device.getDeviceName());
        editor.putString(LAST_CONNECTED_DEVICE_ADDRESS, device.getDeviceAddress());
        boolean flag = editor.commit();

        Log.i(TAG, "setLastConnectedDevice.");
        return flag;
    }

    public boolean cleanLastConnectedDevice() {
        Log.i(TAG, "clearLastConnectedDevice...");
        SharedPreferences sp = mContext.getSharedPreferences(LAST_CONNECTED_DEVICE, Context.MODE_PRIVATE);
        boolean flag = false;
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            flag = editor.commit();
        }
        Log.i(TAG, "clearLastConnectedDevice");
        return flag;
    }

    /**
     * 连接设备
     */
    public boolean connectDevice(BluetoothIBridgeDevice device) {
        return this.connectDevice(device, 10);
    }

    public boolean connectDevice(BluetoothIBridgeDevice device, int bondTime) {
        Log.i(TAG, "connectDevice...");
        Log.i(TAG, "bondTime = " + bondTime);
        boolean result = false;
        if (isEnabled()) {
            if (null == device) {
                Log.e(TAG, "device is null");
            } else if (!device.isValidDevice()) {
                Log.e(TAG, "device is not valid");
            } else {
                Log.i(TAG, "start to connect");
                mConnManager.connect(device, bondTime);
                result = true;
            }
        } else {
            Log.e(TAG, "bluetooth is not enabled");
        }
        Log.i(TAG, "connectDevice");
        return result;
    }

    /**
     * 断开设备连接
     */
    public void disconnectDevice(BluetoothIBridgeDevice device) {
        Log.i(TAG, "disconnectDevice...");
        if (isEnabled()) {
            if (null == device) {
                Log.e(TAG, "device is not enabled");
                return;
            }

            mConnManager.disconnect(device);
        }

        Log.i(TAG, "disconnectDevice");
    }

    /**
     * 发送信息
     * @param device
     * @param buffer
     * @param length
     */
    public void send(BluetoothIBridgeDevice device, byte[] buffer, int length) {
        if (isEnabled()) {
            if (null == device || null == mConnManager) {
                return;
            }
            if (device.isValidDevice()) {
                mConnManager.write(device, buffer, length);
            }
        }
    }

    /**
     * 关闭
     */
    public void destory() {
        if (mConnManager != null) {
            mConnManager.stop();
            mConnManager = null;
        }
        if (mContext != null) {
            mContext.unregisterReceiver(mReceiver);
        }
        mContext = null;
        sAdapter = null;
    }

    private void clean() {
        if (mConnManager != null) {
            mConnManager.stop();
            mConnManager = null;
        }
        mContext = null;
        sAdapter = null;
    }

    /**
     * 获取本地蓝牙设备名称
     */
    public String getLocalName() {
        Log.i(TAG, "getLocalName");
        Log.i(TAG, "local name is " + mAdapter.getName());
        return mAdapter.getName();
    }

    /**
     * 设置本地蓝牙设备名称
     */
    public boolean setLocalName(String name) {
        Log.i(TAG, "setLocalName to " + name);
        return mAdapter.setName(name);
    }

    public void setLinkKeyNeedAuthentiated(boolean authenticated) {
        Log.i(TAG, "setLinkKeyNeedAuthentiated to " + authenticated);
        if (mConnManager != null) {
            mConnManager.setLinkKeyNeedAuthenticated(authenticated);
        }
    }

    public void setAutoBondBeforConnect(boolean auto) {
        Log.i(TAG, "setAutoBondBeforConnect to " + auto);
        if (mConnManager != null) {
            mConnManager.setAutoBond(auto);
        }
    }

    public void cancelBondProcess() {
        Log.i(TAG, "cancelBondProcess...");
        if (mConnManager != null) {
            mConnManager.cancelBond();
        }
        Log.i(TAG, "cancelBondProcess");
    }

    /**
     * 设置本地蓝牙设备是否可被搜索到
     */
    public void setDiscoverable(boolean discoverable) {
        Log.i(TAG, "setDiscoverable to " + discoverable);
        if (isEnabled()) {
            int duration = discoverable ? 120 : 1;
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
            mContext.startActivity(discoverableIntent);

        }
    }

    private static String messageString(int message) {
        switch (message) {
            case 1:
                return "MESSAGE_DEVICE_CONNECTED";
            case 2:
                return "MESSAGE_DEVICE_DISCONNECTED";
            case 3:
            default:
                return "MESSAGE";
            case 4:
                return "MESSAGE_DEVICE_CONNECT_FAILED";
        }
    }


    /**
     * 事件接收器
     * @param what              消息ID
     * @param device            设备
     * @param exceptionMessage  错误信息
     */
    private void onEventReceived(int what, BluetoothIBridgeDevice device, String exceptionMessage) {
        if (this.mEventReceivers != null) {
            ArrayList listenersCopy = (ArrayList) this.mEventReceivers.clone();
            int numListeners = listenersCopy.size();

            for (int i = 0; i < numListeners; i++) {
                EventReceiver er = (EventReceiver) listenersCopy.get(i);
                switch (what) {
                    case MESSAGE_DEVICE_CONNECTED:          //设备已连接
                        er.onDeviceConnected(device);
                        break;
                    case MESSAGE_DEVICE_DISCONNECTED:       //设备断开连接
                        er.onDeviceDisconnected(device, exceptionMessage);
                        break;
                    case MESSAGE_DEVICE_CONNECT_FAILED:     //设备连接失败
                        er.onDeviceConnectFailed(device, exceptionMessage);
                        break;
                    case MESSAGE_DEVICE_FOUND:              //发现设备
                        boolean notifyFound = device != null;
                        if (this.mDiscoveryOnlyBonded && notifyFound) {
                            notifyFound = device.isBonded();
                        }

                        if (notifyFound) {
                            er.onDeviceFound(device);
                        }
                        break;
                    case MESSAGE_DISCOVERY_FINISHED:        //搜索设备结束
                        er.onDiscoveryFinished();
                        break;
                    case MESSAGE_WRITE_FAILED:              //发送信息失败
                        er.onWriteFailed(device, exceptionMessage);
                        break;
                }
            }
        }
    }

    /**
     * 获取IBridge版本号
     */
    public static String getVersion() {
        return VERSION_CODE;
    }

    public void setConnectType(String type) {
        Log.i(TAG, "setConnectType to " + type);
        mConnManager.setConnectType(type);
    }

    public void setPinCode(String pincode) {
        Log.i(TAG, "setPinCode to " + pincode);
        mConnManager.setPincode(pincode);
    }

    public void setAutoWritePincode(boolean autoWrite) {
        Log.i(TAG, "setAutoWritePincode to " + autoWrite);
        isAutoWritePincode = autoWrite;
    }

    static class MyHandler extends Handler {
        static final String BUNDLE_EXCEPTION = "exception";
        private final WeakReference<BluetoothIBridgeAdapter> mAdapter;

        public MyHandler(BluetoothIBridgeAdapter adapter) {
            this.mAdapter = new WeakReference<>(adapter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String exceptionMessage = null;
            Bundle bundle = msg.getData();
            if (bundle != null) {
                exceptionMessage = bundle.getString("exception");
            }

            BluetoothIBridgeAdapter adapter = mAdapter.get();
            Log.i(TAG, "recevier message:" + messageString(msg.what));
            BluetoothIBridgeDevice device = (BluetoothIBridgeDevice) msg.obj;
            if (adapter != null) {
                adapter.onEventReceived(msg.what, device, exceptionMessage);
            }
        }
    }

    class SaveInformationThread extends Thread {
        public SaveInformationThread() {
        }

        @Override
        public void run() {
            super.run();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            BluetoothIBridgeAdapter.this.saveInformationToServer(Build.MODEL + "|" + BluetoothIBridgeAdapter.this.mAdapter.getAddress() + "|" + formatter.format(curDate), BluetoothIBridgeAdapter.this.getInformation());
        }
    }

    public interface DataReceiver {
        void onDataReceived(BluetoothIBridgeDevice device, byte[] data, int dataLen);
    }

    public interface EventReceiver {
        void onDiscoveryFinished();

        void onDeviceFound(BluetoothIBridgeDevice device);

        void onDeviceConnected(BluetoothIBridgeDevice device);

        void onDeviceDisconnected(BluetoothIBridgeDevice device, String exceptionMessage);

        void onDeviceConnectFailed(BluetoothIBridgeDevice device, String exceptionMessage);

        void onWriteFailed(BluetoothIBridgeDevice device, String exceptionMessage);
    }
}
