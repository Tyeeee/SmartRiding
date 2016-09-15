package com.yjt.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.PermissionStatus;
import com.yjt.app.permission.PermissionsResultAction;
import com.yjt.app.ui.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 权限管理工具
 *
 * @author yjt
 */
public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();

    private final Set<String>                                  mPendingRequests = new HashSet<>(1);
    private final Set<String>                                  mPermissions     = new HashSet<>(1);
    private final List<WeakReference<PermissionsResultAction>> mPendingActions  = new ArrayList<>(1);

    private static PermissionUtil mPermissionUtil = null;

    public static synchronized PermissionUtil getInstance() {
        if (mPermissionUtil == null) {
            mPermissionUtil = new PermissionUtil();
        }
        return mPermissionUtil;
    }

    public static void releaseInstance() {
        if (mPermissionUtil != null) {
            mPermissionUtil = null;
        }
    }

    private PermissionUtil() {
        initializePermissionsMap();
    }

    private synchronized void initializePermissionsMap() {
        Field[] fields = Manifest.permission.class.getFields();
        for (Field field : fields) {
            String name = null;
            try {
                name = (String) field.get("");
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Could not access field", e);
            }
            mPermissions.add(name);
        }
    }

    @NonNull
    public synchronized String[] getManifestPermissions(@NonNull final Activity activity) {
        PackageInfo  packageInfo = null;
        List<String> list        = new ArrayList<>(1);
        try {
            Log.d(TAG, activity.getPackageName());
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String permission : permissions) {
                    LogUtil.print("Manifest contained permission:" + permission);
                    list.add(permission);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    private synchronized void addPendingAction(@NonNull String[] permissions,
                                               @Nullable PermissionsResultAction action) {
        if (action == null) {
            return;
        }
        action.registerPermissions(permissions);
        mPendingActions.add(new WeakReference<>(action));
    }

    private synchronized void removePendingAction(@Nullable PermissionsResultAction action) {
        for (Iterator<WeakReference<PermissionsResultAction>> iterator = mPendingActions.iterator();
             iterator.hasNext(); ) {
            WeakReference<PermissionsResultAction> weakRef = iterator.next();
            if (weakRef.get() == action || weakRef.get() == null) {
                iterator.remove();
            }
        }
    }

    public synchronized boolean hasPermission(@Nullable Context context, @NonNull String permission) {
        return context != null && (ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED || !mPermissions.contains(permission));
    }

    public synchronized boolean hasAllPermissions(@Nullable Context context, @NonNull String... permissions) {
        if (context == null) {
            return false;
        }
        boolean hasAllPermissions = true;
        for (String ermission : permissions) {
            hasAllPermissions &= hasPermission(context, ermission);
        }
        return hasAllPermissions;
    }

    public synchronized void requestAllManifestPermissionsIfNecessary(final @Nullable Activity activity,
                                                                      final @Nullable PermissionsResultAction action) {
        if (activity == null) {
            return;
        }
        String[] perms = getManifestPermissions(activity);
        requestPermissionsIfNecessaryForResult(activity, action, perms);
    }

    public synchronized void requestPermissionsIfNecessaryForResult(@Nullable Activity activity,
                                                                    @Nullable PermissionsResultAction action,
                                                                    @NonNull String... permissions) {
        if (activity == null) {
            return;
        }
        addPendingAction(permissions, action);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            doPermissionWorkBeforeAndroidM(activity, permissions, action);
        } else {
            List<String> permList = getPermissionsListToRequest(activity, permissions, action);
            if (permList.isEmpty()) {
                //if there is no permission to request, there is no reason to keep the action int the list
                removePendingAction(action);
            } else {
                String[] permsToRequest = permList.toArray(new String[permList.size()]);
                mPendingRequests.addAll(permList);
                ActivityCompat.requestPermissions(activity, permsToRequest, Constant.PERMISSION_REQUEST_CODE);
            }
        }
    }

    public synchronized void requestPermissionsIfNecessaryForResult(@NonNull Fragment fragment,
                                                                    @Nullable PermissionsResultAction action,
                                                                    @NonNull String... permissions) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return;
        }
        addPendingAction(permissions, action);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            doPermissionWorkBeforeAndroidM(activity, permissions, action);
        } else {
            List<String> permList = getPermissionsListToRequest(activity, permissions, action);
            if (permList.isEmpty()) {
                //if there is no permission to request, there is no reason to keep the action int the list
                removePendingAction(action);
            } else {
                String[] permsToRequest = permList.toArray(new String[permList.size()]);
                mPendingRequests.addAll(permList);
                fragment.requestPermissions(permsToRequest, Constant.PERMISSION_REQUEST_CODE);
            }
        }
    }

    public synchronized void notifyPermissionsChange(@NonNull String[] permissions, @NonNull int[] results) {
        int size = permissions.length;
        if (results.length < size) {
            size = results.length;
        }
        Iterator<WeakReference<PermissionsResultAction>> iterator = mPendingActions.iterator();
        while (iterator.hasNext()) {
            PermissionsResultAction action = iterator.next().get();
            for (int n = 0; n < size; n++) {
                if (action == null || action.onResult(permissions[n], results[n])) {
                    iterator.remove();
                    break;
                }
            }
        }
        for (int n = 0; n < size; n++) {
            mPendingRequests.remove(permissions[n]);
        }
    }

    private void doPermissionWorkBeforeAndroidM(@NonNull Activity activity,
                                                @NonNull String[] permissions,
                                                @Nullable PermissionsResultAction action) {
        for (String perm : permissions) {
            if (action != null) {
                if (!mPermissions.contains(perm)) {
                    action.onResult(perm, PermissionStatus.NOT_FOUND);
                } else if (ActivityCompat.checkSelfPermission(activity, perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    action.onResult(perm, PermissionStatus.DENIED);
                } else {
                    action.onResult(perm, PermissionStatus.GRANTED);
                }
            }
        }
    }

    @NonNull
    private List<String> getPermissionsListToRequest(@NonNull Activity activity,
                                                     @NonNull String[] permissions,
                                                     @Nullable PermissionsResultAction action) {
        List<String> permList = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (!mPermissions.contains(permission)) {
                if (action != null) {
                    action.onResult(permission, PermissionStatus.NOT_FOUND);
                }
            } else if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (!mPendingRequests.contains(permission)) {
                    permList.add(permission);
                }
            } else {
                if (action != null) {
                    action.onResult(permission, PermissionStatus.GRANTED);
                }
            }
        }
        return permList;
    }

    /*********************************/

    public boolean isCheckPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void checkPermissions(final FragmentActivity activity, final int requestCode, final String... permissions) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        SnackBarUtil.getInstance().showSnackBar(activity
                                , activity.getString(R.string.prompt_permission)
                                , Snackbar.LENGTH_INDEFINITE
                                , activity.getString(R.string.see)
                                , new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat
                                                .requestPermissions(activity, permissions, requestCode);
                                    }
                                });
                    } else {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                }
            }
        }
    }

    public void checkPermission(final BaseActivity activity, final int requestCode, final String... permission) {
        checkPermissions(activity, requestCode, permission);
    }

    public boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void grantWritePermission(Activity activity, Intent intent, Uri uri) {
        if (uri != null) {
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                activity.grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    public void revokeWritePermission(Activity activity, Uri uri) {
        if (uri != null) {
            activity.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}
