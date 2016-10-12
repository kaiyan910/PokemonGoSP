package com.crookk.pkmgosp.core.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.crookk.pkmgosp.R;
import com.crookk.pkmgosp.core.ui.view.LCEView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment
public abstract class ListFragment<M> extends BaseFragment implements LCEView<M>, SwipeRefreshLayout.OnRefreshListener {

    @ViewById(R.id.list)
    protected RecyclerView mViewList;
    @ViewById(R.id.empty_data)
    protected TextView mViewEmptyData;
    @ViewById(R.id.refresh_layout)
    protected SwipeRefreshLayout mViewRefreshLayout;

    protected List<M> mData = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();

        mViewRefreshLayout.setOnRefreshListener(this);
        mViewRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewRefreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mViewRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void showLoading() {

        mViewList.setVisibility(View.GONE);
        mViewEmptyData.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyData() {

        mViewList.setVisibility(View.GONE);
        mViewEmptyData.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData() {

        mViewList.setVisibility(View.VISIBLE);
        mViewEmptyData.setVisibility(View.GONE);
    }

    @UiThread
    @Override
    public void setData(List<M> data) {

        mData = new ArrayList<>(data);

        if(mData.size() > 0) {

            showData();
            onDataDownloaded();

        } else {

            showEmptyData();
        }
    }

    protected abstract void onDataDownloaded();

}
