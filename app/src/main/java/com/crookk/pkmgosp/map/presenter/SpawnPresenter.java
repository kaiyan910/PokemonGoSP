package com.crookk.pkmgosp.map.presenter;

import com.crookk.pkmgosp.core.presenter.BasePresenter;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.local.model.Spawn;
import com.crookk.pkmgosp.local.service.DatabaseService;
import com.crookk.pkmgosp.map.ui.view.SpawnView;
import com.google.android.gms.maps.model.LatLngBounds;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class SpawnPresenter implements BasePresenter<SpawnView> {

    @Bean
    DatabaseService mDatabaseService;

    private SpawnView mView;

    @Background
    public void getAllSpawnPoint(float zoom, LatLngBounds bounds) {

        if (zoom >= Constant.SPAWN_DISPLAY_ZOOM) {

            List<Spawn> spawnList = mDatabaseService.queryBoundedSpawn(bounds);
            mView.onSpawnListReceived(spawnList);
        }
    }

    @Override
    public void attach(SpawnView view) {
        mView = view;
    }
}
