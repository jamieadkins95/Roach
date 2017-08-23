package com.jamieadkins.gwent.collection;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.bus.CollectionEvent;
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.card.list.BaseCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;

import java.util.HashMap;
import java.util.Map;

/**
 * Includes buttons to add and remove cards from a collection.
 */

public class CollectionCardViewHolder extends BaseCardViewHolder {
    private Map<String, Button> mAddButtons;
    private Map<String, Button> mRemoveButtons;
    private Map<String, TextView> mCollectionCounts;

    public CollectionCardViewHolder(View view) {
        super(view);
        mAddButtons = new HashMap<>();
        mRemoveButtons = new HashMap<>();
        mCollectionCounts = new HashMap<>();
    }

    @Override
    public void bindItem(final RecyclerViewItem item) {
        super.bindItem(item);

        resetViews();

        for (final String variationId : getBoundCardDetails().getVariations().keySet()) {
            int variationNumber = CardDetails.Variation.getVariationNumber(variationId);
            switch (variationNumber) {
                case 1:
                    mAddButtons.put(variationId, (Button) getView().findViewById(R.id.add_variation_1));
                    mRemoveButtons.put(variationId, (Button) getView().findViewById(R.id.remove_variation_1));
                    mCollectionCounts.put(variationId, (TextView) getView().findViewById(R.id.collection_count_1));
                    getView().findViewById(R.id.collection_controls_1).setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mAddButtons.put(variationId, (Button) getView().findViewById(R.id.add_variation_2));
                    mRemoveButtons.put(variationId, (Button) getView().findViewById(R.id.remove_variation_2));
                    mCollectionCounts.put(variationId, (TextView) getView().findViewById(R.id.collection_count_2));
                    getView().findViewById(R.id.collection_controls_2).setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mAddButtons.put(variationId, (Button) getView().findViewById(R.id.add_variation_3));
                    mRemoveButtons.put(variationId, (Button) getView().findViewById(R.id.remove_variation_3));
                    mCollectionCounts.put(variationId, (TextView) getView().findViewById(R.id.collection_count_3));
                    getView().findViewById(R.id.collection_controls_3).setVisibility(View.VISIBLE);
                    break;
            }

            mAddButtons.get(variationId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.INSTANCE.post(new CollectionEvent(
                            new CollectionEvent.CollectionEventBundle(
                                    CollectionEvent.Event.ADD_CARD, getBoundCardDetails().getIngameId(), variationId)));

                }
            });
            mRemoveButtons.get(variationId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RxBus.INSTANCE.post(new CollectionEvent(
                            new CollectionEvent.CollectionEventBundle(
                                    CollectionEvent.Event.REMOVE_CARD, getBoundCardDetails().getIngameId(), variationId)));
                }
            });
        }
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

        mAddButtons = new HashMap<>();
        mRemoveButtons = new HashMap<>();
        mCollectionCounts = new HashMap<>();
    }

    public void setItemCount(String variationId, int count) {

        // Hide remove button if there are already 0 cards in collection.
        if (count > 0) {
            mRemoveButtons.get(variationId).setVisibility(View.VISIBLE);
        } else {
            mRemoveButtons.get(variationId).setVisibility(View.INVISIBLE);
        }

        mCollectionCounts.get(variationId).setText(String.format(
                mCollectionCounts.get(variationId).getContext().getString(R.string.in_collection),
                CardDetails.Variation.getVariationNumber(variationId), count));
    }
}
