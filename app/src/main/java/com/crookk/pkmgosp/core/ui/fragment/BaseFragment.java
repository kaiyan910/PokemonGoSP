package com.crookk.pkmgosp.core.ui.fragment;

import android.support.v4.app.Fragment;

import com.crookk.pkmgosp.core.bean.OttoBus;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment
public abstract class BaseFragment extends Fragment {

    @Bean
    protected OttoBus mOttoBus;

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

    /**
     * call this function whenever the app language is changed
     */
    protected abstract void setupUI();
}
