package com.jamieadkins.gwent.card;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.main.MainActivity;
import com.jamieadkins.gwent.main.PresenterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseCardListFragment extends BaseFragment<CardDetails>
        implements CardsContract.View {
    private CardsContract.Presenter mCardsPresenter;

    private PresenterFactory<CardsContract.Presenter> mPresenterFactory =
            new PresenterFactory<CardsContract.Presenter>() {
                @NonNull
                @Override
                public CardsContract.Presenter createPresenter() {
                    return new CardsPresenter();
                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardsPresenter = getPresenterCache().getPresenter(getClass().getSimpleName(), mPresenterFactory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);
        onLoadData();
        return rootView;
    }

    public abstract int getLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardsPresenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCardsPresenter.unbindView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCardsPresenter.stop();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        CardFilter cardFilter = ((MainActivity) getActivity()).getCardFilter();
        mCardsPresenter.getCards(cardFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(active);
    }

    @Override
    public void onCardFilterUpdated() {
        getRecyclerViewAdapter().clear();
        onLoadData();
    }
}
