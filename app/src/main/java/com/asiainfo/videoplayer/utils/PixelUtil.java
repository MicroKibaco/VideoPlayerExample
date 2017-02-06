package com.asiainfo.videoplayer.utils;

import android.content.Context;
import android.widget.TextView;

import java.util.Locale;

/**
 * 描述:工具类 
 * 创建时间:2/6/17/11:24 作者:小木箱 邮箱:yangzy3@asiainfo.com
 */

public class PixelUtil {

    /**
     * 根据手机的分辨率从dp单位转成为px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px单位转成为dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置时间格式
     *
     * @param tv          TextView
     * @param millisecond 毫秒数
     */
    public static void updateTimeFormat(TextView tv, int millisecond) {
        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        tv.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", hh, mm, ss));
    }

}
