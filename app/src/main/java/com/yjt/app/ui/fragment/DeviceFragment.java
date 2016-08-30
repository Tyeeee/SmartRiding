package com.yjt.app.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjt.app.R;
import com.yjt.app.ui.base.BaseFragment;

public class DeviceFragment extends BaseFragment {


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_device, container, false);
        findViewById();
        setListener();
        initialize(savedInstanceState);
        return mRootView;
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

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
}
