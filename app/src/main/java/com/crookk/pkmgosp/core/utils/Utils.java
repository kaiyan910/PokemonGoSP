package com.crookk.pkmgosp.core.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

    public static int[] calculateScreenSize(WindowManager manager) {

        Display display = manager.getDefaultDisplay();
        Point size      = new Point();

        display.getSize(size);

        return new int[]{ size.x, size.y };
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
