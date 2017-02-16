package com.jamieadkins.gwent.card.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.LargeCardView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows picture and details of a card.
 */

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter mDetailPresenter;
    private ImageView mCardPicture;
    private LargeCardView mLargeCardView;
    private ViewPager mViewPager;
    private CardImagePagerAdapter mAdapter;

    private Observer<RxDatabaseEvent<CardDetails>> mObserver = new Observer<RxDatabaseEvent<CardDetails>>() {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RxDatabaseEvent<CardDetails> value) {
            switch (value.getEventType()) {
                case ADDED:
                    CardDetails card = value.getValue();

                    // Update UI with card details.
                    getActivity().setTitle(card.getName());

                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    for (String variationId : card.getVariations().keySet()) {
                        CardDetails.Variation variation = card.getVariations().get(variationId);
                        StorageReference storageReference = storage.getReferenceFromUrl(
                                FirebaseUtils.STORAGE_BUCKET +
                                        variation.getArt().getFullsizeImageUrl());

                        mAdapter.addItem(storageReference);
                    }

                    mLargeCardView.setCardDetails(card);
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            setLoadingIndicator(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mCardPicture = (ImageView) rootView.findViewById(R.id.card_image);
        mLargeCardView = (LargeCardView) rootView.findViewById(R.id.card_details);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);

        mAdapter = new CardImagePagerAdapter(getActivity());
        mViewPager.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetailPresenter.getCard(mDetailPresenter.getCardId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDetailPresenter.onStop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mDetailPresenter = presenter;
    }
}
