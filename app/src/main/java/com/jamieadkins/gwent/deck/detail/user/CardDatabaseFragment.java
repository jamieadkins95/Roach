package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;

import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;

import org.jetbrains.annotations.Nullable;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardDatabaseFragment extends BaseCardListFragment<CardsContract.View>
        implements CardsContract.View {

    private String factionId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            factionId = savedInstanceState.getString(DeckDetailActivity.EXTRA_FACTION_ID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupPresenter() {
       setPresenter(new CardsPresenter(Injection.INSTANCE.provideCardsInteractor(getContext())));
    }

    public static CardDatabaseFragment newInstance(String factionId) {
        CardDatabaseFragment fragment = new CardDatabaseFragment();
        fragment.factionId = factionId;
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
                .build();
    }

    @Override
    public CardFilter initialiseCardFilter() {
        CardFilter filter = new CardFilter();
        filter.put(Type.LEADER_ID, false);
        filter.setCollectibleOnly(true);
        for (Filterable faction : Faction.ALL_FACTIONS) {
            if (!faction.getId().equals(factionId)) {
                filter.put(faction.getId(), false);
            }
        }
        filter.put(Faction.NEUTRAL_ID, true);
        filter.setCurrentFilterAsBase();
        return filter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DeckDetailActivity.EXTRA_FACTION_ID, factionId);
        super.onSaveInstanceState(outState);
    }

}
