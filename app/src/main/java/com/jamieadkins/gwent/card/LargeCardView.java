package com.jamieadkins.gwent.card;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.Rarity;

/**
 * Wrapper for our card detail view.
 */

public class LargeCardView extends SimpleCardView {
    private TextView mCardDescription;
    private TextView mCardFaction;
    private TextView mCardRarity;
    private TextView mCardLoyalty;
    private TextView mCardStrength;
    private TextView mCardType;

    public LargeCardView(Context context) {
        super(context);
    }

    public LargeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initialiseView() {
        super.initialiseView();
        mCardDescription = (TextView) findViewById(R.id.card_description);
        mCardFaction = (TextView) findViewById(R.id.card_faction);
        mCardRarity = (TextView) findViewById(R.id.card_rarity);
        mCardLoyalty = (TextView) findViewById(R.id.card_loyalty);
        mCardStrength = (TextView) findViewById(R.id.card_strength);
        mCardType = (TextView) findViewById(R.id.card_type);
    }

    @Override
    protected void inflateView() {
        inflate(getContext(), R.layout.item_card_large, this);
    }

    @Override
    public void setCardDetails(CardDetails cardDetails) {
        super.setCardDetails(cardDetails);
        setDescription(cardDetails.getInfo());
        setFaction(cardDetails.getFaction());
        setRarity(cardDetails.getRarity());
        if (cardDetails.getLoyalty() != null) {
            setLoyalty(cardDetails.getLoyalty().get(0));
        }
        if (cardDetails.getType() != null) {
            setType(cardDetails.getType());
        }

        setStrength(String.valueOf(cardDetails.getStrength()));
    }

    private void setType(String type) {
        mCardType.setText(type);

        int typeColor;
        switch (type) {
            case Type.BRONZE:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.bronze);
                break;
            case Type.SILVER:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.silver);
                break;
            case Type.GOLD:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.gold);
                break;
            case Type.LEADER:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.gold);
                break;
            default:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.common);
                break;
        }
        mCardType.setTextColor(typeColor);
    }

    private void setLoyalty(String loyalty) {
        if (loyalty != null) {
            mCardLoyalty.setText(loyalty);
        } else {
            mCardLoyalty.setVisibility(View.GONE);
        }
    }

    private void setDescription(String description) {
        mCardDescription.setText(description);
    }

    private void setStrength(String strength) {
        mCardStrength.setText(strength);
    }

    private void setRarity(String rarity) {
        mCardRarity.setText(rarity);

        int rarityColor;
        switch (rarity) {
            case Rarity.COMMON:
                rarityColor = ContextCompat.getColor(mCardRarity.getContext(), R.color.common);
                break;
            case Rarity.RARE:
                rarityColor = ContextCompat.getColor(mCardRarity.getContext(), R.color.rare);
                break;
            case Rarity.EPIC:
                rarityColor = ContextCompat.getColor(mCardRarity.getContext(), R.color.epic);
                break;
            case Rarity.LEGENDARY:
                rarityColor = ContextCompat.getColor(mCardRarity.getContext(), R.color.legendary);
                break;
            default:
                rarityColor = ContextCompat.getColor(mCardRarity.getContext(), R.color.common);
                break;
        }
        mCardRarity.setTextColor(rarityColor);
    }

    private void setFaction(String faction) {
        mCardFaction.setText(faction);

        int factionColor;
        switch (faction) {
            case Faction.MONSTERS:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.monsters);
                break;
            case Faction.NORTHERN_REALMS:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.northernRealms);
                break;
            case Faction.SCOIATAEL:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.scoiatael);
                break;
            case Faction.SKELLIGE:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.skellige);
                break;
            case Faction.NEUTRAL:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.neutral);
                break;
            case Faction.NILFGAARD:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.nilfgaard);
                break;
            default:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.neutral);
                break;
        }
        mCardFaction.setTextColor(factionColor);
    }
}
