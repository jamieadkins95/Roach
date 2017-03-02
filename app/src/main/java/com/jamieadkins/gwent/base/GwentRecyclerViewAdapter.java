package com.jamieadkins.gwent.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.collection.CollectionCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.deck.detail.user.DeckDetailCardViewHolder;
import com.jamieadkins.gwent.deck.list.DeckViewHolder;

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
    private CollectionCardViewHolder.CollectionButtonListener mCollectionButtonListener;
    private DeckDetailCardViewHolder.DeckDetailButtonListener mDeckButtonListener;
    private Collection mCollection;
    private Map<String, Integer> mDeckCardCounts;

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
                                        .inflate(R.layout.card_collection_layout, parent, false),
                                mCollectionButtonListener);
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
                                .inflate(R.layout.card_deck_detail_layout, parent, false),
                                mDeckButtonListener);
                    case TYPE_CARD_LEADER:
                        return new BaseCardViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_detail_layout, parent, false));
                    case TYPE_DECK:
                        return new DeckViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_deck, parent, false));
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

    public void setCardCollection(Collection collection) {
        if (mCollection == null || !mCollection.equals(collection)) {
            mCollection = collection;
            notifyDataSetChanged();
        }
    }

    public void setDeck(Deck deck) {
        if (mDeckCardCounts == null || !mDeckCardCounts.equals(deck.getCardCount())) {
            mDeckCardCounts = deck.getCardCount();
            notifyDataSetChanged();
        }
    }

    private void bindDeckCardCounts(DeckDetailCardViewHolder holder, int position) {
        CardDetails cardDetails = (CardDetails) getItemAt(position);
        if (mDeckCardCounts != null &&
                mDeckCardCounts.containsKey(cardDetails.getIngameId())) {
            holder.setItemCount(mDeckCardCounts.get(cardDetails.getIngameId()));
        } else {
            holder.setItemCount(0);
        }
    }

    private void bindCollection(CollectionCardViewHolder holder, int position) {
        CardDetails cardDetails = (CardDetails) getItemAt(position);
        if (mCollection != null &&
                mCollection.getCards().containsKey(cardDetails.getIngameId())) {

            for (String variationId : cardDetails.getVariations().keySet()) {
                if (mCollection.getCards().get(cardDetails.getIngameId()).containsKey(variationId)) {
                    final int count = mCollection.getCards().get(cardDetails.getIngameId()).get(variationId);
                    holder.setItemCount(variationId, count);
                } else {
                    holder.setItemCount(variationId, 0);
                }
            }
        } else {
            for (String variationId : cardDetails.getVariations().keySet()) {
                holder.setItemCount(variationId, 0);
            }
        }
    }

    public static class Builder {

        GwentRecyclerViewAdapter mAdapter;

        public Builder() {
            mAdapter = new GwentRecyclerViewAdapter();
        }

        public Builder withCollectionControls(
                CollectionCardViewHolder.CollectionButtonListener listener) {
            mAdapter.mControls = Controls.COLLECTION;
            mAdapter.mCollectionButtonListener = listener;
            return this;
        }

        public Builder withDeckControls(
                DeckDetailCardViewHolder.DeckDetailButtonListener listener) {
            mAdapter.mControls = Controls.DECK;
            mAdapter.mDeckButtonListener = listener;
            return this;
        }

        public GwentRecyclerViewAdapter build() {
            return mAdapter;
        }
    }
}
