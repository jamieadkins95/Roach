package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.GoogleNowSubHeader;
import com.jamieadkins.commonutils.ui.SubHeader;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.data.Deck;

import java.util.HashMap;
import java.util.Map;

/**
 * UI fragment that shows a list of the users decks.
 */

public abstract class BaseDeckDetailFragment<T extends DeckDetailsContract.View> extends BaseFragment<T>
        implements DeckDetailsContract.View {
    private static final int LEADER_INDEX = 0;
    protected String mDeckId;
    protected Deck mDeck;
    private String mPatch;
    protected String mFactionId;

    private Map<String, SubHeader> mRowHeaders = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
            mFactionId = savedInstanceState.getString(DeckDetailActivity.EXTRA_FACTION_ID);
        }
        super.onCreate(savedInstanceState);
        mRowHeaders.put(getString(R.string.leader), new GoogleNowSubHeader(getString(R.string.leader), R.color.gold));
        mRowHeaders.put(getString(R.string.gold), new GoogleNowSubHeader(getString(R.string.gold), R.color.gold));
        mRowHeaders.put(getString(R.string.silver), new GoogleNowSubHeader(getString(R.string.silver), R.color.silver));
        mRowHeaders.put(getString(R.string.bronze), new GoogleNowSubHeader(getString(R.string.bronze), R.color.bronze));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        setupViews(rootView);
        return rootView;
    }

    protected abstract int getLayoutId();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, mDeckId);
        outState.putString(DeckDetailActivity.EXTRA_FACTION_ID, mFactionId);
        super.onSaveInstanceState(outState);
    }
}
