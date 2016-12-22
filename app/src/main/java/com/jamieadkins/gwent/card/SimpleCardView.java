package com.jamieadkins.gwent.card;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * Wrapper for our card detail view.
 */

public class SimpleCardView extends CardView {
    private TextView mCardName;

    public SimpleCardView(Context context) {
        super(context);
        initialiseView();
    }

    public SimpleCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialiseView();
    }

    public SimpleCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialiseView();
    }

    protected void initialiseView() {
        inflateView();
        mCardName = (TextView) findViewById(R.id.card_name);
    }

    protected void inflateView() {
        inflate(getContext(), R.layout.item_card_small, this);
    }

    public void setCardDetails(CardDetails cardDetails) {
        setName(cardDetails.getName());
    }

    private void setName(String name) {
        mCardName.setText(name);
    }
}
