package com.jamieadkins.gwent.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * UI fragment that shows a list of the users decks.
 */
public abstract class BaseFragment<T> extends Fragment {
    private static final int VISIBLE_THRESHHOLD = 5;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshContainer;
    private BaseRecyclerViewAdapter<T> mViewAdapter;
    private boolean mLoading = false;
    private boolean mLoadMore = true;

    private Observer<T> mObserver = new Observer<T>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T value) {
            mViewAdapter.addItem(value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            setLoading(false);
        }
    };

    public void setLoadMore(boolean loadMore) {
        mLoadMore = loadMore;
    }

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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mLoading && mLoadMore &&
                        totalItemCount <= (lastVisibleItem + VISIBLE_THRESHHOLD)) {
                    onLoadData();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewAdapter.clear();
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

    public Observer<T> getObserver() {
        return mObserver;
    }
}
