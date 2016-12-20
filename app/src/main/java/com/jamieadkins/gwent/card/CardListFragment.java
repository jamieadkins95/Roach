package com.jamieadkins.gwent.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardListFragment extends Fragment implements CardsContract.View {
    private CardsContract.Presenter mCardsPresenter;
    private RecyclerView mCardListView;
    private SwipeRefreshLayout mRefreshContainer;
    private CardRecyclerViewAdapter mViewAdapter;

    private int visibleThreshold = 5;
    private boolean mLoading = false;

    Observer<CardDetails> observer = new Observer<CardDetails>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(CardDetails value) {
            mViewAdapter.addItem(value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            setLoadingIndicator(false);
        }
    };

    public CardListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewAdapter = new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);

        mCardListView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        setupRecyclerView(mCardListView);

        mRefreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);

        return rootView;
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

                if (!mLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    getCards();
                    setLoadingIndicator(true);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setLoadingIndicator(true);
        getCards();
    }

    private void getCards() {
        mCardsPresenter.getMoreCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewAdapter.clear();
        mCardsPresenter.stop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoading = active;
        mRefreshContainer.setRefreshing(active);

        // We don't want user's trying to refresh, so only enable the view when we are loading.
        mRefreshContainer.setEnabled(active);
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        mCardsPresenter = presenter;
    }
}
