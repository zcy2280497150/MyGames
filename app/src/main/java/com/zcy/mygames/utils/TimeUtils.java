package com.zcy.mygames.utils;

public class TimeUtils {

    public static final int DJS_FORMAT_TYPE_DEFAULT = 1000;//倒计时 默认不带毫秒 显示
    public static final int DJS_FORMAT_TYPE_MS = 1001;//倒计时 带毫秒 显示
    public static String formatTimeDJS(long time){
        return formatTimeDJS(time,DJS_FORMAT_TYPE_DEFAULT);
    }
    public static String formatTimeDJS(long time,int formatType){
        if (time < 0)return "--";
        StringBuilder sb = new StringBuilder();
        long ms = time % 1000L;
        long s = time / 1000L;
        if (s < 60L ){
            sb.append(s);
        }else {
            long m = s / 60L;
            s = s % 60L;
            sb.append(s < 10 ? "0" + s : s);
            if (m < 60L){
                sb.insert(0,":").insert(0,m);
            }else {
                long h = m / 60L;
                m = m % 60L;
                sb.insert(0,":").insert(0,m < 10 ? "0" + m : m);
                sb.insert(0,":").insert(0,h < 10 ? "0" + h : h);
            }
        }
        if (DJS_FORMAT_TYPE_MS == formatType){
            sb.append(" ");
            if (ms <= 0){
                sb.append("000");
            }else if (ms < 10){
                sb.append("00").append(ms);
            }else if (ms < 100){
                sb.append("0").append(ms);
            }else {
                sb.append(ms);
            }
        }
        return sb.toString();
    }

}
