package com.jamieadkins.gwent.collection;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.bus.CollectionEvent;
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.data.card.CardDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * Includes buttons to add and remove cards from a collection.
 */

public class CollectionCardViewHolder extends BaseCardViewHolder {
    private Button mAddButton;
    private Button mRemoveButton;
    private TextView mCollectionCount;

    public CollectionCardViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindItem(final RecyclerViewItem item) {
        super.bindItem(item);

        resetViews();

        mAddButton = (Button) getView().findViewById(R.id.add_variation_1);
        mRemoveButton = (Button) getView().findViewById(R.id.remove_variation_1);
        mCollectionCount = (TextView) getView().findViewById(R.id.collection_count_1);
        getView().findViewById(R.id.collection_controls_1).setVisibility(View.VISIBLE);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.INSTANCE.post(new CollectionEvent(
                        new CollectionEvent.CollectionEventBundle(
                                CollectionEvent.Event.ADD_CARD, getBoundCardDetails().getId())));

            }
        });
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.INSTANCE.post(new CollectionEvent(
                        new CollectionEvent.CollectionEventBundle(
                                CollectionEvent.Event.REMOVE_CARD, getBoundCardDetails().getId())));
            }
        });
    }

    private void resetViews() {
        // Default to hidden.
        getView().findViewById(R.id.collection_controls_1).setVisibility(View.GONE);
        getView().findViewById(R.id.collection_controls_2).setVisibility(View.GONE);
        getView().findViewById(R.id.collection_controls_3).setVisibility(View.GONE);

        getView().findViewById(R.id.add_variation_1).setOnClickListener(null);
        getView().findViewById(R.id.add_variation_2).setOnClickListener(null);
        getView().findViewById(R.id.add_variation_3).setOnClickListener(null);

        getView().findViewById(R.id.remove_variation_1).setOnClickListener(null);
        getView().findViewById(R.id.remove_variation_2).setOnClickListener(null);
        getView().findViewById(R.id.remove_variation_3).setOnClickListener(null);
    }

    public void setItemCount(int count) {

        // Hide remove button if there are already 0 cards in collection.
        if (count > 0) {
            mRemoveButton.setVisibility(View.VISIBLE);
        } else {
            mRemoveButton.setVisibility(View.INVISIBLE);
        }

        mCollectionCount.setText(mCollectionCount.getContext().getString(R.string.in_collection, 1, count));
    }
}
