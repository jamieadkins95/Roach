package com.jamieadkins.gwent.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * UI fragment that shows a list of the users decks.
 */
public abstract class BaseFragment<T> extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshContainer;
    private BaseRecyclerViewAdapter<T> mViewAdapter;
    private boolean mLoading = false;

    private Observer<RxDatabaseEvent<T>> mObserver = new Observer<RxDatabaseEvent<T>>() {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RxDatabaseEvent<T> value) {
            switch (value.getEventType()) {
                case ADDED:
                    mViewAdapter.addItem(value.getValue());
                    break;
                case REMOVED:
                    mViewAdapter.removeItem(value.getValue());
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {

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
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mViewAdapter);
    }

    public void onLoadData() {
        setLoading(true);
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
        mRefreshContainer.setRefreshing(loading);

        // We don't want user's trying to refresh, so only enable the view when we are loading.
        mRefreshContainer.setEnabled(loading);
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setRecyclerViewAdapter(BaseRecyclerViewAdapter<T> mViewAdapter) {
        this.mViewAdapter = mViewAdapter;
    }

    public BaseRecyclerViewAdapter<T> getRecyclerViewAdapter() {
        return mViewAdapter;
    }

    public Observer<RxDatabaseEvent<T>> getObserver() {
        return mObserver;
    }
}
