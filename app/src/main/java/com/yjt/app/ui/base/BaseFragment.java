package com.yjt.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjt.app.R;
import com.yjt.app.utils.LogUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

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

    protected abstract void findViewById();

    protected abstract void setViewListener();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    public abstract boolean onBackPressed();

    protected abstract void endOperation();

    protected void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    protected void startActivity(String act) {
        startActivity(act, null);
    }

    protected void startActivity(String act, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in,
                                                R.anim.slide_left_out);
    }

    protected void startActivity(Context context, Class<?> cls, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in,
                                                R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    protected void startActivityForResult(String act, int requestCode) {
        startActivityForResult(act, requestCode, null);
    }

    protected void startActivityForResult(String act, int requestCode,
                                          Bundle mBundle) {
        startActivityForResult(act, null, null, requestCode, mBundle);
    }

    protected void startActivityForResult(String act, Uri data, int requestCode) {
        startActivityForResult(act, data, null, requestCode, null);
    }

    protected void startActivityForResult(String act, String type,
                                          int requestCode, Bundle mBundle) {
        startActivityForResult(act, null, type, requestCode, mBundle);
    }

    protected void startActivityForResultWithParcelable(String act, HashMap<String, Parcelable> map,
                                                        int requestCode) {
        startActivityForResultWithParcelable(act, null, null, map, requestCode);
    }

    protected void startActivityForResultWithParcelable(String act, Uri data, String type, HashMap<String, Parcelable> map,
                                                        int requestCode) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        intent.setDataAndType(data, type);
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResultWithSerializable(String act, HashMap<String, Serializable> map,
                                                          int requestCode) {
        startActivityForResultWithSerializable(act, null, null, map, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResultWithSerializable(String act, Uri data, String type, HashMap<String, Serializable> map,
                                                          int requestCode) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        intent.setDataAndType(data, type);
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(String act, Uri data, String type,
                                          int requestCode, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setAction(act);
        intent.setDataAndType(data, type);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode,
                                          Bundle mBundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

}
