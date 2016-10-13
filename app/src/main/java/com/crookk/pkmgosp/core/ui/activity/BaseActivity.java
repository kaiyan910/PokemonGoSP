package com.crookk.pkmgosp.core.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.Preference_;
import com.crookk.pkmgosp.core.bean.OttoBus;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public abstract class BaseActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar mViewToolbar;

    @Bean
    protected OttoBus mOttoBus;
    @Pref
    protected Preference_ mPreference;

    @Override
    public void onResume() {
        super.onResume();
        mOttoBus.register(this);
        setupUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOttoBus.unregister(this);
    }

    protected void setupBackNavigation() {

        if (mViewToolbar != null) {
            mViewToolbar.setTitle(getActivityTitle());
            setSupportActionBar(mViewToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }
    }

    protected abstract String getActivityTitle();

    /**
     * call this function whenever the app language is changed
     */
    protected abstract void setupUI();
}
