package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.view.MenuItem;

import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.filter.FilterableItem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardDatabaseFragment extends BaseCardListFragment<CardsContract.View>
        implements CardsContract.View {

    private String factionId;

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<FilterableItem> filterableItems = new ArrayList<>();
        String filteringOn;
        Filterable[] filterItems;

        switch (item.getItemId()) {
            case R.id.filter_reset:
                filterPresenter.clearFilters();
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                Filterable faction = Faction.NORTHERN_REALMS;
                switch (factionId) {
                    case Faction.MONSTERS_ID:
                        faction = Faction.MONSTERS;
                        break;
                    case Faction.NORTHERN_REALMS_ID:
                        faction = Faction.NORTHERN_REALMS;
                        break;
                    case Faction.SCOIATAEL_ID:
                        faction = Faction.SCOIATAEL;
                        break;
                    case Faction.SKELLIGE_ID:
                        faction = Faction.SKELLIGE;
                        break;
                    case Faction.NILFGAARD_ID:
                        faction = Faction.NILFGAARD;
                        break;
                }
                filterItems = new Filterable[] {faction, Faction.NEUTRAL};
                break;
            case R.id.filter_rarity:
                filteringOn = getString(R.string.rarity);
                filterItems = Rarity.ALL_RARITIES;
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                filterItems = new Filterable[] {Type.BRONZE, Type.SILVER, Type.GOLD};
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        for (Filterable filterable : filterItems) {
            filterableItems.add(new FilterableItem(
                    filterable.getId(),
                    getString(filterable.getName()),
                    filterPresenter.cardFilter.get(filterable.getId())));
        }

        showFilterMenu(filteringOn, filterableItems);
        return true;
    }
}
