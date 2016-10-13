package com.crookk.pkmgosp.map.ui.custom;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.map.bean.MarkerWrapper;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Date;
import java.util.Locale;

@EViewGroup(R.layout.custom_marker_popup)
public class MarkerPopupView extends LinearLayout {

    @ViewById(R.id.name)
    TextView mViewName;
    @ViewById(R.id.spawn_time)
    TextView mViewSpawnTime;
    @ViewById(R.id.expire_time)
    TextView mViewExpireTime;

    @StringRes(R.string.popup_spawnTime)
    String mStringSpawnTime;
    @StringRes(R.string.popup_expireTime)
    String mStringExpireTime;

    private MarkerWrapper mMarkerModel;

    public MarkerPopupView(Context context) {
        super(context);
    }

    public void bind(MarkerWrapper markerModel) {

        this.mMarkerModel = markerModel;

        String name = Constant.SPAWN_TYPE.get(mMarkerModel.getSpawn().getType());
        String spawnTime = String.format(Locale.getDefault(), mStringSpawnTime,
                Constant.DATE_FORMAT.format(new Date(mMarkerModel.getSpawn().getNextOrCurrentSpawn().getTimeInMillis())),
                Constant.DATE_FORMAT.format(new Date(mMarkerModel.getSpawn().getNextOrCurrentSpawnEnd().getTimeInMillis())));
        String timeLeft = String.format(Locale.getDefault(), mStringExpireTime, mMarkerModel.getSpawn().getTimeLeft());

        mViewName.setText(name);
        mViewSpawnTime.setText(spawnTime);
        mViewExpireTime.setText(timeLeft);

        updateTimer();
    }

    @UiThread(delay = 1000)
    void updateTimer() {

        if (mMarkerModel.getMarker().isInfoWindowShown()) {
            mMarkerModel.getMarker().hideInfoWindow();
            mMarkerModel.getMarker().showInfoWindow();
        }
    }
}
