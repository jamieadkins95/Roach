package com.jamieadkins.gwent.card.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
    private List<StorageReference> mItems;

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

    public void addItem(StorageReference storageReference) {
        boolean alreadyAdded = false;
        for (StorageReference reference : mItems) {
            if (reference.getPath().equals(storageReference.getPath())) {
                alreadyAdded = true;
            }
        }

        if (!alreadyAdded) {
            mItems.add(storageReference);
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
