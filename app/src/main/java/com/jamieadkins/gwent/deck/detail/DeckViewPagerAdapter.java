package com.jamieadkins.gwent.deck.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

/**
 * ViewPager adapter that holds card images.
 */

public class DeckViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_COUNT = 2;
    private final String mDeckId;

    public DeckViewPagerAdapter(FragmentManager fragmentManager, String deckId) {
        super(fragmentManager);
        mDeckId = deckId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = UserDeckDetailFragment.newInstance(mDeckId);

        new DecksPresenter(
                (DecksContract.View) fragment,
                new DecksInteractorFirebase(),
                CardsInteractorFirebase.getInstance(),
                new PatchInteractorFirebase());
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
