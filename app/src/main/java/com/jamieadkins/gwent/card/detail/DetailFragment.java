package com.jamieadkins.gwent.card.detail;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.card.LargeCardView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows picture and details of a card.
 */

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final String STATE_CARD_ID = "com.jamieadkins.gwent.cardid";
    private static final String STATE_PATCH = "com.jamieadkins.gwent.patch";
    private DetailContract.Presenter mDetailPresenter;
    private ImageView mCardPicture;
    private LargeCardView mLargeCardView;
    private ViewPager mViewPager;
    private CardImagePagerAdapter mAdapter;

    private String mCardId;
    private String mPatch;

    private boolean mUseLowData = false;

    private SingleObserver<RxDatabaseEvent<CardDetails>> mObserver =
            new BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
                @Override
                public void onSuccess(RxDatabaseEvent<CardDetails> value) {
                    CardDetails card = value.getValue();

                    // Update UI with card details.
                    getActivity().setTitle(card.getName());

                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    for (String variationId : card.getVariations().keySet()) {
                        CardDetails.Variation variation = card.getVariations().get(variationId);
                        StorageReference storageReference;
                        if (mUseLowData) {
                            storageReference = storage.getReferenceFromUrl(
                                    FirebaseUtils.STORAGE_BUCKET +
                                            variation.getArt().getLowImage());
                        } else {
                            storageReference = storage.getReferenceFromUrl(
                                    FirebaseUtils.STORAGE_BUCKET +
                                            variation.getArt().getLowImage());
                        }

                        mAdapter.addItem(storageReference);
                    }

                    mLargeCardView.setCardDetails(card);
                }
            };

    protected static DetailFragment newInstance(String cardId, String patch) {
        DetailFragment fragment = new DetailFragment();
        fragment.mCardId = cardId;
        fragment.mPatch = patch;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCardId = savedInstanceState.getString(STATE_CARD_ID);
            mPatch = savedInstanceState.getString(STATE_PATCH);
        }
    }

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUseLowData = preferences.getBoolean(getString(R.string.pref_low_data_key), false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.card_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetailPresenter.getCard(mCardId)
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CARD_ID, mCardId);
        outState.putString(STATE_PATCH, mPatch);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mDetailPresenter = presenter;
    }
}
