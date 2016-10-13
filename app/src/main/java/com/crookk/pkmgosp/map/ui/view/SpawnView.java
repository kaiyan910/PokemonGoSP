package com.crookk.pkmgosp.map.ui.view;

import com.crookk.pkmgosp.core.ui.view.BaseView;
import com.crookk.pkmgosp.local.model.Spawn;

import java.util.List;

public interface SpawnView extends BaseView {
    void onSpawnListReceived(List<Spawn> spawnList);
}
