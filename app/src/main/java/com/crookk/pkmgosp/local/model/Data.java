package com.crookk.pkmgosp.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("spawns")
    private List<Spawn> spawnList = new ArrayList<>();
    @SerializedName("gyms")
    private List<Gym> gymList = new ArrayList<>();
    @SerializedName("stops")
    private List<PokeStop> pokeStopList = new ArrayList<>();

    public List<Spawn> getSpawnList() {
        return spawnList;
    }

    public void setSpawnList(List<Spawn> spawnList) {
        this.spawnList = spawnList;
    }

    public List<Gym> getGymList() {
        return gymList;
    }

    public void setGymList(List<Gym> gymList) {
        this.gymList = gymList;
    }

    public List<PokeStop> getPokeStopList() {
        return pokeStopList;
    }

    public void setPokeStopList(List<PokeStop> pokeStopList) {
        this.pokeStopList = pokeStopList;
    }
}
