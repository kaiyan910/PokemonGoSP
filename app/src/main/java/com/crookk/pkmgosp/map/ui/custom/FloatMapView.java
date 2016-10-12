package com.crookk.pkmgosp.map.ui.custom;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.custom_float_map)
public class FloatMapView extends LinearLayout {

    @ViewById(R.id.icon)
    ImageView mViewIcon;
    @ViewById(R.id.container)
    LinearLayout mViewContainer;

    private Context mContext;
    private WindowManager mWindowManager;

    private CustomMapView mViewGoogleMap;

    private boolean mExpand = false;

    public FloatMapView(Context context) {

        super(context);
        mContext = context;
    }

    @AfterViews
    void afterViews() {
        attachToWindow();
    }

    private void attachToWindow() {

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(this, params);

        mViewGoogleMap = CustomMapView_.build(getContext());
        mViewGoogleMap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.floatMapView_height)));
        mViewGoogleMap.onCreate();
        mViewGoogleMap.initMap();

        mViewIcon.setOnTouchListener(new OnTouchListener() {
            private int initX, initY;
            private int initTouchX, initTouchY;
            private Long tapMS = 0L;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtils.debug(FloatMapView.this, "DEBUG 0");
                        tapMS = System.currentTimeMillis();
                        if (!mExpand) {
                            initX = params.x;
                            initY = params.y;
                            initTouchX = x;
                            initTouchY = y;
                        }
                        return true;

                    case MotionEvent.ACTION_UP:

                        if (System.currentTimeMillis() - tapMS <= 100) {

                            LogUtils.debug(FloatMapView.this, "DEBUG 3");

                            if (!mExpand) {

                                LogUtils.debug(FloatMapView.this, "DEBUG 4");

                                mExpand = true;
                                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                                params.gravity = Gravity.START | Gravity.TOP;
                                params.x = 0;
                                params.y = 0;

                                mViewContainer.addView(mViewGoogleMap);
                                mViewGoogleMap.onResume();

                                mWindowManager.updateViewLayout(FloatMapView.this, params);

                            } else {

                                LogUtils.debug(FloatMapView.this, "DEBUG 5");

                                mExpand = false;
                                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                                params.gravity = Gravity.START;

                                mViewContainer.removeView(mViewGoogleMap);
                                mViewGoogleMap.onPause();

                                mWindowManager.updateViewLayout(FloatMapView.this, params);
                            }
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        LogUtils.debug(FloatMapView.this, "DEBUG 1");
                        if (!mExpand) {
                            params.x = initX + (x - initTouchX);
                            params.y = initY + (y - initTouchY);
                            mWindowManager.updateViewLayout(FloatMapView.this, params);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void destroy() {
        mViewGoogleMap.onDestroy();
        mWindowManager.removeView(this);
    }
}
