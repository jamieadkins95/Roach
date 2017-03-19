package com.jamieadkins.gwent.card.detail;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.card.LargeCardView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.FirebaseUtils;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import java.util.ArrayList;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows picture and details of a card.
 */

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final String STATE_CARD_ID = "com.jamieadkins.gwent.cardid";
    private DetailContract.Presenter mDetailPresenter;
    private ImageView mCardPicture;
    private LargeCardView mLargeCardView;
    private ViewPager mViewPager;
    private CardImagePagerAdapter mAdapter;

    private String mCardId;

    private String mLocale;

    private CardDetails mCard;

    private boolean mUseLowData = false;

    private SingleObserver<RxDatabaseEvent<CardDetails>> mObserver =
            new BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
                @Override
                public void onSuccess(RxDatabaseEvent<CardDetails> value) {
                    getActivity().invalidateOptionsMenu();
                    CardDetails card = value.getValue();
                    mCard = card;

                    // Update UI with card details.
                    getActivity().setTitle(card.getName(mLocale));

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

    protected static DetailFragment newInstance(String cardId) {
        DetailFragment fragment = new DetailFragment();
        fragment.mCardId = cardId;
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
        }

        String key = getString(R.string.pref_locale_key);
        mLocale = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(key, getString(R.string.default_locale));
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

        if (mCard == null) {
            return;
        }

        inflater.inflate(R.menu.card_detail, menu);
        menu.findItem(R.id.action_related).setVisible(mCard.getRelated() != null && BuildConfig.DEBUG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_related:
                ArrayList<String> relatedCards = new ArrayList<>();
                relatedCards.addAll(mCard.getRelated());
                CardListBottomSheetFragment fragment =
                        CardListBottomSheetFragment.newInstance(relatedCards);
                fragment.setPresenter(mDetailPresenter);
                fragment.show(getChildFragmentManager(), "related");
                return true;
            case R.id.action_flag_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_edit_text, null);
                final EditText input = (EditText) view.findViewById(R.id.edit_text);
                input.setHint(R.string.error_description);
                builder.setView(view)
                        .setTitle(R.string.flag_error_title)
                        .setMessage(R.string.flag_error_message)
                        .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDetailPresenter.reportMistake(mCardId, input.getText().toString());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
                return true;
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
        mDetailPresenter.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CARD_ID, mCardId);
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
