package com.jamieadkins.gwent.card;

import android.os.Parcel;
import android.os.Parcelable;

import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used to filter cards.
 */

public class CardFilter implements Parcelable {
    private String mSearchQuery;
    private HashMap<String, Boolean> mFilters;
    private List<String> mCardIds;

    private boolean mCollectibleOnly = false;

    public CardFilter() {
        mSearchQuery = null;
        mFilters = new HashMap<>();
        mCardIds = null;

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

    public void setCollectibleOnly(boolean collectibleOnly) {
        mCollectibleOnly = collectibleOnly;
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

    public void addCardId(String cardId) {
        if (mCardIds == null) {
            mCardIds = new ArrayList<>();
        }

        mCardIds.add(cardId);
    }

    public boolean doesCardMeetFilter(CardDetails card) {
        if (mCardIds != null) {
            // If there are card ids specified, use them only.
            return mCardIds.contains(card.getIngameId());
        } else {
            boolean collectible = false;
            for (String variationId : card.getVariations().keySet()) {
                if (card.getVariations().get(variationId).isCollectible()) {
                    collectible = true;
                }
            }

            boolean include = !mCollectibleOnly || collectible;
            return get(card.getFaction()) && get(card.getRarity())
                    && get(card.getType()) && card.isReleased() && include;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSearchQuery);
        parcel.writeSerializable(mFilters);
        parcel.writeSerializable(mCollectibleOnly);
        parcel.writeStringList(mCardIds);
    }


    public static final Parcelable.Creator<CardFilter> CREATOR
            = new Parcelable.Creator<CardFilter>() {
        public CardFilter createFromParcel(Parcel in) {
            return new CardFilter(in);
        }

        public CardFilter[] newArray(int size) {
            return new CardFilter[size];
        }
    };

    private CardFilter(Parcel parcel) {
        mSearchQuery = parcel.readString();
        mFilters = (HashMap<String, Boolean>) parcel.readSerializable();
        mCollectibleOnly = (Boolean) parcel.readSerializable();
        parcel.readStringList(mCardIds);

    }

    @Override
    public String toString() {
        return mSearchQuery + "," + mCollectibleOnly + "," + mCardIds + "," + mFilters;
    }
}
