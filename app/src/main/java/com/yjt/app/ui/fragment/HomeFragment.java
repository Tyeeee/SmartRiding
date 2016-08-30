package com.yjt.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.activity.MainActivity;
import com.yjt.app.ui.activity.RouteActivity;
import com.yjt.app.ui.base.BaseFragment;
import com.yjt.app.ui.widget.SearchTextView;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivExchange;

    private SearchTextView stvStartPoint;
    private SearchTextView stvPassPoint;
    private SearchTextView stvEndPoint;

    private ImageView ivAddPassPoint;
    private ImageView ivDeletePassPoint;
    private ImageView ivVoice;

    private TextView tvSearch;

    private String mTempString;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById();
        setListener();
        initialize(savedInstanceState);
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivExchange:
                if (!TextUtils.equals(stvStartPoint.getText(), getString(R.string.start_point)) && !TextUtils.equals(stvEndPoint.getText(), getString(R.string.end_point))) {
                    mTempString = stvStartPoint.getText();
                    stvStartPoint.setText(stvEndPoint.getText());
                    stvEndPoint.setText(mTempString);
                }
                break;
            case R.id.stvStartPoint:
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Temp.POINT_TYPE.getContent(), Constant.PointType.START);
                startActivityForResult(RouteActivity.class, Constant.RequestCode.POINT, bundle1);
                break;
            case R.id.stvPassPoint:
                Bundle bundle2 = new Bundle();
                bundle2.putInt(Temp.POINT_TYPE.getContent(), Constant.PointType.PASS);
                startActivityForResult(RouteActivity.class, Constant.RequestCode.POINT, bundle2);
                break;
            case R.id.stvEndPoint:
                Bundle bundle3 = new Bundle();
                bundle3.putInt(Temp.POINT_TYPE.getContent(), Constant.PointType.END);
                startActivityForResult(RouteActivity.class, Constant.RequestCode.POINT, bundle3);
                break;
            case R.id.ivAddPassPoint:
                ivAddPassPoint.setVisibility(View.GONE);
                ivDeletePassPoint.setVisibility(View.VISIBLE);
                stvPassPoint.setText(R.string.pass_point);
                stvPassPoint.setVisibility(View.VISIBLE);
                stvPassPoint.setLineVisible(View.VISIBLE);
                break;
            case R.id.ivDeletePassPoint:
                ivAddPassPoint.setVisibility(View.VISIBLE);
                ivDeletePassPoint.setVisibility(View.GONE);
                stvPassPoint.setVisibility(View.GONE);
                stvPassPoint.setLineVisible(View.GONE);
                break;
            case R.id.ivVoice:
                SnackBarUtil.getInstance().showSnackBar(mRootView, "ivVoice", Snackbar.LENGTH_SHORT);
                break;
            case R.id.tvSearch:
                if (TextUtils.equals(stvStartPoint.getText(), getString(R.string.start_point))) {
                    SnackBarUtil.getInstance().showSnackBar(mRootView, getString(R.string.start_point), Snackbar.LENGTH_SHORT);
                } else if (TextUtils.equals(stvEndPoint.getText(), getString(R.string.end_point))) {
                    SnackBarUtil.getInstance().showSnackBar(mRootView, getString(R.string.end_point), Snackbar.LENGTH_SHORT);
                } else {
                    startActivity(getActivity(), MainActivity.class);
                }
                break;
        }
    }

    @Override
    protected void findViewById() {
        ivExchange = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.ivExchange, this);

        stvStartPoint = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.stvStartPoint, this);
        stvPassPoint = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.stvPassPoint, this);
        stvEndPoint = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.stvEndPoint, this);
        stvStartPoint.setText(R.string.start_point);
        stvStartPoint.setLineVisible(View.VISIBLE);
        stvEndPoint.setText(R.string.end_point);
        stvEndPoint.setLineVisible(View.GONE);

        ivAddPassPoint = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.ivAddPassPoint, this);
        ivAddPassPoint.setVisibility(View.VISIBLE);
        ivDeletePassPoint = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.ivDeletePassPoint, this);
        ivVoice = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.ivVoice, this);
        tvSearch = ViewUtil.getInstance().findViewAttachOnclick(mRootView, R.id.tvSearch, this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Constant.Common.RESULT_CODE) {
            switch (requestCode) {
                case Constant.RequestCode.POINT:
                    switch (data.getIntExtra(Temp.POINT_TYPE.getContent(), -1)) {
                        case Constant.PointType.START:
                            stvStartPoint.setText(data.getStringExtra(Temp.POINT_CONTENT.getContent()));
                            break;
                        case Constant.PointType.PASS:
                            stvPassPoint.setText(data.getStringExtra(Temp.POINT_CONTENT.getContent()));
                            break;
                        case Constant.PointType.END:
                            stvEndPoint.setText(data.getStringExtra(Temp.POINT_CONTENT.getContent()));
                            break;
                    }
                    break;
            }
        }
    }
}
