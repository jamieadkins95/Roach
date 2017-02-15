package com.jamieadkins.gwent.card.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseCardListFragment extends BaseFragment<CardDetails>
        implements CardFilterListener {
    private CardsContract.Presenter mCardsPresenter;
    private boolean mDataLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);
        return rootView;
    }

    public abstract int getLayoutId();

    @Override
    public void onStart() {
        super.onStart();
        if (!mDataLoaded) {
            onLoadData();
        }
    }

    @Override
    public void onCardFilterUpdated() {
        getRecyclerViewAdapter().clear();
        onLoadData();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        CardFilter cardFilter = ((CardFilterProvider) getActivity()).getCardFilter();
        mCardsPresenter.getCards(cardFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
        mDataLoaded = true;
    }

    public void setCardsPresenter(CardsContract.Presenter cardsPresenter) {
        mCardsPresenter = cardsPresenter;
    }
}
