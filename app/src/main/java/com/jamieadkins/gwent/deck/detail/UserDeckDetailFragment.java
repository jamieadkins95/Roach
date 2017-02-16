package com.jamieadkins.gwent.deck.detail;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.deck.list.DecksContract;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment implements DecksContract.View {

    public UserDeckDetailFragment() {
    }

    public static UserDeckDetailFragment newInstance(String deckId) {
        UserDeckDetailFragment fragment = new UserDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_public_deck_detail;
    }

}
