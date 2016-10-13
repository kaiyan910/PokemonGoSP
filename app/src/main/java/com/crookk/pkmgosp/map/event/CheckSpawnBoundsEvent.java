package com.crookk.pkmgosp.map.event;

import com.google.android.gms.maps.model.LatLngBounds;

public class CheckSpawnBoundsEvent {

    private LatLngBounds bounds;

    public CheckSpawnBoundsEvent(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }
}
