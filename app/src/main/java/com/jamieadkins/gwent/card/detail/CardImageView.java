package com.jamieadkins.gwent.card.detail;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jamieadkins.gwent.R;

/**
 * Wrapper for our card detail view.
 */

public class CardImageView extends LinearLayout implements RequestListener {
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView mImageView;

    public CardImageView(Context context) {
        super(context);
        initialiseView();
    }

    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialiseView();
    }

    public CardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialiseView();
    }

    protected void initialiseView() {
        inflateView();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshContainer);
        mRefreshLayout.setColorSchemeResources(R.color.gwentAccent);
        mImageView = (ImageView) findViewById(R.id.card_image_view);
        setLoading(true);
    }

    protected void inflateView() {
        inflate(getContext(), R.layout.card_image_view, this);
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        onNoArtAvailable();
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        setLoading(false);
        return false;
    }

    private void onNoArtAvailable() {
        findViewById(R.id.no_art).setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        setLoading(false);
    }

    private void setLoading(boolean loading) {
        mRefreshLayout.setRefreshing(loading);
        mRefreshLayout.setEnabled(loading);
    }

    protected void setCardImage(String image) {
        if (image != null) {
            Glide.with(getContext())
                    .load(image)
                    .listener(this)
                    .fitCenter()
                    .into(mImageView);
        } else {
            onNoArtAvailable();
        }
    }
}
