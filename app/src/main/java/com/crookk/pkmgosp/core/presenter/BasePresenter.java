package com.crookk.pkmgosp.core.presenter;

import com.crookk.pkmgosp.core.ui.view.BaseView;

public interface BasePresenter<V extends BaseView> {

    void attach(V view);
}
