package com.jamieadkins.gwent.deck.list;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailActivity;
import com.jamieadkins.gwent.model.deck.GwentDeckSummary;

/**
 * Holds much more detail about a card.
 */

public class DeckViewHolder extends BaseViewHolder {
    private GwentDeckSummary mDeck;
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
        mDeck = (GwentDeckSummary) item;

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getView().getContext(), UserDeckDetailActivity.class);
                intent.putExtra(DeckDetailActivity.EXTRA_DECK_ID, mDeck.getDeck().getId());
                getView().getContext().startActivity(intent);
            }
        });

        mDeckName.setText(mDeck.getDeck().getName());
        mDeckLeader.setText(mDeck.getLeader().getName().get(mLocale));

        mDeckSummary.setDeck(mDeck.getCardCounts());

        int color;
        switch (mDeck.getDeck().getFaction()) {
            case MONSTER:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.monsters);
                break;
            case NORTHERN_REALMS:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.northernRealms);
                break;
            case SCOIATAEL:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.scoiatael);
                break;
            case SKELLIGE:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
            case NILFGAARD:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.nilfgaard);
                break;
            default:
                color = ContextCompat.getColor(mDeckLeader.getContext(), R.color.skellige);
                break;
        }
        mDeckLeader.setTextColor(color);
    }
}
