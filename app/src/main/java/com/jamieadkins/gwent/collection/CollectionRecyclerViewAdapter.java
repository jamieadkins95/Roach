package com.jamieadkins.gwent.collection;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jamieadkins.commonutils.ui.BaseRecyclerViewAdapter;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.BaseCardViewHolder;
import com.jamieadkins.gwent.card.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * RecyclerViewAdapter that shows a list of cards.
 */

public class CollectionRecyclerViewAdapter extends CardRecyclerViewAdapter {
    CollectionCardViewHolder.CollectionButtonListener mCollectionButtonListener;

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
}
