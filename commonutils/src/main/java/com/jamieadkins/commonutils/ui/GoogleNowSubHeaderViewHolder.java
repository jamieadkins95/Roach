package com.jamieadkins.commonutils.ui;

import android.view.View;

import com.jamieadkins.commonutils.R;

/**
 * Created by jamiea on 03/03/17.
 */

public class GoogleNowSubHeaderViewHolder extends SubHeaderViewHolder {
    private GoogleNowSubHeader mBoundSubHeader;

    private View mColoredView;

    public GoogleNowSubHeaderViewHolder(View itemView) {
        super(itemView);
        mColoredView = itemView.findViewById(R.id.color_view);
    }

    @Override
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mBoundSubHeader = (GoogleNowSubHeader) item;

        mColoredView.setBackgroundResource(mBoundSubHeader.getColour());
    }
}
