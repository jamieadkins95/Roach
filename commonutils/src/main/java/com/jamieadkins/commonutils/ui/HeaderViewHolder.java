package com.jamieadkins.commonutils.ui;

import android.view.View;
import android.widget.TextView;

import com.jamieadkins.commonutils.R;

/**
 * Created by jamiea on 25/02/17.
 */

public class HeaderViewHolder extends BaseViewHolder {
    private Header mBoundHeader;

    private final TextView mPrimaryText;
    private final TextView mSecondaryText;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        mPrimaryText = (TextView) itemView.findViewById(R.id.header_primary_text);
        mSecondaryText = (TextView) itemView.findViewById(R.id.header_secondary_text);
    }

    @Override
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mBoundHeader = (Header) item;
        mPrimaryText.setText(mBoundHeader.getHeader());
        mSecondaryText.setText(mBoundHeader.getSubHeader());
    }
}
