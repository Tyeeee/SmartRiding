package com.yjt.app.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;

/**
 * View处理
 *
 * @author yjt
 */
public class ViewUtil {

    private static ViewUtil mViewUtil;

    private ViewUtil() {
        // cannot be instantiated
    }

    public static synchronized ViewUtil getInstance() {
        if (mViewUtil == null) {
            mViewUtil = new ViewUtil();
        }
        return mViewUtil;
    }

    public static void releaseInstance() {
        if (mViewUtil != null) {
            mViewUtil = null;
        }
    }

    /**
     * 设置View背景
     *
     * @param view
     * @param background
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setViewBackground(View view, Drawable background) {
        if (VersionUtil.getInstance().isJellyBean()) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public void setViewBackground(View view, int drawableId) {
        Drawable drawable = view.getContext().getResources().getDrawable(drawableId);
        setViewBackground(view, drawable);
    }

    /**
     * 判断view是否显示
     *
     * @param view
     *
     * @return
     */
    public boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置View为Visible
     *
     * @param view
     */
    public void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 设置View为Gone
     *
     * @param view
     */
    public void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * 设置View为Invisible
     *
     * @param view
     */
    public void setViewInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    /***
     * 释放ImageView图片资源
     *
     * @param imageView
     */
    public void releaseImageDrawable(ImageView imageView) {
        if (imageView == null)
            return;
        Drawable d = imageView.getDrawable();
        if (d != null)
            d.setCallback(null);
        imageView.setImageDrawable(null);
        setViewBackground(imageView, null);
    }

    /**
     * 释放view背景资源
     *
     * @param view
     */
    public void releaseLayoutDrawable(View view) {
        if (view == null)
            return;
        Drawable d = view.getBackground();
        if (d != null)
            d.setCallback(null);
        setViewBackground(view, null);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param activity
     * @param resId
     *
     * @return
     */
    public <V> V findView(Activity activity, @IdRes int resId) {
        return (V) activity.findViewById(resId);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param rootView
     * @param <V>具体的View类型
     *
     * @return
     */
    public <V> V findView(View rootView, @IdRes int resId) {
        //noinspection unchecked
        return (V) rootView.findViewById(resId);
    }

    /**
     * findviewById 并添加点击事件
     *
     * @param activity
     * @param resId
     * @param onClickListener
     * @param <V>具体的View类型
     *
     * @return
     */
    public <V> V findViewAttachOnclick(Activity activity, @IdRes int resId, View.OnClickListener onClickListener) {
        View view = activity.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }

    /**
     * findviewById 并添加点击事件
     *
     * @param rootView
     * @param resId
     * @param onClickListener
     * @param <V>具体的View类型
     *
     * @return
     */
    public <V> V findViewAttachOnclick(View rootView, @IdRes int resId, View.OnClickListener onClickListener) {
        //noinspection unchecked
        View view = rootView.findViewById(resId);
        view.setOnClickListener(onClickListener);
        //noinspection unchecked
        return (V) view;
    }


    /**
     * RecyclerView 是否滚动到顶部
     *
     * @param recyclerView
     *
     * @return
     */
    public boolean isScrollTop(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getChildCount() > 0) {
            if (recyclerView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ListView 是否滚动到顶部
     *
     * @param listView
     *
     * @return
     */
    public boolean isScrollTop(ListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            if (listView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ExpandableListView 是否滚动到顶部
     *
     * @param listView
     *
     * @return
     */
    public boolean isScrollTop(ExpandableListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            if (listView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ScrollView 是否滚动到顶部
     *
     * @param scrollView
     *
     * @return
     */
    public boolean isScrollTop(ScrollView scrollView) {
        if (scrollView != null) {
            if (scrollView.getScrollY() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 控制view的Visible与Gone
     *
     * @param view
     * @param show
     */
    public void toggleView(View view, boolean show) {
        if (show) {
            setViewVisible(view);
        } else {
            setViewGone(view);
        }
    }

    public void setText(TextView textView, String content, int imageId, int width, int height, int position, boolean clickable) {
        textView.setText(content);
        if (imageId != -1) {
            Drawable drawable = BaseApplication.getInstance().getResources().getDrawable(imageId);
            drawable.setBounds(0, 0, width, height);
            switch (position) {
                case Constant.DRAWABLE_TOP:
                    textView.setCompoundDrawables(null, drawable, null, null);
                    break;
                case Constant.DRAWABLE_LEFT:
                    textView.setCompoundDrawables(drawable, null, null, null);
                    break;
                case Constant.DRAWABLE_RIGHT:
                    textView.setCompoundDrawables(null, null, drawable, null);
                    break;
                case Constant.DRAWABLE_BOTTOM:
                    textView.setCompoundDrawables(null, null, null, drawable);
                    break;
                default:
                    textView.setCompoundDrawables(drawable, null, null, null);
                    break;
            }
        }
        textView.setClickable(clickable);
    }

    public AlertDialog showAlertDialog(Activity activity, String title, String message,
                                       String posLabel, String negLabel,
                                       DialogInterface.OnClickListener posListener,
                                       DialogInterface.OnClickListener negListener,
                                       DialogInterface.OnDismissListener canListener) {
        if (activity != null && !activity.isFinishing()) {
            Dialog dialog = new AlertDialog.Builder(activity).setTitle(title)
                    .setMessage(message).setPositiveButton(posLabel, posListener)
                    .setNegativeButton(negLabel, negListener).show();
            if (canListener != null) {
                dialog.setOnDismissListener(canListener);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return (AlertDialog) dialog;
        } else {
            return null;
        }
    }

    protected AlertDialog showAlertDialog(Activity activity, View view, String posLabel,
                                          String negLabel, DialogInterface.OnClickListener posListener,
                                          DialogInterface.OnClickListener negListener,
                                          DialogInterface.OnDismissListener canListener) {
        if (activity != null && !activity.isFinishing()) {
            if (view != null) {
                ViewGroup group = (ViewGroup) view.getParent();
                if (group != null) {
                    group.removeAllViews();
                }
            }
            Dialog dialog = new AlertDialog.Builder(activity).setView(view)
                    .setPositiveButton(posLabel, posListener)
                    .setNegativeButton(negLabel, negListener).show();
            if (canListener != null) {
                dialog.setOnDismissListener(canListener);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            return (AlertDialog) dialog;
        } else {
            return null;
        }
    }

    public ProgressDialog showProgressDialog(Activity activity, String title,
                                             String message, DialogInterface.OnCancelListener cancelListener) {
        if (activity != null && !activity.isFinishing()) {
            lockScreenOrientation(activity);
            Dialog dialog = ProgressDialog.show(activity, title, message, true,
                                                true, cancelListener);
            dialog.setCanceledOnTouchOutside(false);
            // dialog.setCancelable(false);
            return (ProgressDialog) dialog;
        } else {
            return null;
        }
    }

    public void hideDialog(Dialog dialog, Activity activity) {
        if (dialog != null && dialog.isShowing() && activity != null && !activity.isFinishing()) {
            dialog.dismiss();
            unLockScreenOrientation(activity);
        }
    }

    public void lockScreenOrientation(Activity activity) {
        Configuration newConfig = activity.getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (newConfig.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
            // 键盘没关闭。屏幕方向为横屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (newConfig.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
            // 键盘关闭。屏幕方向为竖屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void unLockScreenOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    /**
     * 设置 dragTopLayout 是否能拖动
     *
     * @param dragTopLayout
     * @param recyclerView
     */
//    public void setTouchMode(DragTopLayout dragTopLayout, RecyclerView recyclerView) {
//        dragTopLayout.setTouchMode(isScrollTop(recyclerView));
//        DragTopLayout.PanelState state = dragTopLayout.getState();
//        if (state == DragTopLayout.PanelState.EXPANDED) {
//            if (!isScrollTop(recyclerView))
//                dragTopLayout.setTouchMode(true);
//        }
//    }

//    public void setTouchMode(DragTopLayout dragTopLayout, ListView listView) {
//        dragTopLayout.setTouchMode(isScrollTop(listView));
//        DragTopLayout.PanelState state = dragTopLayout.getState();
//        if (state == DragTopLayout.PanelState.EXPANDED) {
//            if (!isScrollTop(listView))
//                dragTopLayout.setTouchMode(true);
//        }
//    }

//    public void setTouchMode(DragTopLayout dragTopLayout, ExpandableListView listView) {
//        dragTopLayout.setTouchMode(isScrollTop(listView));
//        DragTopLayout.PanelState state = dragTopLayout.getState();
//        if (state == DragTopLayout.PanelState.EXPANDED) {
//            if (!isScrollTop(listView))
//                dragTopLayout.setTouchMode(true);
//        }
//    }

//    public void setTouchMode(DragTopLayout dragTopLayout, ScrollView scrollView) {
//        dragTopLayout.setTouchMode(isScrollTop(scrollView));
//        DragTopLayout.PanelState state = dragTopLayout.getState();
//        if (state == DragTopLayout.PanelState.EXPANDED) {
//            if (!isScrollTop(scrollView))
//                dragTopLayout.setTouchMode(true);
//        }
//    }

}
