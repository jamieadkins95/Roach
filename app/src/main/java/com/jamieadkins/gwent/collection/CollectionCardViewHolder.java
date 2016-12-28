package com.jamieadkins.gwent.collection;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.BaseCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.detail.DetailActivity;

/**
 * Includes buttons to add and remove cards from a collection.
 */

public class CollectionCardViewHolder extends BaseCardViewHolder {
    private Button buttonAdd;
    private Button buttonRemove;
    private TextView collectionCount;
    private CollectionButtonListener mListener;

    public interface CollectionButtonListener {
        void addCard(String cardId);
        void removeCard(String cardId);
    }

    public CollectionCardViewHolder(View view, CollectionButtonListener listener) {
        super(view);
        buttonAdd = (Button) view.findViewById(R.id.add_card);
        buttonRemove = (Button) view.findViewById(R.id.remove_card);
        collectionCount = (TextView) view.findViewById(R.id.collection_count);
        mListener = listener;
    }

    @Override
    public void bindItem(final CardDetails item) {
        super.bindItem(item);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.addCard(item.getCardid());
                }
            }
        });
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.removeCard(item.getCardid());
                }
            }
        });
    }

    public void setItemCount(int count) {
        collectionCount.setText(String.format(
                collectionCount.getContext().getString(R.string.in_collection), count));
    }

    @Override
    public void launchDetailActivity() {
        Intent intent = new Intent(getView().getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CARD_ID, getBoundItem().getCardid());
        getView().getContext().startActivity(intent);
    }
}
