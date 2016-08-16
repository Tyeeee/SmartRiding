package com.yjt.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.SnackBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView nvMenu;
    private RelativeLayout rlHeader;
    private ImageView ivHeader;
    private TextView tvAccountName;
    private TextView tvTelphoneNumber;
    private ActionBarDrawerToggle mToggle;

//    static {
//        System.loadLibrary("native-lib");
//    }

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
        nvMenu = (NavigationView) findViewById(R.id.nvMenu);
        rlHeader = (RelativeLayout) nvMenu.getHeaderView(0).findViewById(R.id.rlHeader);
        ivHeader = (ImageView) nvMenu.getHeaderView(0).findViewById(R.id.ivHead);
        tvAccountName = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvAccountName);
        tvTelphoneNumber = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.tvTelphoneNumber);
    }

    @Override
    protected void setListener() {
        drawerLayout.addDrawerListener(mToggle);
        nvMenu.getHeaderView(0).setOnClickListener(this);
        nvMenu.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        nvMenu.setItemIconTintList(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlHeader:
                SnackBarUtil.getInstance().showSnackBar(nvMenu, getResources().getString(R.string.app_name), Snackbar.LENGTH_SHORT);
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemHomePage:
                SnackBarUtil.getInstance().showSnackBar(nvMenu, getResources().getString(R.string.home_page), Snackbar.LENGTH_SHORT);
                break;
            case R.id.itemDeviceManagement:
                SnackBarUtil.getInstance().showSnackBar(nvMenu, getResources().getString(R.string.device_management), Snackbar.LENGTH_SHORT);
                break;
            case R.id.itemMessage:
                SnackBarUtil.getInstance().showSnackBar(nvMenu, getResources().getString(R.string.message), Snackbar.LENGTH_SHORT);
                break;
            case R.id.itemSetting:
                SnackBarUtil.getInstance().showSnackBar(nvMenu, getResources().getString(R.string.setting), Snackbar.LENGTH_SHORT);
                break;
        }
        drawerLayout.closeDrawers();
        return true;
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

//    public native String stringFromJNI();
}
