package com.jamieadkins.gwent.collection;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.BaseCardViewHolder;
import com.jamieadkins.gwent.card.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CollectionRecyclerViewAdapter extends CardRecyclerViewAdapter {
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
    public void onBindViewHolder(BaseViewHolder<CardDetails> holder, int position) {
        super.onBindViewHolder(holder, position);
        CollectionCardViewHolder collectionCardViewHolder = (CollectionCardViewHolder) holder;
        if (mCollection != null &&
                mCollection.getCards().containsKey(getItemAt(position).getCardid())) {
            final int count = mCollection.getCards().get(getItemAt(position).getCardid());
            collectionCardViewHolder.setItemCount(count);
        } else {
            collectionCardViewHolder.setItemCount(0);
        }

    }

    public void setCollection(Collection collection) {
        mCollection = collection;
        notifyDataSetChanged();
    }
}
