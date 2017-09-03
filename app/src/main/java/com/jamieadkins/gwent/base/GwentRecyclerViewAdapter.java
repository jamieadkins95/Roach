package com.jamieadkins.gwent.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.collection.CollectionCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.deck.detail.user.DeckDetailCardViewHolder;
import com.jamieadkins.gwent.deck.list.DeckSummaryViewHolder;
import com.jamieadkins.gwent.deck.list.DeckViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                    case TYPE_DECK:
                        return new DeckSummaryViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_deck_summary, parent, false));
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
        getIndexOfCard(cardId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        if (value != -1) {
                            notifyItemChanged(value);
                        }
                    }
                });
    }

    public void updateCardCount(String cardId, int newCount) {
        mDeckCardCounts.put(cardId, newCount);

        getIndexOfCard(cardId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        if (value != -1) {
                            notifyItemChanged(value);
                        }
                    }
                });
    }

    private Single<Integer> getIndexOfCard(final String cardId) {
        return Single.defer(new Callable<SingleSource<? extends Integer>>() {
            @Override
            public SingleSource<? extends Integer> call() throws Exception {
                return Single.create(new SingleOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i < getItems().size(); i++ ){
                            if (getItems().get(i) instanceof CardDetails) {
                                CardDetails cardDetails = (CardDetails) getItems().get(i);
                                if (cardDetails.getIngameId().equals(cardId)) {
                                    emitter.onSuccess(i);
                                }
                            }
                        }

                        emitter.onSuccess(-1);
                    }
                });
            }
        });
    }

    public void removeCard(String cardId) {
        getIndexOfCard(cardId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        if (value != -1) {
                            removeItemAt(value);
                        }
                    }
                });
    }

    private void bindDeckCardCounts(DeckDetailCardViewHolder holder, int position) {
        CardDetails cardDetails = (CardDetails) getItems().get(position);
        if (mDeckCardCounts != null &&
                mDeckCardCounts.containsKey(cardDetails.getIngameId())) {
            holder.setItemCount(mDeckCardCounts.get(cardDetails.getIngameId()));
        } else {
            holder.setItemCount(0);
        }
    }

    private void bindCollection(CollectionCardViewHolder holder, int position) {
        CardDetails cardDetails = (CardDetails) getItems().get(position);
        if (mCollection != null &&
                mCollection.containsKey(cardDetails.getIngameId())) {

            for (String variationId : cardDetails.getVariations().keySet()) {
                if (mCollection.get(cardDetails.getIngameId()).containsKey(variationId)) {
                    final int count = mCollection.get(cardDetails.getIngameId()).get(variationId).intValue();
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

        public Builder withControls(Controls controls) {
            mAdapter.mControls = controls;
            return this;
        }

        public GwentRecyclerViewAdapter build() {
            return mAdapter;
        }
    }
}
