package com.yjt.app.ui.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.yjt.app.R;
import com.yjt.app.constant.Regex;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;


public class RouteDetailActivity extends BaseActivity {

    private DriveRouteResult mResult;
    private TextView tvDistance;
    private TextView tvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        tvDistance = ViewUtil.getInstance().findView(this, R.id.tvDistance);
        tvCost = ViewUtil.getInstance().findView(this, R.id.tvCost);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        if (IntentDataUtil.getInstance().hasIntentExtraValue(this, Temp.ROUTE_INFO.getContent())) {
            mResult = (DriveRouteResult) IntentDataUtil.getInstance().getParcelableData(this, Temp.ROUTE_INFO.getContent());
            DrivePath path = mResult.getPaths().get(0);
            tvDistance.setText(MapUtil.getInstance().getFriendlyTime((int) path.getDuration()) + Regex.LEFT_PARENTHESIS.getRegext() + MapUtil.getInstance().getFriendlyLength((int) path.getDistance()) + Regex.RIGHT_PARENTHESIS.getRegext());
            tvCost.setText("乘出租车约:" + mResult.getTaxiCost() + "元");
        } else {
            ToastUtil.getInstance().showToast(this, getString(R.string.route_prompt3), Toast.LENGTH_SHORT);
        }
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
    protected void permissionRequestIntent() {

    }

    @Override
    protected void permissionRequestResult() {

    }

    @Override
    protected void endOperation() {

    }
}
