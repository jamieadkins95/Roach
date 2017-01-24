package com.jamieadkins.gwent.card;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.detail.DetailActivity;

/**
 * ViewHolder for general yearns
 */

public class BaseCardViewHolder extends BaseViewHolder<CardDetails> {
    private final SimpleCardView mSimpleCardView;

    public BaseCardViewHolder(View view) {
        super(view);
        mSimpleCardView = (SimpleCardView) view;
    }

    @Override
    public void bindItem(final CardDetails item) {
        super.bindItem(item);
        mSimpleCardView.setCardDetails(item);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDetailActivity();
            }
        });
    }

    public void launchDetailActivity() {
        Intent intent = new Intent(getView().getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CARD_ID, getBoundItem().getIngameId());

        String transitionName = getView().getContext().getString(R.string.transition_card);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) getView().getContext(),
                        getView(),
                        transitionName
                );

        // Show details using transition animation.
        ActivityCompat.startActivity(getView().getContext(), intent, options.toBundle());
    }
}
