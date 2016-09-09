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
import android.widget.Button;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.ui.adapter.MenuAdapter;
import com.yjt.app.ui.adapter.binder.MenuBinder;
import com.yjt.app.ui.base.BaseFragment;
import com.yjt.app.ui.sticky.FixedStickyViewAdapter;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends BaseFragment implements View.OnClickListener, FixedStickyViewAdapter.OnItemClickListener {

    private RecyclerView rvMenu;
    private LinearLayoutManager mLayoutManager;
    private FixedStickyViewAdapter mAdapter;
    private SettingHandler mHandler;
    private Button btnLogout;

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
        setViewListener();
        initialize(savedInstanceState);
        setListener();
        return mRootView;
    }

    @Override
    protected void findViewById() {
        rvMenu = ViewUtil.getInstance().findView(mRootView, R.id.rvMenu);
        btnLogout = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.btnLogout, this);
    }

    @Override
    protected void setViewListener() {

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
                menu1.setTitle(getResources().getString(R.string.password_management));
                menus.add(menu1);
                Menu menu2 = new Menu();
                menu2.setIcon(R.mipmap.dir1);
                menu2.setTitle(getResources().getString(R.string.device_label));
                menus.add(menu2);
                Menu menu3 = new Menu();
                menu3.setIcon(R.mipmap.dir1);
                menu3.setTitle(getResources().getString(R.string.use_help));
                menus.add(menu3);
                Menu menu4 = new Menu();
                menu4.setIcon(R.mipmap.dir1);
                menu4.setTitle(getResources().getString(R.string.feedback));
                menus.add(menu4);
                Menu menu5 = new Menu();
                menu5.setIcon(R.mipmap.dir1);
                menu5.setTitle(getResources().getString(R.string.version_update));
                menus.add(menu5);
                Menu menu6 = new Menu();
                menu6.setIcon(R.mipmap.dir1);
                menu6.setTitle(getResources().getString(R.string.about_us));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "btnLogout", Snackbar.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case Constant.ItemPosition.PASSWORD_MANAGEMENT:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "PASSWORD_MANAGEMENT", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.DEVICE_LABEL:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "DEVICE_LABEL", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.USE_HELP:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "USE_HELP", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.FEEDBACK:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "FEEDBACK", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.VERSION_UPDATE:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "VERSION_UPDATE", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
            case Constant.ItemPosition.ABOUT_US:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "ABOUT_US", Snackbar.LENGTH_SHORT, Color.WHITE);
                break;
        }
    }
}
