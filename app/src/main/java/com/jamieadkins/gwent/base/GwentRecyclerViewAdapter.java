package com.jamieadkins.gwent.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.collection.CollectionCardViewHolder;
import com.jamieadkins.gwent.deck.detail.user.DeckDetailCardViewHolder;
import com.jamieadkins.gwent.deck.list.DeckViewHolder;
import com.jamieadkins.gwent.model.GwentCard;

import java.util.HashMap;
import java.util.Map;

/**
 * Is able to contain different Gwent elements in a list (cards, decks, etc.).
 */

public class GwentRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    public static final int TYPE_CARD = 2000;
    public static final int TYPE_CARD_LEADER = 2001;
    public static final int TYPE_DECK = 1000;

    public enum Controls {
        NONE,
        COLLECTION,
        DECK
    }

    private Controls mControls = Controls.NONE;
    private Map<String, Map<String, Long>> mCollection;
    private Map<String, Integer> mDeckCardCounts = new HashMap<>();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (mControls) {
            case NONE:
                switch (viewType) {
                    case TYPE_CARD:
                    case TYPE_CARD_LEADER:
                        return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_detail_layout, parent, false));
                    case TYPE_DECK:
                        return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_deck, parent, false));
                    default:
                        return super.onCreateViewHolder(parent, viewType);
                }
            case COLLECTION:
                switch (viewType) {
                    case TYPE_CARD:
                    case TYPE_CARD_LEADER:
                        return new CollectionCardViewHolder(
                                LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.card_collection_layout, parent, false));
                    case TYPE_DECK:
                        return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_deck, parent, false));
                    default:
                        return super.onCreateViewHolder(parent, viewType);
                }
            case DECK:
                switch (viewType) {
                    case TYPE_CARD:
                        return new DeckDetailCardViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_deck_detail_layout, parent, false));
                    case TYPE_CARD_LEADER:
                        return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_detail_layout, parent, false));
                    default:
                        return super.onCreateViewHolder(parent, viewType);
                }
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof DeckDetailCardViewHolder) {
            bindDeckCardCounts((DeckDetailCardViewHolder) holder, position);
        }

        if (holder instanceof CollectionCardViewHolder) {
            bindCollection((CollectionCardViewHolder) holder, position);
        }
    }

    public void updateCollection(String cardId, Map<String, Long> variationCounts) {
        if (mCollection == null) {
            mCollection = new HashMap<>();
        }

        mCollection.put(cardId, variationCounts);
        notifyItemChanged(getIndexOfCard(cardId));
    }

    public void updateCardCount(String cardId, int newCount) {
        mDeckCardCounts.put(cardId, newCount);
        notifyItemChanged(getIndexOfCard(cardId));
    }

    private int getIndexOfCard(final String cardId) {
        for (int i = 0; i < getItems().size(); i++ ){
            if (getItems().get(i) instanceof GwentCard) {
                GwentCard cardDetails = (GwentCard) getItems().get(i);
                if (cardDetails.getId().equals(cardId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void removeCard(String cardId) {
        removeItemAt(getIndexOfCard(cardId));
    }

    private void bindDeckCardCounts(DeckDetailCardViewHolder holder, int position) {
        GwentCard cardDetails = (GwentCard) getItems().get(position);
        if (mDeckCardCounts != null &&
                mDeckCardCounts.containsKey(cardDetails.getId())) {
            holder.setItemCount(mDeckCardCounts.get(cardDetails.getId()));
        } else {
            holder.setItemCount(0);
        }
    }

    private void bindCollection(CollectionCardViewHolder holder, int position) {
        GwentCard cardDetails = (GwentCard) getItems().get(position);
        if (mCollection != null &&
                mCollection.containsKey(cardDetails.getId())) {

            final int count = mCollection.get(cardDetails.getId()).get(cardDetails.getId() + "00").intValue();
            holder.setItemCount(count);
        } else {
            holder.setItemCount(0);
        }
    }

    public static class Builder {

        GwentRecyclerViewAdapter mAdapter;

        public Builder() {
            mAdapter = new GwentRecyclerViewAdapter();
        }

        public Builder withControls(Controls controls) {
            mAdapter.mControls = controls;
            return this;
        }

        public GwentRecyclerViewAdapter build() {
            return mAdapter;
        }
    }
}
