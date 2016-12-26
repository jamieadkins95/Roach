package com.jamieadkins.gwent.card;

import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Group;
import com.jamieadkins.gwent.data.Rarity;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to filter cards.
 */

public class CardFilter {
    private String mSearchQuery;
    private Map<String, Boolean> mFactions;
    private Map<String, Boolean> mTypes;
    private Map<String, Boolean> mRarities;

    private boolean mCollectableOnly = false;

    public CardFilter() {
        mSearchQuery = null;
        mFactions = new HashMap<>();
        mTypes = new HashMap<>();
        mRarities = new HashMap<>();

        mRarities.put(Rarity.COMMON, true);
        mRarities.put(Rarity.RARE, true);
        mRarities.put(Rarity.EPIC, true);
        mRarities.put(Rarity.LEGENDARY, true);

        mTypes.put(Group.BRONZE, true);
        mTypes.put(Group.SILVER, true);
        mTypes.put(Group.GOLD, true);
        mTypes.put(Group.LEADER, true);

        mFactions.put(Faction.NEUTRAL, true);
        mFactions.put(Faction.NORTHERN_REALMS, true);
        mFactions.put(Faction.SKELLIGE, true);
        mFactions.put(Faction.SCOIATAEL, true);
        mFactions.put(Faction.MONSTERS, true);
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public void setSearchQuery(String mSearchQuery) {
        this.mSearchQuery = mSearchQuery;
    }

    public Map<String, Boolean> getFactions() {
        return mFactions;
    }

    public Map<String, Boolean> getRarities() {
        return mRarities;
    }

    public Map<String, Boolean> getTypes() {
        return mTypes;
    }

    public boolean isCollectableOnly() {
        return mCollectableOnly;
    }

    public void clearFilters() {
        for (String faction : mFactions.keySet()) {
            mFactions.put(faction, true);
        }

        for (String type : mTypes.keySet()) {
            mTypes.put(type, true);
        }

        for (String rarity : mRarities.keySet()) {
            mRarities.put(rarity, true);
        }
    }
}
