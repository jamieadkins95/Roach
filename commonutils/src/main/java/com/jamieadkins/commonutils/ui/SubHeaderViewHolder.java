package com.jamieadkins.commonutils.ui;

import android.view.View;
import android.widget.TextView;

import com.jamieadkins.commonutils.R;

/**
 * Created by jamiea on 25/02/17.
 */

public class SubHeaderViewHolder extends BaseViewHolder {
    private SubHeader mBoundSubHeader;

    private final TextView mPrimaryText;

    public SubHeaderViewHolder(View itemView) {
        super(itemView);
        mPrimaryText = (TextView) itemView.findViewById(R.id.subheader_text);
    }

    @Override
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mBoundSubHeader = (SubHeader) item;
        mPrimaryText.setText(mBoundSubHeader.getText());
    }
}
