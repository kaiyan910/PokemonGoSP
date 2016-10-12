package com.crookk.pkmgosp.map.ui.custom;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.bean.LocationRequestService;
import com.crookk.pkmgosp.core.bean.OttoBus;
import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.local.model.Spawn;
import com.crookk.pkmgosp.map.bean.MarkerWrapper;
import com.crookk.pkmgosp.map.bean.MarkerWrapper_;
import com.crookk.pkmgosp.map.event.CheckSpawnBoundsEvent;
import com.crookk.pkmgosp.map.event.CheckSpawnTimeEvent;
import com.crookk.pkmgosp.map.event.RemoveSpawnEvent;
import com.crookk.pkmgosp.map.model.SearchCircle;
import com.crookk.pkmgosp.map.presenter.SpawnPresenter;
import com.crookk.pkmgosp.map.ui.view.SpawnView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

@EViewGroup(R.layout.custom_map_view)
public class CustomMapView extends RelativeLayout implements SpawnView, OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapLongClickListener {

    /*@ViewById(R.id.map_container)
    FrameLayout mViewMapContainer;*/
    @ViewById(R.id.google_map_view)
    MapView mViewGoogleMap;

    @Bean
    OttoBus mOttoBus;
    @Bean
    SpawnPresenter mSpawnPresenter;

    @ColorRes(R.color.spawn_border)
    int mColorSpawnBorder;
    @ColorRes(R.color.spawn_fill)
    int mColorSpawnFill;

    private SearchCircle mFocusPoint = new SearchCircle();
    private GoogleMap mGoogleMap;
    private LongSparseArray<MarkerWrapper> mSpawnMarker = new LongSparseArray<>();
    private Map<Marker, MarkerWrapper> mMarkerMap = new HashMap<>();

    private boolean mMapCreated = false;
    private boolean mMapRunning = false;
    private boolean mMapLocationInit = false;

    private Location mLastLocation;
    private LocationRequestService mLocationService;

    public CustomMapView(Context context) {
        super(context);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void afterViews() {

        mOttoBus.register(this);
        obtainLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        if (PermissionUtils.isGranted(getContext(), PermissionEnum.ACCESS_COARSE_LOCATION)) {
            //noinspection MissingPermission
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setIndoorEnabled(false);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setBuildingsEnabled(false);

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMapLongClickListener(this);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.312927, 114.261670), Constant.SPAWN_DISPLAY_ZOOM));

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                MarkerWrapper markerModel = mMarkerMap.get(marker);

                if (markerModel != null && !markerModel.getSpawn().isDespawn()) {
                    MarkerPopupView window = MarkerPopupView_.build(getContext());
                    window.bind(markerModel);
                    return window;
                }

                return null;
            }
        });
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });


        mViewGoogleMap.onResume();
        mMapCreated = true;
        mMapRunning = true;
        checkSpawnTime();
    }

    @Override
    public void onError(int code) {

    }

    @Override
    public void onCameraIdle() {

        mSpawnPresenter.getAllSpawnPoint(mGoogleMap.getCameraPosition().zoom,
                mGoogleMap.getProjection().getVisibleRegion().latLngBounds);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mFocusPoint.createFocus(getContext(), mGoogleMap, latLng);
    }

    @UiThread
    @Override
    public void onSpawnListReceived(List<Spawn> spawnList) {
        if (spawnList.size() > 0) drawSpawnPoint(mGoogleMap, spawnList);
    }

    @Subscribe
    public void onRemoveSpawn(RemoveSpawnEvent event) {
        event.getModel().getMarker().remove();
        mSpawnMarker.remove(event.getModel().getSpawn().getId());
        mMarkerMap.remove(event.getModel().getMarker());
    }

    @UiThread(delay = 1000)
    protected void checkSpawnTime() {

        if (mMapRunning) {
            mOttoBus.post(new CheckSpawnTimeEvent());
            checkSpawnTime();
        }
    }

    public void onCreate() {
        if (mViewGoogleMap != null) mViewGoogleMap.onCreate(new Bundle());
    }

    public void onResume() {

        mMapRunning = true;
        if (mViewGoogleMap != null) mViewGoogleMap.onResume();
        if (mMapCreated) {
            if (mLocationService != null) mLocationService.startLocationUpdates();
            checkSpawnTime();
        }
    }

    public void onPause() {

        if (mLocationService != null) mLocationService.stopLocationUpdates();
        if (mViewGoogleMap != null) mViewGoogleMap.onPause();
        mMapRunning = false;
    }

    public void onDestroy() {
        mViewGoogleMap.onDestroy();
        mOttoBus.unregister(this);
    }

    public boolean isMapCreated() {
        return mMapCreated;
    }

    public void initMap() {

        try {

            mSpawnPresenter.attach(this);

            mViewGoogleMap.getMapAsync(this);

        } catch (IllegalStateException e) {

            LogUtils.error(this, e);
        }
    }

    private void drawSpawnPoint(GoogleMap map, List<Spawn> spawnList) {

        mOttoBus.post(new CheckSpawnBoundsEvent(map.getProjection().getVisibleRegion().latLngBounds));

        for (Spawn spawn : spawnList) {

            if (mSpawnMarker.indexOfKey(spawn.getId()) < 0) {

                MarkerWrapper model = MarkerWrapper_.getInstance_(getContext());
                model.init(map, spawn);

                mSpawnMarker.append(spawn.getId(), model);
                mMarkerMap.put(model.getMarker(), model);
            }
        }
    }


    private void obtainLocation() {

        LogUtils.debug(this, "obtainLocation()");
        mLocationService = new LocationRequestService(getContext());
        mLocationService.executeService(new LocationRequestService.LocationListener() {
            @Override
            public void onLocationUpdate(Location location) {
                LogUtils.debug(CustomMapView.this, "location=[%f, %f]", location.getLatitude(), location.getLongitude());
                mLastLocation = location;
                if (!mMapLocationInit && mGoogleMap != null) {
                    mMapLocationInit = true;
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), Constant.SPAWN_DISPLAY_ZOOM));
                }
            }
        });
    }
}
