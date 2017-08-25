package com.jamieadkins.commonutils.ui;

/**
 * Created by jamiea on 25/02/17.
 */

public class Header implements RecyclerViewItem {
    public static final int TYPE_HEADER = 0;

    private String mHeader;
    private String mSubHeader;

    public Header(String header, String subHeader) {
        mHeader = header;
        mSubHeader = subHeader;
    }

    public String getHeader() {
        return mHeader;
    }

    public String getSubHeader() {
        return mSubHeader;
    }

    @Override
    public int getItemType() {
        return TYPE_HEADER;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Header && ((Header) obj).mHeader.equals(mHeader);
    }

    @Override
    public boolean areContentsTheSame(RecyclerViewItem other) {
        return other instanceof Header && mHeader.equals(((Header) other).getHeader());
    }
}
