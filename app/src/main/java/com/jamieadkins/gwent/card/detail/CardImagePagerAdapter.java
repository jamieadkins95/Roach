package com.jamieadkins.gwent.card.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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
 * ViewPager adapter that holds card iamges
 */

public class CardImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<StorageReference> mItems;
    // Unfortunate hack we need to do to reference views later.
    List<View> mViews;

    private RequestListener<StorageReference, GlideDrawable> mGlideListener =
            new RequestListener<StorageReference, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, StorageReference model,
                                           Target<GlideDrawable> target, boolean isFirstResource) {
                    // No art available.
                    View view =  mViews.get(mItems.indexOf(model));
                    view.findViewById(R.id.no_art).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.card_image).setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, StorageReference model,
                                               Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            };

    public CardImagePagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_card_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.card_image);
        container.addView(itemView);

        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(mItems.get(position))
                .listener(mGlideListener)
                .centerCrop()
                .into(imageView);

        mViews.add(position, itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public void addItem(StorageReference storageReference) {
        mItems.add(storageReference);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        mViews.remove(position);
    }
}
