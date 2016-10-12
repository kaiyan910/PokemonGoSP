package com.crookk.pkmgosp.local.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "gym")
public class Gym {

    @SerializedName("id")
    @DatabaseField(id = true)
    private String id;
    @SerializedName("lat")
    @DatabaseField
    private double latitude;
    @SerializedName("lng")
    @DatabaseField
    private double longitude;

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
}
