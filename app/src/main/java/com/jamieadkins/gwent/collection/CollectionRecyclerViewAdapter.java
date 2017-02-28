package com.jamieadkins.gwent.collection;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CollectionRecyclerViewAdapter extends GwentRecyclerViewAdapter {
    private CollectionCardViewHolder.CollectionButtonListener mCollectionButtonListener;
    private Collection mCollection;

    public CollectionRecyclerViewAdapter(CollectionCardViewHolder.CollectionButtonListener
                                                 collectionButtonListener) {
        super();
        mCollectionButtonListener = collectionButtonListener;
    }

    @Override
    public BaseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionCardViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_collection_layout, parent, false),
                mCollectionButtonListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CollectionCardViewHolder collectionCardViewHolder = (CollectionCardViewHolder) holder;

        CardDetails cardDetails = (CardDetails) getItemAt(position);
        if (mCollection != null &&
                mCollection.getCards().containsKey(cardDetails.getIngameId())) {

            for (String variationId : cardDetails.getVariations().keySet()) {
                if (mCollection.getCards().get(cardDetails.getIngameId()).containsKey(variationId)) {
                    final int count = mCollection.getCards().get(cardDetails.getIngameId()).get(variationId);
                    collectionCardViewHolder.setItemCount(variationId, count);
                } else {
                    collectionCardViewHolder.setItemCount(variationId, 0);
                }
            }
        } else {
            for (String variationId : cardDetails.getVariations().keySet()) {
                collectionCardViewHolder.setItemCount(variationId, 0);
            }
        }

    }

    public void setCollection(Collection collection) {
        if (mCollection == null || !mCollection.equals(collection)) {
            mCollection = collection;
            notifyDataSetChanged();
        }
    }
}
