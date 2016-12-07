package com.jamieadkins.gwent.cardui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jamieadkins.gwent.R;
import com.jamieadkins.jgaw.card.BaseApiResult;
import com.jamieadkins.jgaw.card.Card;

/**
 * ViewHolder for general yearns
 */

public class SimpleCardViewHolder extends RecyclerView.ViewHolder {
    private final View mView;
    private final TextView mCardName;

    private BaseApiResult mBoundCard;

    public SimpleCardViewHolder(View view) {
        super(view);
        mView = view;
        mCardName = (TextView) view.findViewById(R.id.card_name);
    }

    public void bindCard(BaseApiResult card) {
        mBoundCard = card;

        mCardName.setText(mBoundCard.getName());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), mBoundCard.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mCardName.getText();
    }
}
