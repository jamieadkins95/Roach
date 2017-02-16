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
import com.jamieadkins.gwent.data.Position;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends BaseViewHolder<Deck> {
    private final TextView mDeckName;
    private final TextView mDeckLeader;
    private final TextView mDeckMelee;
    private final TextView mDeckRanged;
    private final TextView mDeckSiege;
    private final TextView mDeckTotalCards;
    private final TextView mDeckTotalAttack;

    public DeckViewHolder(View view) {
        super(view);
        mDeckName = (TextView) view.findViewById(R.id.deck_name);
        mDeckLeader = (TextView) view.findViewById(R.id.deck_leader);
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
                intent.putExtra(DeckDetailActivity.EXTRA_IS_PUBLIC_DECK, getBoundItem().isPublicDeck());
                getView().getContext().startActivity(intent);

                // Log what deck has been viewed.
                if (getBoundItem().isPublicDeck()) {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            getBoundItem().getId(), getBoundItem().getName(), "View Public Deck");
                } else {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            getBoundItem().getId(), getBoundItem().getName(), "View Deck");
                }

            }
        });

        mDeckName.setText(getBoundItem().getName());
        mDeckLeader.setText(getBoundItem().getLeader().getName());
        mDeckMelee.setText(String.valueOf(getBoundItem().getStrengthForPosition(Position.MELEE)));
        mDeckRanged.setText(String.valueOf(getBoundItem().getStrengthForPosition(Position.RANGED)));
        mDeckSiege.setText(String.valueOf(getBoundItem().getStrengthForPosition(Position.SIEGE)));
        mDeckTotalCards.setText(String.format(
                mDeckTotalCards.getContext().getString(R.string.total_cards),
                getBoundItem().getTotalCardCount()));
        mDeckTotalAttack.setText(String.valueOf(getBoundItem().getTotalStrength()));

        int color;
        switch (getBoundItem().getFactionId()) {
            case Faction.MONSTERS:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.monsters);
                break;
            case Faction.NORTHERN_REALMS:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.northernRealms);
                break;
            case Faction.SCOIATAEL:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.scoiatael);
                break;
            case Faction.SKELLIGE:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
            case Faction.NILFGAARD:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.nilfgaard);
                break;
            default:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
        }
        mDeckLeader.setTextColor(color);
    }
}
