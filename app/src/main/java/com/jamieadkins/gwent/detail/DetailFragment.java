package com.jamieadkins.gwent.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.LargeCardView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.main.PresenterCache;
import com.jamieadkins.gwent.main.PresenterFactory;

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

    private PresenterFactory<DetailContract.Presenter> mPresenterFactory =
            new PresenterFactory<DetailContract.Presenter>() {
                @NonNull
                @Override
                public DetailContract.Presenter createPresenter() {
                    return new DetailPresenter();
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
                    Glide.with(getActivity())
                            .load(value.getValue().getImage())
                            .fitCenter()
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailPresenter = PresenterCache.getInstance()
                .getPresenter(getClass().getSimpleName(), mPresenterFactory);
    }

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDetailPresenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDetailPresenter.unbindView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetailPresenter.getCard(((DetailActivity) getActivity()).getCardId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }
}
