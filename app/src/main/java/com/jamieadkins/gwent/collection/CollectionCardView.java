package com.jamieadkins.gwent.collection;

import android.content.Context;
import android.util.AttributeSet;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.LargeCardView;

/**
 * Card view to be used in the collection view.
 */

public class CollectionCardView extends LargeCardView {
    public CollectionCardView(Context context) {
        super(context);
    }

    public CollectionCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollectionCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void inflateView() {
        inflate(getContext(), R.layout.item_card_collection, this);
    }
}
