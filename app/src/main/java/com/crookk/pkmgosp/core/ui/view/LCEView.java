package com.crookk.pkmgosp.core.ui.view;

import java.util.List;

public interface LCEView<M> extends BaseView {

    void showLoading();
    void showData();
    void showEmptyData();

    void setData(List<M> data);
}
