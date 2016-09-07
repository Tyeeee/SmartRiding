package com.yjt.app.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.receiver.BluetoothReceiver;
import com.yjt.app.ui.adapter.MenuAdapter;
import com.yjt.app.ui.adapter.binder.MenuBinder;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.fragment.DeviceFragment;
import com.yjt.app.ui.fragment.HomeFragment;
import com.yjt.app.ui.fragment.MessageFragment;
import com.yjt.app.ui.fragment.SettingFragment;
import com.yjt.app.ui.sticky.FixedStickyViewAdapter;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.ApplicationUtil;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.FragmentHelper;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, FixedStickyViewAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    private RelativeLayout rlHeader;
    private CircleImageView civHead;
    private TextView tvAccountName;
    private TextView tvTelphoneNumber;

    private RecyclerView rvMenu;
    private LinearLayoutManager mLayoutManager;
    private FixedStickyViewAdapter mAdapter;

    private FragmentHelper mHelper;

    private BluetoothReceiver mReceiver;

    private Handler mFragmentHandler;
    private MainHandler mHandler;

    protected static class MainHandler extends Handler {

        private WeakReference<MainActivity> mActivitys;

        public MainHandler(MainActivity activity) {
            mActivitys = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity activity = mActivitys.get();
            if (activity != null) {
                switch (msg.what) {
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setViewListener();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void findViewById() {
        toolbar = ViewUtil.getInstance().findView(this, R.id.toolbar);
        drawerLayout = ViewUtil.getInstance().findView(this, R.id.drawerLayout);
        rvMenu = ViewUtil.getInstance().findView(this, R.id.rvMenu);
        rlHeader = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlHeader, this);
        civHead = ViewUtil.getInstance().findView(this, R.id.civHead);
        tvAccountName = ViewUtil.getInstance().findView(this, R.id.tvAccountName);
        tvTelphoneNumber = ViewUtil.getInstance().findView(this, R.id.tvTelphoneNumber);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        LogUtil.print("---->" + ApplicationUtil.getInstance().getSha1());
        mHandler = new MainHandler(this);
        mReceiver = new BluetoothReceiver();
        if (BluetoothUtil.getInstance().isBluetoothSupported() && !BluetoothUtil.getInstance().isBluetoothEnabled()) {
            registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            BluetoothUtil.getInstance().turnOnBluetooth();
        } else {
            ToastUtil.getInstance().showToast(this, R.string.bluetooth_status5, Toast.LENGTH_SHORT);
        }
        mHelper = new FragmentHelper(getSupportFragmentManager(), R.id.flContent);
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ItemPosition.HOME, HomeFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ItemPosition.DEVICE, DeviceFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ItemPosition.MESSAGE, MessageFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ItemPosition.SETTING, SettingFragment.class));
        mHelper.show(Constant.ItemPosition.HOME, false, toolbar, R.string.home_page);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(getResources().getColor(android.R.color.black), 1, LinearLayoutManager.VERTICAL));
        mAdapter = new MenuAdapter(this, new MenuBinder(this, rvMenu), false);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<Menu> menus = new ArrayList<>();
                Menu menu1 = new Menu();
                menu1.setIcon(R.mipmap.dir1);
                menu1.setTitle(getResources().getString(R.string.home_page));
                menus.add(menu1);
                Menu menu2 = new Menu();
                menu2.setIcon(R.mipmap.dir1);
                menu2.setTitle(getResources().getString(R.string.device_management));
                menus.add(menu2);
                Menu menu3 = new Menu();
                menu3.setIcon(R.mipmap.dir1);
                menu3.setTitle(getResources().getString(R.string.message));
                menus.add(menu3);
                Menu menu4 = new Menu();
                menu4.setIcon(R.mipmap.dir1);
                menu4.setTitle(getResources().getString(R.string.setting));
                menus.add(menu4);
                mAdapter.addItems(menus);
            }
        });
        rvMenu.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        drawerLayout.addDrawerListener(mToggle);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        InputUtil.getInstance().hideKeyBoard(this, view);
        switch (view.getId()) {
            case R.id.rlHeader:
                SnackBarUtil.getInstance().showSnackBar(view, getResources().getString(R.string.app_name), Snackbar.LENGTH_SHORT, Color.WHITE);
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case Constant.ItemPosition.HOME:
                mHelper.show(Constant.ItemPosition.HOME, false, toolbar, R.string.home_page);
                drawerLayout.closeDrawers();
                break;
            case Constant.ItemPosition.DEVICE:
                mHelper.show(Constant.ItemPosition.DEVICE, false, toolbar, R.string.device_management);
                drawerLayout.closeDrawers();
                break;
            case Constant.ItemPosition.MESSAGE:
                mHelper.show(Constant.ItemPosition.MESSAGE, false, toolbar, R.string.message);
                drawerLayout.closeDrawers();
                break;
            case Constant.ItemPosition.SETTING:
                mHelper.show(Constant.ItemPosition.SETTING, false, toolbar, R.string.setting);
                drawerLayout.closeDrawers();
                break;
            default:
                mHelper.show(Constant.ItemPosition.HOME, false, toolbar, R.string.home_page);
                drawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void permissionRequestIntent() {

    }

    @Override
    protected void permissionRequestResult() {

    }

    @Override
    protected void endOperation() {
        if (BluetoothUtil.getInstance().isBluetoothSupported() && BluetoothUtil.getInstance().isBluetoothEnabled()) {
            BluetoothUtil.getInstance().turnOffBluetooth();
            unregisterReceiver(mReceiver);
        }
    }

    public void setFragmentHandler(Handler handler) {
        this.mFragmentHandler = handler;
    }
}
