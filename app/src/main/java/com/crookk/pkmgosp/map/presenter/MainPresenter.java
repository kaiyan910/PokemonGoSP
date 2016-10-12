package com.crookk.pkmgosp.map.presenter;

import android.content.Context;

import com.crookk.pkmgosp.core.bean.AssetsLoader;
import com.crookk.pkmgosp.core.presenter.BasePresenter;
import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.local.model.Spawn;
import com.crookk.pkmgosp.local.service.DatabaseService;
import com.crookk.pkmgosp.map.ui.view.MainView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@EBean
public class MainPresenter implements BasePresenter<MainView> {

    @RootContext
    Context mContext;

    @Bean
    DatabaseService mDatabaseService;

    private MainView mView;

    @Background
    public void insertDataFromJson() {

        try {

            insert("data_" + Constant.DATA_VERSION + ".json");
            mView.onDatabaseInserted();

        } catch (SQLException e) {

            LogUtils.error(this, e);
            mView.onError(1);
        }
    }

    @Override
    public void attach(MainView view) {
        this.mView = view;
    }

    private void insert(String jsonFile) throws SQLException {

        String json = AssetsLoader.load(mContext, jsonFile);

        if (json != null) {

            List<Spawn> data = new Gson().fromJson(json, new TypeToken<List<Spawn>>() {
            }.getType());
            mDatabaseService.insertSpawn(data);
        }
    }
}
