package com.yjt.app.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.utils.LogUtil;

import java.lang.reflect.Field;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onAttach() invoked!!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onCreate() invoked!!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onCreateView() invoked!!");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onViewCreated() invoked!!");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onActivityCreated() invoked!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(getClass().getName(), getClass().getSimpleName() + " onStart() invoked!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onResume() invoked!!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setSavedInstanceState(outState);
        super.onSaveInstanceState(outState);
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onSaveInstanceState() invoked!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(getClass().getName(), getClass().getSimpleName() + " onPause() invoked!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName() + " onStop() invoked!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onDestroyView() invoked!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onDestroy() invoked!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(getClass().getName(), getClass().getSimpleName()
                + " onDetach() invoked!!");
        try {
            Field field = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            field.setAccessible(true);
            field.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onHiddenChanged() invoked!!--" + hidden);
    }


    /**
     * 加载控件id
     */
    protected abstract void findViewById();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 其他初始化
     */
    protected abstract void initialize(Bundle savedInstanceState);

    /**
     * 获取备份数据
     */
    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    /**
     * 设置备份数据
     */
    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    public abstract boolean onBackPressed();

    protected abstract void endOperation();

}
