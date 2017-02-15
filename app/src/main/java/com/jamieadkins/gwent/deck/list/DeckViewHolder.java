package com.jamieadkins.gwent.deck.list;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends BaseViewHolder<Deck> {
    private final TextView mDeckName;
    private final TextView mDeckFaction;
    private final TextView mDeckMelee;
    private final TextView mDeckRanged;
    private final TextView mDeckSiege;
    private final TextView mDeckTotalCards;
    private final TextView mDeckTotalAttack;

    public DeckViewHolder(View view) {
        super(view);
        mDeckName = (TextView) view.findViewById(R.id.deck_name);
        mDeckFaction = (TextView) view.findViewById(R.id.deck_faction);
        mDeckMelee = (TextView) view.findViewById(R.id.deck_melee);
        mDeckRanged = (TextView) view.findViewById(R.id.deck_range);
        mDeckSiege = (TextView) view.findViewById(R.id.deck_siege);
        mDeckTotalCards = (TextView) view.findViewById(R.id.deck_total_cards);
        mDeckTotalAttack = (TextView) view.findViewById(R.id.deck_total_attack);
    }

    @Override
    public void bindItem(Deck item) {
        super.bindItem(item);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getView().getContext(), DeckDetailActivity.class);
                intent.putExtra(DeckDetailActivity.EXTRA_DECK_ID, getBoundItem().getId());
                intent.putExtra(DeckDetailActivity.EXTRA_PATCH, getBoundItem().getPatch());
                getView().getContext().startActivity(intent);

                // Log what card has been viewed.
                FirebaseUtils.logAnalytics(getView().getContext(),
                        getBoundItem().getId(), getBoundItem().getName(), "View Deck");
            }
        });

        mDeckName.setText(getBoundItem().getName());
        mDeckFaction.setText(getBoundItem().getFaction());
        mDeckMelee.setText("40");
        mDeckRanged.setText("24");
        mDeckSiege.setText("23");
        mDeckTotalCards.setText("Total Cards: 27");
        mDeckTotalAttack.setText("Total Attack: 80");

        int color;
        switch (getBoundItem().getFaction()) {
            case Faction.MONSTERS:
                color = ContextCompat.getColor(mDeckFaction.getContext(), R.color.monsters);
                break;
            case Faction.NORTHERN_REALMS:
                color = ContextCompat.getColor(mDeckFaction.getContext(), R.color.northernRealms);
                break;
            case Faction.SCOIATAEL:
                color = ContextCompat.getColor(mDeckFaction.getContext(), R.color.scoiatael);
                break;
            case Faction.SKELLIGE:
                color = ContextCompat.getColor(mDeckFaction.getContext(), R.color.skellige);
                break;
            default:
                color = ContextCompat.getColor(mDeckFaction.getContext(), R.color.skellige);
                break;
        }
        mDeckFaction.setTextColor(color);
    }
}