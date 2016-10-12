package com.crookk.pkmgosp.local;

import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constant {

    public static final float SPAWN_DISPLAY_ZOOM = 16.0F;
    public static final Long SPAWN_SOON_RANGE = 5 * 60 * 1000L;

    public static final int DATA_VERSION = 1;

    public static final SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final SparseArray<String> SPAWN_TYPE = new SparseArray<>();

    static {

        SPAWN_TYPE.append(101, "1x15");
        SPAWN_TYPE.append(102, "1x30");
        SPAWN_TYPE.append(103, "1x45");
        SPAWN_TYPE.append(104, "1x60");
        SPAWN_TYPE.append(201, "2x15");
        SPAWN_TYPE.append(202, "1x60h2");
        SPAWN_TYPE.append(203, "1x60h3");
        SPAWN_TYPE.append(204, "1x60h23");
    }
}
