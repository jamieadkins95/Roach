package com.jamieadkins.gwent.card;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Group;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;

/**
 * Holds much more detail about a card.
 */

public class LargeCardViewHolder extends BaseCardViewHolder {
    private final TextView mCardDescription;
    private final TextView mCardFaction;
    private final TextView mCardRarity;
    private final TextView mCardLoyalty;
    private final TextView mCardStrength;
    private final TextView mCardType;

    public LargeCardViewHolder(View view) {
        super(view);
        mCardDescription = (TextView) view.findViewById(R.id.card_description);
        mCardFaction = (TextView) view.findViewById(R.id.card_faction);
        mCardRarity = (TextView) view.findViewById(R.id.card_rarity);
        mCardLoyalty = (TextView) view.findViewById(R.id.card_loyalty);
        mCardStrength = (TextView) view.findViewById(R.id.card_strength);
        mCardType = (TextView) view.findViewById(R.id.card_type);
    }

    @Override
    public void bindItem(CardDetails card) {
        super.bindItem(card);

        mCardDescription.setText(getBoundItem().getInfo());
        setFactionInfo();
        setRarityInfo();
        setLoyaltyInfo();
        setTypeInfo();
        mCardStrength.setText(getBoundItem().getStrength());
    }

    private void setTypeInfo() {
        mCardType.setText(getBoundItem().getType());

        int typeColor;
        switch (getBoundItem().getType()) {
            case Group.BRONZE:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.bronze);
                break;
            case Group.SILVER:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.silver);
                break;
            case Group.GOLD:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.gold);
                break;
            case Group.LEADER:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.gold);
                break;
            default:
                typeColor = ContextCompat.getColor(mCardType.getContext(), R.color.common);
                break;
        }
        mCardType.setTextColor(typeColor);
    }

    private void setLoyaltyInfo() {
        if (getBoundItem().getLoyalty() != null) {
            mCardLoyalty.setText(getBoundItem().getLoyalty());
        } else {
            mCardLoyalty.setVisibility(View.GONE);
        }
    }

    private void setRarityInfo() {
        mCardRarity.setText(getBoundItem().getRarity());

        int rarityColor;
        switch (getBoundItem().getRarity()) {
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

    private void setFactionInfo() {
        mCardFaction.setText(getBoundItem().getFaction());

        int factionColor;
        switch (getBoundItem().getFaction()) {
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
            default:
                factionColor = ContextCompat.getColor(mCardFaction.getContext(), R.color.neutral);
                break;
        }
        mCardFaction.setTextColor(factionColor);
    }
}
