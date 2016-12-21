package com.jamieadkins.gwent.card;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.detail.DetailActivity;

/**
 * ViewHolder for general yearns
 */

public class BaseCardViewHolder extends BaseViewHolder<CardDetails> {
    private final TextView mCardName;


    public BaseCardViewHolder(View view) {
        super(view);
        mCardName = (TextView) view.findViewById(R.id.card_name);
    }

    @Override
    public void bindItem(final CardDetails item) {
        super.bindItem(item);
        mCardName.setText(getBoundItem().getName());

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getView().getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_CARD_ID, item.getCardid());
                getView().getContext().startActivity(intent);
            }
        });
    }
}
