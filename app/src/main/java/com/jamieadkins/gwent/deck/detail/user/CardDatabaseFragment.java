package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.view.MenuItem;

import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.deck.detail.DeckBuilderContract;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.filter.FilterableItem;
import com.jamieadkins.gwent.model.CardColour;
import com.jamieadkins.gwent.model.GwentFaction;

import java.util.ArrayList;
import java.util.List;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardDatabaseFragment extends BaseCardListFragment<DeckBuilderContract.CardDatabaseView>
        implements DeckBuilderContract.CardDatabaseView {

    private GwentFaction faction;
    private String deckId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            faction = (GwentFaction) savedInstanceState.getSerializable(DeckDetailActivity.EXTRA_FACTION_ID);
            deckId = savedInstanceState.getString(DeckDetailActivity.EXTRA_DECK_ID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupPresenter() {
        setPresenter(new CardDatabasePresenter(
                deckId,
                Injection.INSTANCE.provideDecksInteractor(getContext()),
                Injection.INSTANCE.provideSchedulerProvider(),
                Injection.INSTANCE.provideCardRepository(),
                Injection.INSTANCE.provideUpdateRepository()));
    }

    public static CardDatabaseFragment newInstance(String deckId, GwentFaction faction) {
        CardDatabaseFragment fragment = new CardDatabaseFragment();
        fragment.deckId = deckId;
        fragment.faction = faction;
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
        filter.getColourFilter().put(CardColour.LEADER, false);
        filter.setCollectibleOnly(true);
        for (GwentFaction f : GwentFaction.values()) {
            if (!f.equals(faction)) {
                filter.getFactionFilter().put(f, false);
            }
        }
        filter.getFactionFilter().put(GwentFaction.NEUTRAL, true);
        filter.setCollectibleOnly(true);
        filter.setCurrentFilterAsBase();
        return filter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(DeckDetailActivity.EXTRA_FACTION_ID, faction);
        outState.putString(DeckDetailActivity.EXTRA_DECK_ID, deckId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<FilterableItem> filterableItems = new ArrayList<>();
        String filteringOn;

        switch (item.getItemId()) {
            case R.id.filter_reset:
                filterPresenter.clearFilters();
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                for (GwentFaction faction : new GwentFaction[]{faction, GwentFaction.NEUTRAL}) {
                    filterableItems.add(new FilterableItem<GwentFaction>(
                            faction,
                            filterPresenter.cardFilter.getFactionFilter().get(faction)));
                }
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                for (CardColour colour : new CardColour[]{CardColour.BRONZE, CardColour.SILVER, CardColour.GOLD}) {
                    filterableItems.add(new FilterableItem<CardColour>(
                            colour,
                            filterPresenter.cardFilter.getColourFilter().get(colour)));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        showFilterMenu(filteringOn, filterableItems);
        return true;
    }

    @Override
    public void updateCardCount(String cardId, int count) {
        getRecyclerViewAdapter().updateCardCount(cardId, count);
    }
}
