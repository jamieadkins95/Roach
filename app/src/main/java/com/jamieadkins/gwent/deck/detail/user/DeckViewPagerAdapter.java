package com.jamieadkins.gwent.deck.detail.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jamieadkins.gwent.card.list.CardListFragment;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

/**
 * ViewPager adapter that holds card images.
 */

public class DeckViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_COUNT = 2;
    private static final int DECK_INDEX = 0;
    private static final int CARD_DB_INDEX = 1;
    private final String mDeckId;
    private final String mPatch;

    public DeckViewPagerAdapter(FragmentManager fragmentManager, String deckId, String patch) {
        super(fragmentManager);
        mDeckId = deckId;
        mPatch = patch;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case DECK_INDEX:
                fragment = UserDeckDetailFragment.newInstance(mDeckId);
                new DecksPresenter(
                        (DecksContract.View) fragment,
                        new DecksInteractorFirebase(),
                        CardsInteractorFirebase.getInstance(mPatch),
                        new PatchInteractorFirebase());
                break;
            case CARD_DB_INDEX:
                fragment = UserDeckDetailFragment.newInstance(mDeckId);
                new DecksPresenter(
                        (DecksContract.View) fragment,
                        new DecksInteractorFirebase(),
                        CardsInteractorFirebase.getInstance(mPatch),
                        new PatchInteractorFirebase());
                break;
            default:
                throw new RuntimeException("Fragment " + position + " not implemented");
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "This Deck";
            case 1:
                return "Card Database";
            default:
                throw new RuntimeException("Tab not implemented");
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
