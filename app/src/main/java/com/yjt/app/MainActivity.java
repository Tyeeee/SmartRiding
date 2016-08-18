package com.yjt.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.ui.adapter.FixedStickyViewAdapter;
import com.yjt.app.ui.adapter.MenuAdapter;
import com.yjt.app.ui.adapter.binder.MenuBinder;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.fragment.DeviceFragment;
import com.yjt.app.ui.fragment.HomeFragment;
import com.yjt.app.ui.fragment.MessageFragment;
import com.yjt.app.ui.fragment.SettingFragment;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.FragmentHelper;
import com.yjt.app.utils.SnackBarUtil;

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

    private FrameLayout flContent;
    private FragmentHelper mHelper;

    private Handler mFragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        flContent = (FrameLayout) findViewById(R.id.flContent);
        rvMenu = (RecyclerView) findViewById(R.id.rvMenu);
        rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        civHead = (CircleImageView) findViewById(R.id.civHead);
        tvAccountName = (TextView) findViewById(R.id.tvAccountName);
        tvTelphoneNumber = (TextView) findViewById(R.id.tvTelphoneNumber);
    }

    @Override
    protected void setListener() {
        drawerLayout.addDrawerListener(mToggle);
        rlHeader.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        List<Menu> menus = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setIcon(R.mipmap.ic_launcher);
        menu1.setTitle(getResources().getString(R.string.home_page));
        menus.add(menu1);
        Menu menu2 = new Menu();
        menu2.setIcon(R.mipmap.ic_launcher);
        menu2.setTitle(getResources().getString(R.string.device_management));
        menus.add(menu2);
        Menu menu3 = new Menu();
        menu3.setIcon(R.mipmap.ic_launcher);
        menu3.setTitle(getResources().getString(R.string.message));
        menus.add(menu3);
        Menu menu4 = new Menu();
        menu4.setIcon(R.mipmap.ic_launcher);
        menu4.setTitle(getResources().getString(R.string.setting));
        menus.add(menu4);
        mHelper = new FragmentHelper(getSupportFragmentManager(), R.id.flContent);
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ITEM_POSITION.HOME, HomeFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ITEM_POSITION.DEVICE, DeviceFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ITEM_POSITION.MESSAGE, MessageFragment.class));
        mHelper.addItem(new FragmentHelper.OperationInfo(this, Constant.ITEM_POSITION.SETTING, SettingFragment.class));

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(mLayoutManager);
        rvMenu.addItemDecoration(new LinearLayoutDividerItemDecoration(getResources().getColor(android.R.color.black), 1, LinearLayoutManager.VERTICAL));
        mAdapter = new MenuAdapter(this, new MenuBinder(this, rvMenu), false);
        mAdapter.addItems(menus);
        rvMenu.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlHeader:
                SnackBarUtil.getInstance().showSnackBar(view, getResources().getString(R.string.app_name), Snackbar.LENGTH_SHORT);
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case Constant.ITEM_POSITION.HOME:
                mHelper.show(Constant.ITEM_POSITION.HOME, false);
                drawerLayout.closeDrawers();
                break;
            case Constant.ITEM_POSITION.DEVICE:
                mHelper.show(Constant.ITEM_POSITION.DEVICE, false);
                drawerLayout.closeDrawers();
                break;
            case Constant.ITEM_POSITION.MESSAGE:
                mHelper.show(Constant.ITEM_POSITION.MESSAGE, false);
                drawerLayout.closeDrawers();
                break;
            case Constant.ITEM_POSITION.SETTING:
                mHelper.show(Constant.ITEM_POSITION.SETTING, false);
                drawerLayout.closeDrawers();
                break;
            default:
                mHelper.show(Constant.ITEM_POSITION.HOME, false);
                drawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showExitDialog();
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

    }

    public void setFragmentHandler(Handler handler) {
        this.mFragmentHandler = handler;
    }
}
