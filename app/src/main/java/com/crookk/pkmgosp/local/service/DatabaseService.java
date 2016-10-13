package com.crookk.pkmgosp.local.service;

import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.DatabaseHelper;
import com.crookk.pkmgosp.local.model.Gym;
import com.crookk.pkmgosp.local.model.PokeStop;
import com.crookk.pkmgosp.local.model.Spawn;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@EBean
public class DatabaseService {

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Spawn, Long> mSpawnDao;

    public List<Spawn> queryBoundedSpawn(LatLngBounds bounds) {

        try {

            QueryBuilder<Spawn, Long> builder = mSpawnDao.queryBuilder();

            return builder.where()
                    .le(Spawn.COLUMN_LATITUDE, bounds.northeast.latitude)
                    .and()
                    .le(Spawn.COLUMN_LONGITUDE, bounds.northeast.longitude)
                    .and()
                    .ge(Spawn.COLUMN_LATITUDE, bounds.southwest.latitude)
                    .and()
                    .ge(Spawn.COLUMN_LONGITUDE, bounds.southwest.longitude)
                    .query();

        } catch (SQLException e) {

            LogUtils.error(this, e);
        }
        return new ArrayList<>();
    }

    public void insertSpawn(List<Spawn> spawnList) throws SQLException {

        if (spawnList.size() > 0) {
            mSpawnDao.create(spawnList);
        }
    }
}
