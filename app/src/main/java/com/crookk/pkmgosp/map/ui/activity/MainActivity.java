package com.crookk.pkmgosp.map.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.bean.PermissionChecker;
import com.crookk.pkmgosp.core.ui.activity.BaseActivity;
import com.crookk.pkmgosp.local.Constant;
import com.crookk.pkmgosp.map.presenter.MainPresenter;
import com.crookk.pkmgosp.map.service.FloatMapService_;
import com.crookk.pkmgosp.map.ui.custom.CustomMapView;
import com.crookk.pkmgosp.map.ui.view.MainView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainView {

    @ViewById(R.id.map_view)
    CustomMapView mViewMap;

    @Bean
    PermissionChecker mPermissionChecker;
    @Bean
    MainPresenter mainPresenter;

    @StringRes(R.string.widget_error_message)
    String mStringWidgetErrorMessage;

    private boolean mCreatedService = false;
    private MaterialDialog mMaterialDialog;

    @AfterViews
    void afterViews() {

        mainPresenter.attach(this);
        mViewMap.onCreate();

        if (PermissionUtils.isGranted(MainActivity.this, PermissionEnum.ACCESS_COARSE_LOCATION)) {
            startMap();
        } else {
            requestPermission();
        }
    }

    @OnActivityResult(PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE)
    public void onActivityResult(int resultCode, Intent data) {

        if (!mPermissionChecker.isRequiredPermissionGranted()) {
            Toast.makeText(this, mStringWidgetErrorMessage, Toast.LENGTH_LONG).show();
        } else {
            startWidget();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatMapService_.intent(this).stop();
        mCreatedService = false;
        mViewMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewMap.onDestroy();
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void setupUI() {

    }

    @UiThread
    @Override
    public void onDatabaseInserted() {

        if (mMaterialDialog != null && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }

        mPreference.edit().dataVersion().put(Constant.DATA_VERSION).apply();
        mViewMap.initMap();
    }

    @Override
    public void onError(int code) {

    }

    @Click(R.id.windows)
    void windows() {

        if (!mPermissionChecker.isRequiredPermissionGranted()) {
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            startWidget();
        }
    }

    private void startMap() {


        if (Constant.DATA_VERSION != mPreference.dataVersion().get()) {

            mMaterialDialog = new MaterialDialog.Builder(this)
                    .title(R.string.data_loading_title)
                    .titleColorRes(R.color.colorPrimary)
                    .content(R.string.data_loading_message)
                    .cancelable(false)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .canceledOnTouchOutside(false)
                    .show();

            mainPresenter.insertDataFromJson();

        } else {

            mViewMap.initMap();
        }
    }

    private void startWidget() {

        if (!mCreatedService) {
            mCreatedService = true;
            FloatMapService_.intent(this).start();
        }
    }

    private void requestPermission() {

        PermissionManager.with(MainActivity.this)
                .permission(PermissionEnum.ACCESS_COARSE_LOCATION, PermissionEnum.ACCESS_FINE_LOCATION)
                .askagain(true)
                .askagainCallback(new AskagainCallback() {
                    @Override
                    public void showRequestPermission(UserResponse response) {
                        requestPermission();
                    }
                })
                .callback(new FullCallback() {
                    @Override
                    public void result(ArrayList<PermissionEnum> permissionsGranted,
                                       ArrayList<PermissionEnum> permissionsDenied,
                                       ArrayList<PermissionEnum> permissionsDeniedForever,
                                       ArrayList<PermissionEnum> permissionsAsked) {

                        startMap();
                    }
                })
                .ask();
    }
}
