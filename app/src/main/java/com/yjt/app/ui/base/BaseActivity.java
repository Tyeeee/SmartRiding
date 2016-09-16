package com.yjt.app.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.Window;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.utils.ActivityUtil;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.PermissionUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar tbTitle;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onWindowFocusChanged() invoked!!");
        if (SnackBarUtil.getInstance().isShown()) {
            ViewUtil.getInstance().setSystemUiVisibility(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onCreate() invoked!!");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityUtil.add(this);
        LogUtil.print(getClass().getSimpleName() + ":" + getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName() + " onStart() invoked!!");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName() + " onPostCreate() invoked!!");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onRestart() invoked!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onResume() invoked!!");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onPostResume() invoked!!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        setSavedInstanceState(outState);
        super.onSaveInstanceState(outState);
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onSaveInstanceState() invoked!!");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onRestoreInstanceState() invoked!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName() + " onPause() invoked!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName() + " onStop() invoked!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("---->" + getClass().getName(), this.getClass().getSimpleName()
                + " onDestroy() invoked!!");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                InputUtil.getInstance().hideKeyBoard(event, this);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onFinish("onBackPressed");
    }

    public void onFinish(String message) {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        LogUtil.print("---->onFinish is called: " + message);
    }

    protected abstract void findViewById();

    protected abstract void setViewListener();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    protected abstract void permissionRequestIntent();

    protected abstract void permissionRequestResult();

    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    protected void startActivity(String act) {
        startActivity(act, null);
    }

    protected void startActivity(Class<?> cls, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(String act, Bundle mBundle) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void permissionRequest(String... permissions) {
        LogUtil.print("---->" + permissions);
        if (PermissionUtil.getInstance().isCheckPermission()) {
            if (PermissionUtil.getInstance().hasAllPermissions(this, permissions)) {
                LogUtil.print("---->hasAllPermissions:" + permissions);
                permissionRequestIntent();
            } else {
                LogUtil.print("---->requestPermissions:" + permissions);
                PermissionUtil.getInstance().requestPermissionsIfNecessaryForResult(this, null, permissions);
            }
        } else {
            permissionRequestIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    LogUtil.print("---->permission:" + permissions[i] + ",result:" + grantResults[i]);
                }
                if (PermissionUtil.getInstance().verifyPermissions(grantResults)) {
                    permissionRequestResult();
                } else {
                    List<String> surplus = new ArrayList<>();
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != 0) {
                            surplus.add(permissions[i]);
                            LogUtil.print("---->denied permission:" + permissions[i] + ",result:" + grantResults[i]);
                        }
                    }
                    PermissionUtil.getInstance().checkPermissions(this, Constant.PERMISSION_REQUEST_CODE, surplus.toArray(new String[surplus.size()]));
                }
                PermissionUtil.getInstance().notifyPermissionsChange(permissions, grantResults);
                break;
            default:
                break;
        }
    }
}

