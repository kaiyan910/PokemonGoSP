package com.crookk.pkmgosp.map.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.utils.LogUtils;
import com.crookk.pkmgosp.map.ui.activity.MainActivity_;
import com.crookk.pkmgosp.map.ui.custom.FloatMapView;
import com.crookk.pkmgosp.map.ui.custom.FloatMapView_;

import org.androidannotations.annotations.EService;

@EService
public class FloatMapService extends Service {

    private final static int FOREGROUND_ID = 999;

    private FloatMapView mFloatMapView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtils.debug(this, "Widget started...");

        initView();

        PendingIntent pendingIntent = createPendingIntent();
        Notification notification = createNotification(pendingIntent);

        startForeground(FOREGROUND_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        destroyViw();
        stopForeground(true);

        LogUtils.debug(this, "Widget ended...");
    }

    private void initView() {
        mFloatMapView = FloatMapView_.build(this);
    }

    private void destroyViw() {
        mFloatMapView.destroy();
        mFloatMapView = null;
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity_.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    private Notification createNotification(PendingIntent intent) {

        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                .build();
    }
}
