package com.jamieadkins.gwent.deck.detail;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;

/**
 * UI fragment that shows a list of the users decks.
 */

public class PublicDeckDetailFragment extends BaseDeckDetailFragment implements DeckDetailsContract.View {

    public static PublicDeckDetailFragment newInstance(String deckId) {
        PublicDeckDetailFragment fragment = new PublicDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_public_deck_detail;
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(250);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showDeck(@NonNull Deck deck) {

    }
}
