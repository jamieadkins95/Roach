package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.deck.detail.BaseDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment
        implements DecksContract.View {

    protected interface DeckBuilderListener {
        void onDeckBuilderStateChanged(boolean open);
    }

    DeckDetailCardViewHolder.DeckDetailButtonListener mButtonListener =
            new DeckDetailCardViewHolder.DeckDetailButtonListener() {
                @Override
                public void addCard(CardDetails card) {
                    mDecksPresenter.addCardToDeck(mDeck, card);
                }

                @Override
                public void removeCard(CardDetails card) {
                    mDecksPresenter.removeCardFromDeck(mDeck, card);
                }
            };

    private DeckBuilderListener mDeckBuilderListener;

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mDeckBuilderListener.onDeckBuilderStateChanged(false);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mDeckBuilderListener.onDeckBuilderStateChanged(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setPeekHeight(250);
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
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.DECK)
                .withDeckButtonListener(mButtonListener)
                .build();
    }

    @Override
    protected void onDeckLoaded(Deck deck) {
        super.onDeckLoaded(deck);
        getRecyclerViewAdapter().setDeck(deck);
    }

    protected void setDeckBuilderListener(DeckBuilderListener listener) {
        mDeckBuilderListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDeckBuilderListener = null;
    }
}
