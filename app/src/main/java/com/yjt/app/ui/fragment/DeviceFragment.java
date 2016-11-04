package com.yjt.app.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.entity.Menu;
import com.yjt.app.receiver.BluetoothReceiver;
import com.yjt.app.service.BluetoothService;
import com.yjt.app.ui.adapter.MenuAdapter;
import com.yjt.app.ui.adapter.binder.MenuBinder;
import com.yjt.app.ui.base.BaseFragment;
import com.yjt.app.ui.dialog.DeviceListDialog;
import com.yjt.app.ui.dialog.ListDialog;
import com.yjt.app.ui.dialog.ProgressDialog;
import com.yjt.app.ui.listener.bluetooth.OnConnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnDataListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnMtuChangedListener;
import com.yjt.app.ui.listener.bluetooth.OnReadRemoteRssiListener;
import com.yjt.app.ui.listener.bluetooth.OnServicesDiscoveredListener;
import com.yjt.app.ui.listener.bluetooth.implement.CustomLeScanCallback;
import com.yjt.app.ui.listener.bluetooth.implement.CustomScanCallback;
import com.yjt.app.ui.listener.dialog.OnDialogCancelListener;
import com.yjt.app.ui.listener.dialog.OnListDialogListener;
import com.yjt.app.ui.sticky.FixedStickyViewAdapter;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class DeviceFragment extends BaseFragment implements FixedStickyViewAdapter.OnItemClickListener, OnDialogCancelListener, OnListDialogListener, OnServicesDiscoveredListener, OnDataListener, OnConnectedListener, OnDisconnectedListener, OnMtuChangedListener, OnReadRemoteRssiListener {

    private CircleImageView civDevice;
    private RecyclerView rvMenu;
    private LinearLayoutManager mLayoutManager;
    private FixedStickyViewAdapter mAdapter;
    private DeviceHandler mHandler;
    private DialogFragment mDialog;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private CustomLeScanCallback mLeScanCallback;
    private CustomScanCallback mScanCallback;
    private BluetoothService mService;

    private boolean isScaning;

    private static class DeviceHandler extends Handler {

        private WeakReference<DeviceFragment> mFragments;

        public DeviceHandler(DeviceFragment fragment) {
            mFragments = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DeviceFragment fragment = mFragments.get();
            if (fragment != null) {
                switch (msg.what) {
                    case Constant.Bluetooth.GET_DEVICE_LIST_SUCCESS:
                        BluetoothUtil.getInstance().stopScanner(fragment.mBluetoothAdapter, fragment.mScanner, fragment.mScanCallback, fragment.mLeScanCallback, false);
                        ViewUtil.getInstance().hideDialog(fragment.mDialog);
                        ArrayList<BluetoothDevice> devices = (ArrayList<BluetoothDevice>) msg.obj;
                        if (devices != null && devices.size() > 0) {
                            DeviceListDialog.createBuilder(fragment.getFragmentManager())
                                    .setItems(devices)
                                    .setTitle(fragment.getString(R.string.search_device))
                                    .setNegativeButtonText(R.string.cancel)
                                    .setTargetFragment(fragment, Constant.RequestCode.DIALOG_LIST_DEVICE_SEARCH)
                                    .setCancelableOnTouchOutside(false)
                                    .showAllowingStateLoss();
                        } else {
                            ToastUtil.getInstance().showToast(fragment.getActivity(), fragment.getString(R.string.search_device_prompt1), Toast.LENGTH_SHORT);
                        }
                        fragment.isScaning = false;
                        break;
                    case Constant.Bluetooth.GET_DEVICE_LIST_FAILED:
                        BluetoothUtil.getInstance().stopScanner(fragment.mBluetoothAdapter, fragment.mScanner, fragment.mScanCallback, fragment.mLeScanCallback, true);
                        ViewUtil.getInstance().hideDialog(fragment.mDialog);
                        SnackBarUtil.getInstance().showSnackBar(fragment.getActivity(), fragment.getString(R.string.search_device_prompt2), Snackbar.LENGTH_SHORT, Color.WHITE);
                        fragment.isScaning = false;
                        break;
                    case Constant.Bluetooth.GET_DEVICE_LIST_ERROR:
                        BluetoothUtil.getInstance().stopScanner(fragment.mBluetoothAdapter, fragment.mScanner, fragment.mScanCallback, fragment.mLeScanCallback, true);
                        ViewUtil.getInstance().hideDialog(fragment.mDialog);
                        ToastUtil.getInstance().showToast(fragment.getActivity(), fragment.getString(R.string.search_device_prompt3), Toast.LENGTH_SHORT);
                        fragment.isScaning = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_device, container, false);
        findViewById();
        setViewListener();
        initialize(savedInstanceState);
        setListener();
        return mRootView;
    }

    @Override
    protected void findViewById() {
        civDevice = ViewUtil.getInstance().findView(mRootView, R.id.civDevice);
        rvMenu = ViewUtil.getInstance().findView(mRootView, R.id.rvMenu);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mHandler = new DeviceHandler(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(getResources().getColor(android.R.color.black), 1, LinearLayoutManager.VERTICAL));
        mAdapter = new MenuAdapter(getActivity(), new MenuBinder(getActivity(), rvMenu), false);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<Menu> menus = new ArrayList<>();
                Menu menu1 = new Menu();
                menu1.setIcon(R.mipmap.dir1);
                menu1.setTitle(getResources().getString(R.string.search_device));
                menus.add(menu1);
                Menu menu2 = new Menu();
                menu2.setIcon(R.mipmap.dir1);
                menu2.setTitle(getResources().getString(R.string.general_setting));
                menus.add(menu2);
                Menu menu3 = new Menu();
                menu3.setIcon(R.mipmap.dir1);
                menu3.setTitle(getResources().getString(R.string.device_detect));
                menus.add(menu3);
                Menu menu4 = new Menu();
                menu4.setIcon(R.mipmap.dir1);
                menu4.setTitle(getResources().getString(R.string.clear_data));
                menus.add(menu4);
                Menu menu5 = new Menu();
                menu5.setIcon(R.mipmap.dir1);
                menu5.setTitle(getResources().getString(R.string.break_link));
                menus.add(menu5);
                Menu menu6 = new Menu();
                menu6.setIcon(R.mipmap.dir1);
                menu6.setTitle(getResources().getString(R.string.about_device));
                menus.add(menu6);
                mAdapter.addItems(menus);
            }
        });
        rvMenu.setAdapter(mAdapter);
        if (BluetoothUtil.getInstance().isBluetoothEnabled()) {
            BluetoothManager manager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = manager.getAdapter();
            mService = new BluetoothService();
            mService.setAdapter(mBluetoothAdapter);
        } else {
            mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.GET_DEVICE_LIST_ERROR));
        }
    }


    @Override
    protected void setListener() {
        mAdapter.setOnItemClickListener(this);
        if (mService != null) {
            mService.setOnConnectedListener(this);
            mService.setOnDisconnectedListener(this);
            mService.setServicesDiscoveredListener(this);
            mService.setOnReadRemoteRssiListener(this);
            mService.setOnDataListener(this);
            mService.setOnMtuChangedListener(this);
        }
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isScaning) {
            startScanner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isScaning) {
            ViewUtil.getInstance().hideDialog(mDialog);
            BluetoothUtil.getInstance().stopScanner(mBluetoothAdapter, mScanner, mScanCallback, mLeScanCallback, true);
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected void endOperation() {

    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case Constant.ItemPosition.SEARCH_DEVICE:
                if (BaseApplication.getInstance().getBluetoothGatt() == null) {
                    startScanner();
                } else {
                    ToastUtil.getInstance().showToast(getActivity(), getString(R.string.bluetooth_status4), Toast.LENGTH_SHORT);
                }
                break;
            case Constant.ItemPosition.GENERAL_SETTING:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "GENERAL_SETTING", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.DEVICE_DETECT:
                if (BaseApplication.getInstance().getBluetoothGatt() != null) {
                    ListDialog.createBuilder(getFragmentManager())
                            .setTitle(getString(R.string.device_detect))
                            .setItems(getString(R.string.light_left)
                                    , getString(R.string.light_right)
                                    , getString(R.string.light_open)
                                    , getString(R.string.light_close)
                                    , getString(R.string.read_device_name)
                                    , getString(R.string.write_device_name)
                                    , getString(R.string.dump_energy))
                            .setChoiceMode(AbsListView.CHOICE_MODE_NONE)
                            .setTargetFragment(this, Constant.RequestCode.DIALOG_LIST_DEVICE_DETECT)
                            .showAllowingStateLoss();
                } else {
                    ToastUtil.getInstance().showToast(getActivity(), getString(R.string.bluetooth_status7), Toast.LENGTH_SHORT);
                }
                break;
            case Constant.ItemPosition.CLEAR_DATA:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "CLEAR_DATA", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.BREAK_LINK:
                if (mService != null) {
                    mService.disconnect();
                } else {
                    ToastUtil.getInstance().showToast(getActivity(), getString(R.string.bluetooth_status7), Toast.LENGTH_SHORT);
                }
                break;
            case Constant.ItemPosition.ABOUT_DEVICE:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "ABOUT_DEVICE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
        }
    }

    @Override
    public void onCanceled(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROGRESS_DEVICE_SEARCH:
                BluetoothUtil.getInstance().stopScanner(mBluetoothAdapter, mScanner, mScanCallback, mLeScanCallback, true);
                isScaning = false;
                break;
            case Constant.RequestCode.DIALOG_PROGRESS_DEVICE_CONNECT:
                if (mService != null) {
                    mService.disconnect();
                }
                break;
            case Constant.RequestCode.DIALOG_LIST_DEVICE_SEARCH:
                BluetoothUtil.getInstance().stopScanner(mBluetoothAdapter, mScanner, mScanCallback, mLeScanCallback, true);
                isScaning = false;
                break;
            case Constant.RequestCode.DIALOG_LIST_DEVICE_DETECT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_LIST_DEVICE_DETECT:
                BluetoothGattCharacteristic characteristic = BluetoothUtil.getInstance().findCharacteristic(BaseApplication.getInstance().getBluetoothGatt(), UUID.fromString(Constant.Bluetooth.UUIDA));
                LogUtil.print("---->mService:" + mService);
                LogUtil.print("---->characteristic:" + characteristic);
                if (mService != null && characteristic != null) {
                    switch (number) {
                        case Constant.ItemPosition.LIGHT_LEFT:
                            mService.writeCharacteristic(characteristic, Constant.Bluetooth.DATA_LIGHT_LEFT);
                            break;
                        case Constant.ItemPosition.LIGHT_RIGHT:
                            mService.writeCharacteristic(characteristic, Constant.Bluetooth.DATA_LIGHT_RIGHT);
                            break;
                        case Constant.ItemPosition.LIGHT_OPEN:
                            mService.writeCharacteristic(characteristic, Constant.Bluetooth.DATA_LIGHT_OPEN);
                            break;
                        case Constant.ItemPosition.LIGHT_CLOSE:
                            mService.writeCharacteristic(characteristic, Constant.Bluetooth.DATA_LIGHT_CLOSE);
                            break;
                        case Constant.ItemPosition.READ_DEVICE_NAME:
                            mService.readDeviceName();
                            break;
                        case Constant.ItemPosition.WRITE_DEVICE_NAME:
                            mService.writeDeviceName(Constant.Bluetooth.DEVICE_NAME.getBytes());
                            break;
                        case Constant.ItemPosition.DUMP_ENERGY:
                            mService.readDumpEnergy();
                            break;
                        default:
                            break;
                    }
                }
            default:
                break;
        }
    }

    @Override
    public void onListItemSelected(Object value, int number, int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_LIST_DEVICE_SEARCH:
                if (value != null) {
                    if (mService.connect(((BluetoothDevice) value).getAddress())) {
                        mDialog = ProgressDialog.createBuilder(getFragmentManager())
                                .setPrompt(getString(R.string.device_connecting))
                                .setCancelableOnTouchOutside(false)
                                .setTargetFragment(this, Constant.RequestCode.DIALOG_PROGRESS_DEVICE_CONNECT)
                                .showAllowingStateLoss();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(Constant.Bluetooth.ACTION_RSSI);
                        filter.addAction(Constant.Bluetooth.ACTION_CONNECT);
                        BluetoothReceiver.getInstance().registerReceiver(filter);
                    }
                }
                BluetoothUtil.getInstance().stopScanner(mBluetoothAdapter, mScanner, mScanCallback, mLeScanCallback, true);
                isScaning = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt) {
        LogUtil.print("---->onServicesDiscovered");
        BluetoothUtil.getInstance().getGattInfo(gatt);
    }


    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        LogUtil.print("---->onServicesDiscovered");
    }


    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        LogUtil.print("---->onCharacteristicRead:" + gatt.getDevice().getName()
                              + ",status:" + status
                              + ",uid:" + characteristic.getUuid()
                              + ",value type:" + BluetoothUtil.getInstance().resolveValueTypeDescription(characteristic.getProperties())
                              + ",value:" + Arrays.toString(characteristic.getValue()));
        if (characteristic.getUuid().equals(UUID.fromString(Constant.Bluetooth.BATTERY_CHARACTERISTIC_UUID))) {
            ToastUtil.getInstance().showToast(getActivity(), "battery:" + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0), Toast.LENGTH_SHORT);
        } else if (characteristic.getUuid().equals(UUID.fromString(Constant.Bluetooth.DEVICE_NAME_CHARACTERISTIC_UUID))) {
            ToastUtil.getInstance().showToast(getActivity(), "name:" + new String(characteristic.getValue()), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        LogUtil.print("---->onCharacteristicWrite:"
                              + gatt.getDevice().getName()
                              + ",uid:" + characteristic.getUuid()
                              + ",value type:" + BluetoothUtil.getInstance().resolveValueTypeDescription(characteristic.getProperties())
                              + ",value:" + Arrays.toString(characteristic.getValue()));
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        LogUtil.print("---->onCharacteristicChanged:"
                              + gatt.getDevice().getName()
                              + ",uid:" + characteristic.getUuid()
                              + ",value type:" + BluetoothUtil.getInstance().resolveValueTypeDescription(characteristic.getProperties())
                              + ",value:" + Arrays.toString(characteristic.getValue()));
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        LogUtil.print("---->onDescriptorRead:"
                              + gatt.getDevice().getName()
                              + ",uid:" + descriptor.getUuid()
                              + ",status:" + status
                              + ",value:" + Arrays.toString(descriptor.getValue()));
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        LogUtil.print("---->onDescriptorWrite:"
                              + gatt.getDevice().getName()
                              + ",uid:" + descriptor.getUuid()
                              + ",status:" + status
                              + ",value:" + Arrays.toString(descriptor.getValue()));
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        LogUtil.print("---->onReliableWriteCompleted:" + gatt.getDevice().getName() + ",status:" + status);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        LogUtil.print("---->onMtuChanged:" + gatt.getDevice().getName() + ",mtu:" + mtu + ",status:" + status);
    }

    @Override
    public void onConnected(BluetoothGatt gatt) {
        LogUtil.print("---->onConnected:" + gatt);
        ViewUtil.getInstance().hideDialog(mDialog);
        BaseApplication.getInstance().setBluetoothGatt(gatt);
        BaseApplication.getInstance().setService(mService);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                civDevice.setText(getString(R.string.bluetooth_status4));
            }
        });
    }

    @Override
    public void onDisconnected(BluetoothGatt gatt) {
        LogUtil.print("---->onDisconnected:" + gatt);
        ViewUtil.getInstance().hideDialog(mDialog);
        gatt.close();
        BluetoothUtil.getInstance().refreshCache(gatt);
        BaseApplication.getInstance().setBluetoothGatt(null);
        BaseApplication.getInstance().setService(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                civDevice.setText(getString(R.string.bluetooth_status3));
            }
        });
    }


    private void startScanner() {
        mDialog = ProgressDialog.createBuilder(getFragmentManager())
                .setPrompt(getString(R.string.device_searching))
                .setCancelableOnTouchOutside(false)
                .setTargetFragment(this, Constant.RequestCode.DIALOG_PROGRESS_DEVICE_SEARCH)
                .showAllowingStateLoss();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mScanCallback = new CustomScanCallback(mHandler);
                        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
                        mScanner.startScan(mScanCallback);
                    } else {
                        mLeScanCallback = new CustomLeScanCallback(mHandler);
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    }
                } else {
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.GET_DEVICE_LIST_ERROR));
                }
                isScaning = true;
            }
        });
    }

}
