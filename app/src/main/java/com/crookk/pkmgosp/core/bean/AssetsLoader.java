package com.crookk.pkmgosp.core.bean;

import android.content.Context;
import android.util.Log;

import com.crookk.pkmgosp.core.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;

public class AssetsLoader {

    public static String load(Context context, String name) {

        String json = null;

        try {

            InputStream stream = context.getAssets().open(name);

            int size = stream.available();

            byte[] buffer = new byte[size];

            int result = stream.read(buffer);

            json = new String(buffer);

            stream.close();

        } catch (IOException e) {

            LogUtils.error(new AssetsLoader(), e);
            return null;
        }

        return json;
    }
}
