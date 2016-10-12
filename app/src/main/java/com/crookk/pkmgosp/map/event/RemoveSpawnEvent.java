package com.crookk.pkmgosp.map.event;

import com.crookk.pkmgosp.map.bean.MarkerWrapper;

public class RemoveSpawnEvent {

    private MarkerWrapper model;

    public RemoveSpawnEvent(MarkerWrapper model) {
        this.model = model;
    }

    public MarkerWrapper getModel() {
        return model;
    }
}
