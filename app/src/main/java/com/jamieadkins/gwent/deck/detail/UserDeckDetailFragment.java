package com.jamieadkins.gwent.deck.detail;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment implements DecksContract.View {
    CardRecyclerViewAdapter mCardRecyclerViewAdapter;

    public UserDeckDetailFragment() {
    }

    public static UserDeckDetailFragment newInstance(String deckId) {
        UserDeckDetailFragment fragment = new UserDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_deck_detail;
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(250);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mCardRecyclerViewAdapter = new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE);
        recyclerView.setAdapter(mCardRecyclerViewAdapter);
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mDecksPresenter.getDeck(mDeckId, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public Observer<RxDatabaseEvent<CardDetails>> getObserver() {
        return new Observer<RxDatabaseEvent<CardDetails>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RxDatabaseEvent<CardDetails> value) {
                switch (value.getEventType()) {
                    case ADDED:
                        mCardRecyclerViewAdapter.addItem(value.getValue());
                        break;
                    case REMOVED:
                        mCardRecyclerViewAdapter.removeItem(value.getValue());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                SwipeRefreshLayout refreshLayout =
                        (SwipeRefreshLayout) getView().findViewById(R.id.cardRefreshContainer);
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
            }
        };
    }
}
