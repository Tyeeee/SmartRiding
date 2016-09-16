package com.yjt.app.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.yjt.app.ui.dialog.ProgressDialog;
import com.yjt.app.ui.listener.bluetooth.OnConnectListener;
import com.yjt.app.ui.listener.bluetooth.OnDataAvailableListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectListener;
import com.yjt.app.ui.listener.bluetooth.OnServiceDiscoverListener;
import com.yjt.app.ui.listener.bluetooth.implement.CustomLeScanCallback;
import com.yjt.app.ui.listener.bluetooth.implement.CustomScanCallback;
import com.yjt.app.ui.listener.dialog.OnDialogCancelListener;
import com.yjt.app.ui.listener.dialog.OnListDialogListener;
import com.yjt.app.ui.sticky.FixedStickyViewAdapter;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.DataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class DeviceFragment extends BaseFragment implements FixedStickyViewAdapter.OnItemClickListener, OnDialogCancelListener, OnListDialogListener, OnServiceDiscoverListener, OnDataAvailableListener, OnConnectListener, OnDisconnectListener {

    private CircleImageView        civDevice;
    private RecyclerView           rvMenu;
    private LinearLayoutManager    mLayoutManager;
    private FixedStickyViewAdapter mAdapter;
    private DeviceHandler          mHandler;
    private ProgressDialog         mDialog;

    private BluetoothAdapter     mBluetoothAdapter;
    private BluetoothLeScanner   mScanner;
    private CustomLeScanCallback mLeScanCallback;
    private CustomScanCallback   mScanCallback;
    private BluetoothService     mService;

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
                        fragment.mDialog.dismiss();
                        ArrayList<BluetoothDevice> devices = (ArrayList<BluetoothDevice>) msg.obj;
                        if (devices != null && devices.size() > 0) {
                            DeviceListDialog.createBuilder(fragment.getFragmentManager())
                                    .setItems(devices)
                                    .setTitle(fragment.getString(R.string.search_device))
                                    .setNegativeButtonText(R.string.cancel)
                                    .setTargetFragment(fragment, Constant.RequestCode.DIALOG_LIST)
                                    .setCancelableOnTouchOutside(false)
                                    .showAllowingStateLoss();
                        } else {
                            SnackBarUtil.getInstance().showSnackBar(fragment.getActivity(), fragment.getString(R.string.search_device_prompt1), Snackbar.LENGTH_SHORT, Color.WHITE);
                        }
                        fragment.isScaning = false;
                        break;
                    case Constant.Bluetooth.GET_DEVICE_LIST_FAILED:
                        BluetoothUtil.getInstance().stopScanner(fragment.mBluetoothAdapter, fragment.mScanner, fragment.mScanCallback, fragment.mLeScanCallback, true);
                        fragment.mDialog.dismiss();
                        SnackBarUtil.getInstance().showSnackBar(fragment.getActivity(), fragment.getString(R.string.search_device_prompt2), Snackbar.LENGTH_SHORT, Color.WHITE);
                        fragment.isScaning = false;
                        break;
                    case Constant.Bluetooth.GET_DEVICE_LIST_ERROR:
                        BluetoothUtil.getInstance().stopScanner(fragment.mBluetoothAdapter, fragment.mScanner, fragment.mScanCallback, fragment.mLeScanCallback, true);
                        fragment.mDialog.dismiss();
                        SnackBarUtil.getInstance().showSnackBar(fragment.getActivity(), fragment.getString(R.string.search_device_prompt3), Snackbar.LENGTH_SHORT, Color.WHITE);
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
                Menu       menu1 = new Menu();
                menu1.setIcon(R.mipmap.dir1);
                menu1.setTitle(getResources().getString(R.string.search_device));
                menus.add(menu1);
                Menu menu2 = new Menu();
                menu2.setIcon(R.mipmap.dir1);
                menu2.setTitle(getResources().getString(R.string.general_setting));
                menus.add(menu2);
                Menu menu3 = new Menu();
                menu3.setIcon(R.mipmap.dir1);
                menu3.setTitle(getResources().getString(R.string.check_update));
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
            mService.setOnConnectListener(this);
            mService.setOnDisconnectListener(this);
            mService.setOnServiceDiscoverListener(this);
            mService.setOnDataAvailableListener(this);
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
        if (isScaning && mDialog != null) {
            mDialog.dismiss();
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
                startScanner();
                break;
            case Constant.ItemPosition.GENERAL_SETTING:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "GENERAL_Device", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.CHECK_UPDATE:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "CHECK_UPDATE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.CLEAR_DATA:
                SnackBarUtil.getInstance().showSnackBar(getActivity(), "CLEAR_DATA", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.BREAK_LINK:
                if (mService != null) {
                    mService.disconnect();
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
            case Constant.RequestCode.DIALOG_LIST:
                BluetoothUtil.getInstance().stopScanner(mBluetoothAdapter, mScanner, mScanCallback, mLeScanCallback, true);
                isScaning = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {

    }

    @Override
    public void onListItemSelected(Object value, int number, int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_LIST:
                if (value != null) {
                    if (mService.connect(((BluetoothDevice) value).getAddress())) {
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
    public void onServiceDiscover(BluetoothGatt gatt) {
        if (mService != null) {
            List<BluetoothGattService> services = mService.getSupportedGattServices();
            if (services != null && services.size() > 0) {
                for (BluetoothGattService service : services) {
                    LogUtil.print("---->service type:" + BluetoothUtil.getInstance().getServiceType(service.getType()));
                    LogUtil.print("---->service uuid:" + service.getUuid().toString());
                    LogUtil.print("---->includedServices size:" + service.getIncludedServices().size());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        LogUtil.print("---->char uuid:" + characteristic.getUuid());
                        LogUtil.print("---->char permission:" + BluetoothUtil.getInstance().getGattCharacteristicInfo(characteristic.getPermissions(), true));
                        LogUtil.print("---->char property:" + BluetoothUtil.getInstance().getGattCharacteristicInfo(characteristic.getProperties(), false));
                        LogUtil.print("---->char value:" + Arrays.toString(characteristic.getValue()));
                        switch (service.getUuid().toString()) {
                            case Constant.Bluetooth.UUID1:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID2:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID3:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID4:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID5:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID6:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID7:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID8:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUID9:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            case Constant.Bluetooth.UUIDA:
                                BaseApplication.getInstance().setCharacteristic(characteristic);
                                break;
                            default:
                                break;
                        }
                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                            LogUtil.print("---->desc uuid:" + descriptor.getUuid());
                            LogUtil.print("---->desc permission:" + BluetoothUtil.getInstance().getGattCharacteristicInfo(characteristic.getPermissions(), true));
                            LogUtil.print("---->desc value:" + Arrays.toString(descriptor.getValue()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        LogUtil.print("---->Device:" + gatt.getDevice().getName() + ",Read:" + characteristic.getUuid() + "__" + DataUtil.getInstance().bytesToHexString(characteristic.getValue()));
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        LogUtil.print("---->Device:" + gatt.getDevice().getName() + ",Write:" + characteristic.getUuid() + "__" + DataUtil.getInstance().bytesToHexString(characteristic.getValue()));
    }


    @Override
    public void onConnect(BluetoothGatt gatt) {
        LogUtil.print("---->onConnect");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                civDevice.setText(getString(R.string.bluetooth_status4));
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothGatt gatt) {
        LogUtil.print("---->onDisconnect");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                civDevice.setText(getString(R.string.bluetooth_status3));
            }
        });
    }


    private void startScanner() {
        mDialog = (ProgressDialog) ProgressDialog.createBuilder(getFragmentManager())
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
