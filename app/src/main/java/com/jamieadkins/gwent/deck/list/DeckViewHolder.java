package com.jamieadkins.gwent.deck.list;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.Position;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.deck.detail.PublicDeckDetailActivity;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends BaseViewHolder {
    private Deck mDeck;
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
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mDeck = (Deck) item;

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mDeck.isPublicDeck()) {
                    intent = new Intent(getView().getContext(), PublicDeckDetailActivity.class);
                } else {
                    intent = new Intent(getView().getContext(), UserDeckDetailActivity.class);
                }
                intent.putExtra(DeckDetailActivity.EXTRA_DECK_ID, mDeck.getId());
                intent.putExtra(UserDeckDetailActivity.EXTRA_FACTION_ID, mDeck.getFactionId());
                intent.putExtra(DetailActivity.EXTRA_PATCH, mDeck.getPatch());
                intent.putExtra(DeckDetailActivity.EXTRA_IS_PUBLIC_DECK, mDeck.isPublicDeck());
                getView().getContext().startActivity(intent);

                // Log what deck has been viewed.
                if (mDeck.isPublicDeck()) {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            mDeck.getId(), mDeck.getName(), "View Public Deck");
                } else {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            mDeck.getId(), mDeck.getName(), "View Deck");
                }

            }
        });

        mDeckName.setText(mDeck.getName());
        mDeckLeader.setText(mDeck.getLeader().getName());
        mDeckMelee.setText(String.valueOf(mDeck.getStrengthForPosition(Position.MELEE)));
        mDeckRanged.setText(String.valueOf(mDeck.getStrengthForPosition(Position.RANGED)));
        mDeckSiege.setText(String.valueOf(mDeck.getStrengthForPosition(Position.SIEGE)));
        mDeckTotalCards.setText(String.format(
                mDeckTotalCards.getContext().getString(R.string.total_cards),
                mDeck.getTotalCardCount()));
        mDeckTotalAttack.setText(String.valueOf(mDeck.getTotalStrength()));

        int color;
        switch (mDeck.getFactionId()) {
            case Faction.MONSTERS_ID:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.monsters);
                break;
            case Faction.NORTHERN_REALMS_ID:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.northernRealms);
                break;
            case Faction.SCOIATAEL_ID:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.scoiatael);
                break;
            case Faction.SKELLIGE_ID:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
            case Faction.NILFGAARD_ID:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.nilfgaard);
                break;
            default:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
        }
        mDeckLeader.setTextColor(color);
    }
}
