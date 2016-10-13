package com.crookk.pkmgosp.map.bean;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.bean.OttoBus;
import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.map.event.CheckSpawnBoundsEvent;
import com.crookk.pkmgosp.map.event.CheckSpawnTimeEvent;
import com.crookk.pkmgosp.map.event.RemoveSpawnEvent;
import com.crookk.pkmgosp.local.model.Spawn;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.logger.Log;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class MarkerWrapper {

    @Bean
    OttoBus mOttoBus;

    private Marker marker;
    private Spawn spawn;
    private Spawn.SpawnStatus lastSpawnStatus;

    public void init(GoogleMap map, Spawn spawn) {

        mOttoBus.register(this);

        this.spawn = spawn;
        this.marker = map.addMarker(createMarkerOptions(spawn));
    }

    private MarkerOptions createMarkerOptions(Spawn spawn) {

        MarkerOptions options = new MarkerOptions();
        options.position(spawn.getLatLng());

        lastSpawnStatus = spawn.checkSpawnStatus();

        if (lastSpawnStatus == Spawn.SpawnStatus.SPAWN) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_spawn));
        } else if (lastSpawnStatus == Spawn.SpawnStatus.SPAWN_SOON) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_spawn_alpha));
        } else {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_despawn));
        }

        return options;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public void setSpawn(Spawn spawn) {
        this.spawn = spawn;
    }

    @Subscribe
    public void onCheckingBounds(CheckSpawnBoundsEvent event) {

        if (!event.getBounds().contains(spawn.getLatLng())) {

            LogUtils.debug(this, "spawn out of bounds=[%d]", spawn.getId());

            mOttoBus.post(new RemoveSpawnEvent(this));
            mOttoBus.unregister(this);
        }
    }

    @Subscribe
    public void onCheckingTime(CheckSpawnTimeEvent event) {

        Spawn.SpawnStatus currentSpawnStatus = spawn.checkSpawnStatus();

        if (currentSpawnStatus != lastSpawnStatus) {

            LogUtils.debug(this, "last spawn=[%s], current spawn=[%s] location=[%f, %f]",
                    lastSpawnStatus.name(), currentSpawnStatus.name(), spawn.getLatitude(), spawn.getLongitude());

            lastSpawnStatus = currentSpawnStatus;

            if (lastSpawnStatus == Spawn.SpawnStatus.SPAWN) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_spawn));
            } else if (lastSpawnStatus == Spawn.SpawnStatus.SPAWN_SOON) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_spawn_alpha));
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_despawn));
            }
        }
    }
}
