package com.yjt.app.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
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
import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.ui.adapter.FixedStickyViewAdapter;
import com.yjt.app.ui.adapter.MenuAdapter;
import com.yjt.app.ui.adapter.binder.MenuBinder;
import com.yjt.app.ui.base.BaseFragment;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends BaseFragment implements FixedStickyViewAdapter.OnItemClickListener {

    private CircleImageView civDevice;
    private RecyclerView rvMenu;
    private LinearLayoutManager mLayoutManager;
    private FixedStickyViewAdapter mAdapter;
    private SettingHandler mHandler;

    private static class SettingHandler extends Handler {

        private WeakReference<SettingFragment> mFragments;

        public SettingHandler(SettingFragment fragment) {
            mFragments = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SettingFragment fragment = mFragments.get();
            if (fragment != null) {
                switch (msg.what) {
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
        mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
        findViewById();
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
    protected void initialize(Bundle savedInstanceState) {
        mHandler = new SettingHandler(this);
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
    }

    @Override
    protected void setListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

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
                SnackBarUtil.getInstance().showSnackBar(mRootView, "SEARCH_DEVICE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.GENERAL_SETTING:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "GENERAL_SETTING", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.CHECK_UPDATE:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "CHECK_UPDATE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.CLEAR_DATA:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "CLEAR_DATA", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.BREAK_LINK:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "BREAK_LINK", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.ABOUT_DEVICE:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "ABOUT_DEVICE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
        }
    }
}
