package com.jamieadkins.gwent.card.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private RequestListener<StorageReference, GlideDrawable> mGlideListener =
            new RequestListener<StorageReference, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, StorageReference model,
                                           Target<GlideDrawable> target, boolean isFirstResource) {
                    // No art available.
                    getView().findViewById(R.id.no_art).setVisibility(View.VISIBLE);
                    mCardPicture.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, StorageReference model,
                                               Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            };

    private Observer<RxDatabaseEvent<CardDetails>> mObserver = new Observer<RxDatabaseEvent<CardDetails>>() {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RxDatabaseEvent<CardDetails> value) {
            switch (value.getEventType()) {
                case ADDED:
                    // Update UI with card details.
                    getActivity().setTitle(value.getValue().getName());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl(
                            FirebaseUtils.STORAGE_BUCKET + value.getValue().getImage());

                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .listener(mGlideListener)
                            .centerCrop()
                            .into(mCardPicture);

                    mLargeCardView.setCardDetails(value.getValue());
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
