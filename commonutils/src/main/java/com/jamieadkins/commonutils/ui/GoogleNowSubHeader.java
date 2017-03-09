package com.jamieadkins.commonutils.ui;

/**
 * Created by jamiea on 03/03/17.
 */

public class GoogleNowSubHeader extends SubHeader {
    public static final int TYPE_GOOGLE_NOW_SUB_HEADER = 2;

    private int mColorResource;

    public GoogleNowSubHeader(String text, int colourResource) {
        super(text);
        mColorResource = colourResource;
    }

    public int getColour() {
        return mColorResource;
    }

    @Override
    public int getItemType() {
        return TYPE_GOOGLE_NOW_SUB_HEADER;
    }
}
