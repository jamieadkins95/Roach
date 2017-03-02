package com.jamieadkins.gwent.card.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseCardListFragment extends BaseFragment
        implements CardFilterListener, CardsContract.View {
    private CardsContract.Presenter mCardsPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);
        return rootView;
    }

    public int getLayoutId() {
        return R.layout.fragment_card_list;
    }

    @Override
    public void onStart() {
        super.onStart();
        onLoadData();
    }

    @Override
    public void onCardFilterUpdated() {
        getRecyclerViewAdapter().clear();
        onLoadData();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        onLoadCardData();
    }

    public void onLoadCardData() {
        CardFilterProvider cardFilterProvider = (CardFilterProvider) getActivity();
        cardFilterProvider.registerCardFilterListener(this);
        CardFilter cardFilter = cardFilterProvider.getCardFilter();
        mCardsPresenter.getCards(cardFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        mCardsPresenter = presenter;
    }
}
