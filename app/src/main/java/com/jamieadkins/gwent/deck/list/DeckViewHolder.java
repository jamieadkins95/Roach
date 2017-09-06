package com.jamieadkins.gwent.deck.list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity;

import kotlin.NotImplementedError;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends BaseViewHolder {
    private Deck mDeck;
    private final TextView mDeckName;
    private final TextView mDeckLeader;
    private final DeckSummaryView mDeckSummary;

    private String mLocale;

    public DeckViewHolder(View view) {
        super(view);
        mDeckName = (TextView) view.findViewById(R.id.deck_name);
        mDeckLeader = (TextView) view.findViewById(R.id.deck_leader);
        mDeckSummary = (DeckSummaryView) view.findViewById(R.id.deck_summary);

        Context context = view.getContext();
        String key = context.getString(R.string.pref_locale_key);
        mLocale = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, context.getString(R.string.default_locale));
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
                    throw new NotImplementedError();
                } else {
                    intent = new Intent(getView().getContext(), UserDeckDetailActivity.class);
                }
                intent.putExtra(DeckDetailActivity.EXTRA_DECK_ID, mDeck.getId());
                intent.putExtra(UserDeckDetailActivity.EXTRA_FACTION_ID, mDeck.getFactionId());
                intent.putExtra(DeckDetailActivity.EXTRA_IS_PUBLIC_DECK, mDeck.isPublicDeck());
                getView().getContext().startActivity(intent);

                // Log what deck has been viewed.
                if (mDeck.isPublicDeck()) {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            mDeck.getId(), mDeck.getName(), "Public Deck", FirebaseAnalytics.Event.VIEW_ITEM);
                } else {
                    FirebaseUtils.logAnalytics(getView().getContext(),
                            mDeck.getId(), mDeck.getName(), "User Deck", FirebaseAnalytics.Event.VIEW_ITEM);
                }

            }
        });

        mDeckName.setText(mDeck.getName());
        mDeckLeader.setText(mDeck.getLeader().getName(mLocale));

        mDeckSummary.setDeck(mDeck);

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
