package com.crookk.pkmgosp.core.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Wrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public Wrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}