package com.jamieadkins.gwent.card;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;
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
    private String mLocale;

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

    public String getLocale() {
        return mLocale;
    }

    protected void initialiseView() {
        inflateView();
        mCardName = (TextView) findViewById(R.id.card_name);
        String localeKey = getContext().getString(R.string.locale_key);
        String defaultLocale = getResources().getConfiguration().locale.toString().replace("_", "-");
        mLocale = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getString(localeKey, defaultLocale);
    }

    protected void inflateView() {
        inflate(getContext(), R.layout.item_card_small, this);
    }

    public void setCardDetails(CardDetails cardDetails) {
        setName(cardDetails.getName(mLocale));
    }

    private void setName(String name) {
        mCardName.setText(name);
    }
}
