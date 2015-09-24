package com.www.goumei.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;


/**
 * UI界面相关工具�?
 */
public class UIUtils {
    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5);
    }

    /**
     * px转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }

    /**
     * 用xml布局文件填充布局
     *
     * @param resId
     * @return
     */
    public static View inflate( Context   context,int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }
    /**
     * 获取颜色
     */
    public static int getColor(Context   context,int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * 获取资源
     */
    public static Resources getResources(Context  context) {

        return context.getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(Context context,int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(Context  context,int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * 获得图片资源
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawable(Context context,int resId) {
        return  context.getResources().getDrawable(resId);
    }
}
