package com.example.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import com.morgoo.droidplugin.pm.PluginManager;

/**
 * Created by Admin on 2017/11/5.
 */

public class PluginUtils {


    /**
     * 启动插件
     *
     * @param activity
     * @param packageName
     */
    public static void startActivity(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 删除apk
     *
     * @param activity
     * @param packageName
     */
    public static void unInstallApk(Activity activity, String packageName) {
        if (!PluginManager.getInstance().isConnected()) {
            ToastUtil.showShort(activity, "服务未连接");
        } else {
            try {
                PluginManager.getInstance().deletePackage(packageName, 0);
                ToastUtil.showShort(activity, "删除完成");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 应该在线程中安装，此方法仅供测试
     *
     * @param context
     * @param apkPathAndName
     * @param packageName
     */
    public static boolean installApk(Context context, String apkPathAndName, String packageName) {
        if (!PluginManager.getInstance().isConnected()) {
            ToastUtil.showShort(context, "插件服务正在初始化，请稍后再试。。。");
            return false;
        }
        try {
            if (PluginManager.getInstance().getPackageInfo(packageName, 0) != null) {
                ToastUtil.showShort(context, "已经安装了，不能再安装");
                return true;
            } else {
                //如果需要更新插件，则flag 设置为 PackageManagerCompat.INSTALL_REPLACE_EXISTING
                //int returnCode = PluginManager.getInstance().installPackage(filepath, PackageManagerCompat.INSTALL_REPLACE_EXISTING);
                int returnCode = PluginManager.getInstance().installPackage(apkPathAndName, 0);
                if (returnCode == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION) {
                    //安装失败，文件请求的权限太多
                    ToastUtil.showShort(context, "安装失败，文件请求的权限太多");
                } else {
                    //安装完成
                    ToastUtil.showShort(context, "安装完成");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }
}
