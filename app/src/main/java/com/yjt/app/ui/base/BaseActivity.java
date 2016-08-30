package com.yjt.app.ui.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;

import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.utils.ActivityUtil;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.ViewUtil;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onWindowFocusChanged() invoked!!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onCreate() invoked!!");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityUtil.add(this);
        LogUtil.print(getClass().getSimpleName() + ":" + getTaskId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName() + " onStart() invoked!!");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName() + " onPostCreate() invoked!!");
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
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onResume() invoked!!");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onPostResume() invoked!!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        setSavedInstanceState(outState);
        super.onSaveInstanceState(outState);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onSaveInstanceState() invoked!!");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onRestoreInstanceState() invoked!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName() + " onPause() invoked!!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName() + " onStop() invoked!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
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

    protected void onFinish(String message) {
        super.finish();
        LogUtil.print("onFinish is called: " + message);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    protected abstract void findViewById();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    protected abstract void permissionRequestIntent();

    protected abstract void permissionRequestResult();

    protected abstract void endOperation();

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

    protected void showExitDialog() {
        ViewUtil.getInstance().showAlertDialog(this, getString(R.string.prompt_title), getString(R.string.exit_prompt_content), getString(R.string.enter), getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                endOperation();
                BaseApplication.getInstance().releaseReference();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, null);
    }
}

