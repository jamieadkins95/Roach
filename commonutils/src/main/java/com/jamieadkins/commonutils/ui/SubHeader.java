package com.jamieadkins.commonutils.ui;

/**
 * Created by jamiea on 25/02/17.
 */

public class SubHeader implements RecyclerViewItem {
    public static final int TYPE_SUB_HEADER = 1;

    private String mText;

    public SubHeader(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public int getItemType() {
        return TYPE_SUB_HEADER;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubHeader && ((SubHeader) obj).getText().equals(mText);
    }

    @Override
    public boolean areContentsTheSame(RecyclerViewItem other) {
        return other instanceof SubHeader && mText.equals(((SubHeader) other).getText());
    }
}
