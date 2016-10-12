package com.crookk.pkmgosp.map.model;

import android.content.Context;

import com.crookk.pkmgosp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchCircle {

    private Circle circle;
    private Marker marker;
    private boolean focus = false;

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public void createFocus(Context context, GoogleMap map, LatLng location) {

        removeFocus();

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(location)
                .strokeWidth(5)
                .strokeColor(context.getResources().getColor(R.color.spawn_border))
                .fillColor(context.getResources().getColor(R.color.spawn_fill))
                .radius(200);

        circle = map.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin));
        markerOptions.position(location);
        markerOptions.zIndex(99);

        marker = map.addMarker(markerOptions);

        focus = true;
    }

    public void removeFocus() {
        if (circle != null) circle.remove();
        if (marker != null) marker.remove();

        focus = false;
    }
}
