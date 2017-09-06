package com.jamieadkins.gwent.card.list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardListFragment extends BaseCardListFragment<CardsContract.View> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.card_database));
    }

    @Override
    public void setupPresenter() {
        setPresenter(new CardsPresenter(Injection.INSTANCE.provideCardsInteractor(getContext())));
    }
}
