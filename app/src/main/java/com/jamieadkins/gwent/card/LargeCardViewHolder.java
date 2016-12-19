package com.jamieadkins.gwent.card;

import android.view.View;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;

/**
 * Holds much more detail about a card.
 */

public class LargeCardViewHolder extends BaseCardViewHolder {
    private final TextView mCardDescription;
    private final TextView mCardFaction;
    private final TextView mCardRarity;
    private final TextView mCardLoyalty;
    private final TextView mCardStrength;

    public LargeCardViewHolder(View view) {
        super(view);
        mCardDescription = (TextView) view.findViewById(R.id.card_description);
        mCardFaction = (TextView) view.findViewById(R.id.card_faction);
        mCardRarity = (TextView) view.findViewById(R.id.card_rarity);
        mCardLoyalty = (TextView) view.findViewById(R.id.card_loyalty);
        mCardStrength = (TextView) view.findViewById(R.id.card_strength);
    }

    @Override
    public void bindItem(CardDetails card) {
        super.bindItem(card);

        mCardDescription.setText(getBoundItem().getInfo());
        mCardFaction.setText(getBoundItem().getFaction());
        mCardRarity.setText(getBoundItem().getRarity());
        mCardLoyalty.setText(getBoundItem().getLoyalty());
        mCardStrength.setText(getBoundItem().getStrength());
    }
}
