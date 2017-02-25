package com.jamieadkins.gwent.deck.detail;

import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class PublicDeckDetailFragment extends BaseDeckDetailFragment implements DecksContract.View {

    public static PublicDeckDetailFragment newInstance(String deckId) {
        PublicDeckDetailFragment fragment = new PublicDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mDecksPresenter.getDeck(mDeckId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_public_deck_detail;
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(250);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
