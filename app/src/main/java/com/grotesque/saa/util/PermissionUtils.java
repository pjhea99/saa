package com.grotesque.saa.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.ArrayList;

public class PermissionUtils {

    public static final String[] CAMERA_PERMISSION = { "android.permission.CAMERA" };
    public static final int CAMERA_REQUEST_CODE = 208;
    public static final String[] MUST_HAVE_PERMISSIONS;
    public static final int MUST_HAVE_REQUEST_CODE = 207;
    static final String TAG = PermissionUtils.class.getSimpleName();

    static {
        MUST_HAVE_PERMISSIONS = new String[] { "android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE" };
    }

    @Nullable
    public static String[] getNotGrantedPermissions(@NonNull Context context, @NonNull String... paramVarArgs) {
        ArrayList notGrantedPermissions = new ArrayList();

        for (String str : paramVarArgs) {
            if (!hasPermissions(context, str)) notGrantedPermissions.add(str);
        }
        if (notGrantedPermissions.size() > 0)
            return (String[]) notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]);
        return null;
    }

    public static boolean hasCameraPermission(Context context) {
        return hasPermissions(context, CAMERA_PERMISSION);
    }

    public static boolean hasMustHavePermissions(Context context) {
        return hasPermissions(context, MUST_HAVE_PERMISSIONS);
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String... paramVarArgs) {
        if (Build.VERSION.SDK_INT >= 23) for (String str : paramVarArgs) {
            int k = PermissionChecker.checkSelfPermission(context, str);

            if (k == 0) Log.d(TAG, "- PERMISSION_GRANTED : " + str);
            else if (k == -2) {
                Log.d(TAG, "- PERMISSION_DENIED_APP_OP : " + str);
                return false;
            } else if (k == -1) {
                Log.d(TAG, "- PERMISSION_DENIED : " + str);
                return false;
            }
        }
        return true;
    }

    public static boolean hasWindowOverlayPermission(Context paramContext) {
        return (Build.VERSION.SDK_INT < 23) || (Settings.canDrawOverlays(paramContext));
    }
}
