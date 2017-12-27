package com.jamieadkins.gwent.card.list;

import android.content.Intent;
import android.view.View;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.card.LargeCardView;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.card.CardDetails;

/**
 * ViewHolder for general yearns
 */

public class BaseCardViewHolder extends BaseViewHolder {
    private final LargeCardView mCardView;
    private CardDetails mCardDetails;

    public BaseCardViewHolder(View view) {
        super(view);
        mCardView = (LargeCardView) view;
    }

    @Override
    public void bindItem(final RecyclerViewItem item) {
        super.bindItem(item);
        mCardDetails = (CardDetails) item;

        mCardView.setCardDetails(mCardDetails);
        mCardView.loadImage();

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
        getView().getContext().startActivity(intent);
    }

    public CardDetails getBoundCardDetails() {
        return mCardDetails;
    }
}
