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

    public CollectionCardViewHolder(View view) {
        super(view);
        buttonAdd = (Button) view.findViewById(R.id.add_card);
        buttonRemove = (Button) view.findViewById(R.id.remove_card);
    }

    @Override
    public void bindItem(CardDetails item) {
        super.bindItem(item);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIEA", "Add card");
            }
        });
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("JAMIEA", "Remove card");
            }
        });
    }
}
