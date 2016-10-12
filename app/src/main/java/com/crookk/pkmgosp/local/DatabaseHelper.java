package com.crookk.pkmgosp.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.crookk.pkmgosp.core.bean.AssetsLoader;
import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.model.Gym;
import com.crookk.pkmgosp.local.model.PokeStop;
import com.crookk.pkmgosp.local.model.Spawn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "pika.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Spawn, Long> spawnDao = null;
    private Dao<PokeStop, String> pokeStopDao = null;
    private Dao<Gym, String> gymDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Spawn.class);
        } catch (SQLException e) {
            LogUtils.error(this, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Spawn, Long> getSpawnDao() throws SQLException {
        if (spawnDao == null) {
            spawnDao = getDao(Spawn.class);
        }
        return spawnDao;
    }

    public Dao<PokeStop, String> getPokeStopDao() throws SQLException {
        if (pokeStopDao == null) {
            pokeStopDao = getDao(PokeStop.class);
        }
        return pokeStopDao;
    }

    public Dao<Gym, String> getGymDao() throws SQLException {
        if (gymDao == null) {
            gymDao = getDao(Gym.class);
        }
        return gymDao;
    }

    @Override
    public void close() {
        super.close();
        spawnDao = null;
        pokeStopDao = null;
        gymDao = null;
    }
}
