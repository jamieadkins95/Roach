package com.jamieadkins.gwent.base;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observer;

/**
 * UI fragment that shows a list of the users decks.
 */
public abstract class BaseFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshContainer;
    private GwentRecyclerViewAdapter mAdapter;
    private boolean mLoading = false;

    private Observer<RxDatabaseEvent<? extends RecyclerViewItem>> mObserver
            = new BaseObserver<RxDatabaseEvent<? extends RecyclerViewItem>>() {
        @Override
        public void onNext(RxDatabaseEvent<? extends RecyclerViewItem> value) {
            if (getActivity() == null) {
                return;
            }
            onDataEvent(value);
        }

        @Override
        public void onComplete() {
            setLoading(false);
        }
    };

    public void setupViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        setupRecyclerView(mRecyclerView);

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);
        mRefreshContainer.setOnRefreshListener(this);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = onBuildRecyclerView();
        recyclerView.setAdapter(mAdapter);
    }

    public void onDataEvent(RxDatabaseEvent<? extends RecyclerViewItem> data) {
        switch (data.getEventType()) {
            case ADDED:
                mAdapter.addItem(data.getValue());
                break;
            case REMOVED:
                mAdapter.removeItem(data.getValue());
                break;
            case CHANGED:
                mAdapter.updateItem(data.getValue());
                break;
            case COMPLETE:
                setLoading(false);
                break;
        }
    }

    public void onLoadData() {
        setLoading(true);
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
        mRefreshContainer.setRefreshing(loading);
        enableRefreshing(loading);
    }

    public void enableRefreshing(boolean enable) {
        mRefreshContainer.setEnabled(enable);
    }

    public boolean isLoading() {
        return mLoading;
    }

    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .build();
    }

    public GwentRecyclerViewAdapter getRecyclerViewAdapter() {
        return mAdapter;
    }

    public Observer<RxDatabaseEvent<? extends RecyclerViewItem>> getObserver() {
        return mObserver;
    }

    @Override
    public void onRefresh() {
        getRecyclerViewAdapter().clear();
        onLoadData();
    }
}
