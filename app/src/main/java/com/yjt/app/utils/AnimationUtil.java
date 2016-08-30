package com.yjt.app.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 动画特效工具
 *
 * @author yjt
 */
public class AnimationUtil {

    private final int Default = -1;

    private static AnimationUtil mAnimationUtil;

    private AnimationUtil() {
        // cannot be instantiated
    }

    public static synchronized AnimationUtil getInstance() {
        if (mAnimationUtil == null) {
            mAnimationUtil = new AnimationUtil();
        }
        return mAnimationUtil;
    }

    public static void releaseInstance() {
        if (mAnimationUtil != null) {
            mAnimationUtil = null;
        }
    }

    private class CustomerAnimationListener implements AnimationListener {

        private View view;

        public CustomerAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ViewUtil.getInstance().setViewGone(view);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

    private void setEffect(Animation animation, int interpolatorType,
                           long durationMillis, long delayMillis) {
        switch (interpolatorType) {
            case 0:
                animation.setInterpolator(new LinearInterpolator());
                break;
            case 1:
                animation.setInterpolator(new AccelerateInterpolator());
                break;
            case 2:
                animation.setInterpolator(new DecelerateInterpolator());
                break;
            case 3:
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case 4:
                animation.setInterpolator(new BounceInterpolator());
                break;
            case 5:
                animation.setInterpolator(new OvershootInterpolator());
                break;
            case 6:
                animation.setInterpolator(new AnticipateInterpolator());
                break;
            case 7:
                animation.setInterpolator(new AnticipateOvershootInterpolator());
                break;
            default:
                break;
        }
        animation.setDuration(durationMillis);
        animation.setStartOffset(delayMillis);
    }

    public void baseIn(View view, Animation animation, long durationMillis,
                       long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        ViewUtil.getInstance().setViewVisible(view);
        view.startAnimation(animation);
    }

    public void baseOut(View view, Animation animation, long durationMillis,
                        long delayMillis) {
        setEffect(animation, Default, durationMillis, delayMillis);
        animation.setAnimationListener(new CustomerAnimationListener(view));
        view.startAnimation(animation);
    }

    public void show(View view) {
        ViewUtil.getInstance().setViewVisible(view);
    }

    public void hide(View view) {
        ViewUtil.getInstance().setViewGone(view);
    }

    public void transparent(View view) {
        ViewUtil.getInstance().setViewInvisible(view);
    }

    public void fadeIn(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        baseIn(view, animation, durationMillis, delayMillis);
    }

    public void fadeOut(View view, long durationMillis, long delayMillis) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        baseOut(view, animation, durationMillis, delayMillis);
    }

}
