package com.crookk.pkmgosp.core.utils;

import android.util.Log;

import com.crookk.pkmgosp.BuildConfig;

public class LogUtils {

    public static void debug(Object object, String message, Object... args) {
        if(BuildConfig.DEBUG) {
            Log.d(object.getClass().getSimpleName(), String.format(message, args));
        }
    }

    public static void error(Object object, Exception exception) {
        if(BuildConfig.DEBUG) {
            Log.e(object.getClass().getSimpleName(), exception.getMessage(), exception);
        }
    }
}
