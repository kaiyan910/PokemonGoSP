package com.crookk.pkmgosp.core.bean;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.crookk.pkmgosp.core.utils.LogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.androidannotations.annotations.EBean;

import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionUtils;

@EBean
public class LocationRequestService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long INTERVAL = 10000L;
    private static final long FASTEST_INTERVAL = 5000L;

    Context mContext;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;

    public LocationRequestService(Context context) {

        this.mContext = context;
        this.mGoogleApiClient = (new GoogleApiClient.Builder(context))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(INTERVAL);
        this.mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void executeService(LocationListener locationListener) {
        this.mLocationListener = locationListener;
        this.mGoogleApiClient.connect();
        if (this.mGoogleApiClient.isConnected()) {
            this.startLocationUpdates();
            LogUtils.debug(this, "Location update resumed .....................");
        }

    }

    public void getLocationListener(LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }

    @Override
    public void onConnected(Bundle bundle) {

        //noinspection MissingPermission
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //LogUtils.debug(this, "Last Location=[%f, %f]", location.getLatitude(), location.getLongitude());
        if (location != null) this.mLocationListener.onLocationUpdate(location);
        this.startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {

        LogUtils.debug(this, "Location Changed=[%f, %f]", location.getLatitude(), location.getLongitude());
        this.mLocationListener.onLocationUpdate(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void startLocationUpdates() {
        if (PermissionUtils.isGranted(mContext, PermissionEnum.ACCESS_COARSE_LOCATION) &&
                PermissionUtils.isGranted(mContext, PermissionEnum.ACCESS_FINE_LOCATION)) {
            //noinspection MissingPermission
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            LogUtils.debug(this, "Location update started ..............: ");
        }
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, this);
            LogUtils.debug(this, "Location update stopped .......................");
        }
    }

    public interface LocationListener {
        void onLocationUpdate(Location location);
    }
}
