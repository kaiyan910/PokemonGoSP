package com.crookk.pkmgosp.local.model;

import android.content.Context;

import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "spawn")
public class Spawn {

    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    @SerializedName("id")
    @DatabaseField(id = true)
    private String id;
    @SerializedName("lat")
    @DatabaseField(columnName = COLUMN_LATITUDE)
    private double latitude;
    @SerializedName("lng")
    @DatabaseField(columnName = COLUMN_LONGITUDE)
    private double longitude;
    @SerializedName("type")
    @DatabaseField
    private int type;
    @SerializedName("spawntime")
    @DatabaseField
    private int spawnTime;
    @SerializedName("pausetime")
    @DatabaseField
    private int pauseTime;

    private Calendar nextOrCurrentSpawn;
    private Calendar nextOrCurrentSpawnEnd;

    public enum SpawnStatus {
        SPAWN, DESPAWN, SPAWN_SOON
    }

    private SpawnStatus spawnStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(int spawnTime) {
        this.spawnTime = spawnTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public Calendar getNextOrCurrentSpawnEnd() {
        return nextOrCurrentSpawnEnd;
    }

    public Calendar getNextOrCurrentSpawn() {
        return nextOrCurrentSpawn;
    }

    public SpawnStatus getSpawnStatus() {
        return spawnStatus;
    }

    public SpawnStatus checkSpawnStatus() {

        // do update nextOrCurrentSpawn && nextOrCurrentSpawnEnd
        updateSpawnStatus();

        if (isActive()) {
            spawnStatus = SpawnStatus.SPAWN;
        } else if (isSpawnSoon(Constant.SPAWN_SOON_RANGE)) {
            spawnStatus = SpawnStatus.SPAWN_SOON;
        } else {
            spawnStatus = SpawnStatus.DESPAWN;
        }

        return spawnStatus;
    }

    private void updateSpawnStatus() {

        Calendar now = Calendar.getInstance();

        if (nextOrCurrentSpawnEnd == null || now.getTimeInMillis() > nextOrCurrentSpawnEnd.getTimeInMillis()) {

            int minutes = (spawnTime / 1000) / 60;
            int seconds = (spawnTime / 1000) % 60;

            // get current hour spawn time
            Calendar currentHourSpawn = Calendar.getInstance();
            currentHourSpawn.set(Calendar.MINUTE, minutes);
            currentHourSpawn.set(Calendar.SECOND, seconds);

            Calendar currentHourSpawnEnd = (Calendar) currentHourSpawn.clone();
            currentHourSpawnEnd.add(Calendar.MILLISECOND, spawnPointTime());

            // get last hour spawn time
            Calendar lastHourSpawn = Calendar.getInstance();
            lastHourSpawn.set(Calendar.MINUTE, minutes);
            lastHourSpawn.set(Calendar.SECOND, seconds);
            lastHourSpawn.add(Calendar.HOUR, -1);

            Calendar lastHourSpawnEnd = (Calendar) lastHourSpawn.clone();
            lastHourSpawnEnd.add(Calendar.MILLISECOND, spawnPointTime());

            if (lastHourSpawnEnd.getTimeInMillis() > now.getTimeInMillis()) { // still in last hour spawn time

                nextOrCurrentSpawn = lastHourSpawn;
                nextOrCurrentSpawnEnd = lastHourSpawnEnd;

            } else { // last hour spawn time expired

                if (currentHourSpawnEnd.getTimeInMillis() < now.getTimeInMillis()) { // current hour spawn time expired

                    currentHourSpawn.add(Calendar.HOUR, 1);
                    currentHourSpawnEnd.add(Calendar.HOUR, 1);
                }

                nextOrCurrentSpawn = currentHourSpawn;
                nextOrCurrentSpawnEnd = currentHourSpawnEnd;
            }
        }
    }

    public boolean isDespawn() {
        return !isActive() && !isSpawnSoon(Constant.SPAWN_SOON_RANGE);
    }

    public String getTimeLeft() {

        Calendar nowCalendar = Calendar.getInstance();
        Long value = nextOrCurrentSpawnEnd.getTimeInMillis() - nowCalendar.getTimeInMillis();

        if (value > 0) {

            Date date = new Date(value);
            return Constant.TIMER_FORMAT.format(date);

        } else {

            return Constant.TIMER_FORMAT.format(new Date(0));
        }
    }

    private boolean isActive() {
        return isSpawnSoon(0L);
    }

    private boolean isSpawnSoon(Long range) {

        Calendar nowCalendar = Calendar.getInstance();

        Long currentTime = nowCalendar.getTimeInMillis() + range;

        return currentTime > nextOrCurrentSpawn.getTimeInMillis() &&
                currentTime < nextOrCurrentSpawnEnd.getTimeInMillis();
    }

    private int spawnPointTime() {

        switch (type) {
            case 101:
                return 15 * 60 * 1000;
            case 102:
                return 30 * 60 * 1000;
            case 103:
                return 45 * 60 * 1000;
            case 104:
                return 60 * 60 * 1000;
            case 201:
                return 45 * 60 * 1000;
            case 202:
                return 60 * 60 * 1000;
            case 203:
                return 60 * 60 * 1000;
            case 204:
                return 60 * 60 * 1000;
            default:
                return 15 * 60 * 1000;
        }
    }

    @Override
    public boolean equals(Object obj) {

        Spawn that = (Spawn) obj;

        if(that.getLongitude() == longitude && that.getLatitude() == latitude) {
            return true;
        }

        return false;
        //return super.equals(obj);
    }
}
