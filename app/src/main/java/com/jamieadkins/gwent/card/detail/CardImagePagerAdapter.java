package com.jamieadkins.gwent.card.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.storage.StorageReference;
import com.jamieadkins.gwent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager adapter that holds card images.
 */

public class CardImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mItems;

    public CardImagePagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_card_image, container, false);

        CardImageView imageView = (CardImageView) itemView.findViewById(R.id.card_image);
        container.addView(itemView);

        imageView.setCardImage(mItems.get(position));

        return itemView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public void addItem(String imageUrl) {
        boolean alreadyAdded = false;
        for (String url : mItems) {
            if (url.equals(imageUrl)) {
                alreadyAdded = true;
            }
        }

        if (!alreadyAdded) {
            mItems.add(imageUrl);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
