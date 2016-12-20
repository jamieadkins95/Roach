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
    String mSearchQuery;
    Map<String, Boolean> mFactions;
    Map<String, Boolean> mTypes;
    Map<String, Boolean> mRarities;

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
}
