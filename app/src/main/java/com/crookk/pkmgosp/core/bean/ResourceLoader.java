package com.crookk.pkmgosp.core.bean;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class ResourceLoader {

    @RootContext
    Context context;

    public int getDrawableResources(String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public String getStringResources(String name) {
        return context.getResources().getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
    }
}
