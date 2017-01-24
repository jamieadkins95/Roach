package com.jamieadkins.gwent.card;

import com.facebook.internal.FetchedAppSettings;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to filter cards.
 */

public class CardFilter {
    private String mSearchQuery;
    private Map<String, Boolean> mFilters;

    private boolean mCollectibleOnly = false;

    public CardFilter() {
        mSearchQuery = null;
        mFilters = new HashMap<>();

        for (String rarity : Rarity.ALL_RARITIES) {
            mFilters.put(rarity, true);
        }

        for (String type : Type.ALL_TYPES) {
            mFilters.put(type, true);
        }

        for (String faction : Faction.ALL_FACTIONS) {
            mFilters.put(faction, true);
        }
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public void setSearchQuery(String mSearchQuery) {
        this.mSearchQuery = mSearchQuery;
    }

    public boolean isCollectibleOnly() {
        return mCollectibleOnly;
    }

    public void clearFilters() {
        for (String faction : mFilters.keySet()) {
            mFilters.put(faction, true);
        }
    }

    public boolean get(String key) {
        if (mFilters.get(key) != null) {
            return mFilters.get(key);
        } else {
            return false;
        }
    }

    public void put(String key, boolean filter) {
        mFilters.put(key, filter);
    }
}
