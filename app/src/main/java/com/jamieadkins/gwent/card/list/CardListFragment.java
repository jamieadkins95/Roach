package com.jamieadkins.gwent.card.list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamieadkins.commonutils.mvp.PresenterFactory;
import com.jamieadkins.gwent.InteractorContainer;
import com.jamieadkins.gwent.InteractorContainers;
import com.jamieadkins.gwent.R;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardListFragment extends BaseCardListFragment implements CardsContract.View,
        PresenterFactory<CardsContract.Presenter> {

    public CardListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.LARGE));
        getActivity().setTitle(getString(R.string.card_database));
        setCardsPresenter(createPresenter());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_card_list;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(active);
    }

    @Override
    public void setLoading(boolean loading) {
        super.setLoading(loading);

        // Card Data doesn't need refreshing, so disable swipe up to refresh.
        enableRefreshing(loading);
    }

    @Override
    public CardsContract.Presenter createPresenter() {
        InteractorContainer interactorContainer = InteractorContainers.getFromApp(getActivity());
        return new CardsPresenter(this,
                interactorContainer.getCardsInteractor());
    }

    @Override
    public void setPresenter(CardsContract.Presenter presenter) {
        setCardsPresenter(presenter);
    }
}
