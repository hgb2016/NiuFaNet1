package com.dream.NiuFaNet.Utils;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.dream.NiuFaNet.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/10/20 0020.
 */
public class ToSettingPageUtil {
    public static boolean gotoPermissionSetting() {
        boolean success = true;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String packageName = LshAppUtils.getPackageName();

        LshOSUtils.ROM romType = LshOSUtils.getRomType();
        switch (romType) {
            case EMUI: // 华为
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
                break;
            case Flyme: // 魅族
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", packageName);
                break;
            case MIUI: // 小米
                String rom = getMiuiVersion();
                if ("V6".equals(rom) || "V7".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else if ("V8".equals(rom) || "V9".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else {
                    intent = getAppDetailsSettingsIntent(packageName);
                }
                break;
            case Sony: // 索尼
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
                break;
            case ColorOS: // OPPO
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
                ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
                intent.setComponent(comp);
                break;
            case EUI: // 乐视
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps"));
                break;
            case LG: // LG
                intent.setAction("android.intent.action.MAIN");
                intent.putExtra("packageName", packageName);
                ComponentName comp1 = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                intent.setComponent(comp1);
                break;
            case SamSung: // 三星
                intent =  new Intent(Settings.ACTION_SETTINGS);
                break;
            case SmartisanOS: // 锤子
                gotoAppDetailSetting(packageName);
                break;
            default:
                intent.setAction(Settings.ACTION_SETTINGS);
                Log.e("tag","没有适配该机型, 跳转普通设置界面");
                success = false;
                break;
        }
        try {
            LshContextUtils.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            // 跳转失败, 前往普通设置界面
            LshContextUtils.startActivity(getSettingIntent());
            success = false;
            Log.e("tag","无法跳转权限界面, 开始跳转普通设置界面");
        }
        return success;
    }
    /**
     * 获取跳转「设置界面」的意图
     */
    public static Intent getSettingIntent() {
        return new Intent(Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("tag","MiuiVersion = " + line);
        return line;
    }

    /**
     * 获取跳转「应用详情」的意图
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + packageName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void gotoAppDetailSetting(String packageName) {
        LshContextUtils.startActivity(getAppDetailsSettingsIntent(packageName));
    }
}
