package com.jamieadkins.gwent.card.list;

import android.content.Intent;
import android.view.View;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.card.SimpleCardView;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;

import java.util.Locale;

/**
 * ViewHolder for general yearns
 */

public class BaseCardViewHolder extends BaseViewHolder {
    private final SimpleCardView mSimpleCardView;
    private CardDetails mCardDetails;

    public BaseCardViewHolder(View view) {
        super(view);
        mSimpleCardView = (SimpleCardView) view;
    }

    @Override
    public void bindItem(final RecyclerViewItem item) {
        super.bindItem(item);
        mCardDetails = (CardDetails) item;

        mSimpleCardView.setCardDetails(mCardDetails);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDetailActivity();
            }
        });
    }

    public void launchDetailActivity() {
        Intent intent = new Intent(getView().getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CARD_ID, mCardDetails.getIngameId());
        intent.putExtra(DetailActivity.EXTRA_PATCH, mCardDetails.getPatch());
        getView().getContext().startActivity(intent);

        // Log what card has been viewed.
        FirebaseUtils.logAnalytics(getView().getContext(),
                mCardDetails.getIngameId(), mCardDetails.getName(Locale.US.toString()), "View Card");
    }

    public CardDetails getBoundCardDetails() {
        return mCardDetails;
    }
}
