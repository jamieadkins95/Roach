package com.jamieadkins.gwent.collection;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.BaseCardViewHolder;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * Includes buttons to add and remove cards from a collection.
 */

public class CollectionCardViewHolder extends BaseCardViewHolder {
    Button buttonAdd;
    Button buttonRemove;
    CollectionButtonListener mListener;

    public interface CollectionButtonListener {
        void addCard(String cardId);
        void removeCard(String cardId);
    }

    public CollectionCardViewHolder(View view, CollectionButtonListener listener) {
        super(view);
        buttonAdd = (Button) view.findViewById(R.id.add_card);
        buttonRemove = (Button) view.findViewById(R.id.remove_card);
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
}
