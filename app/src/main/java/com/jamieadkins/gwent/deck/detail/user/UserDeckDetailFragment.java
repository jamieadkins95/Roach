package com.jamieadkins.gwent.deck.detail.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.detail.BaseDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment
        implements DecksContract.View, CardFilterListener {

    protected interface DeckBuilderListener {
        void onDeckBuilderStateChanged(boolean open);
    }

    DeckDetailCardViewHolder.DeckDetailButtonListener mButtonListener =
            new DeckDetailCardViewHolder.DeckDetailButtonListener() {
                @Override
                public void addCard(CardDetails card) {
                    mDecksPresenter.addCardToDeck(mDeck, card);
                }

                @Override
                public void removeCard(CardDetails card) {
                    mDecksPresenter.removeCardFromDeck(mDeck, card);
                }
            };

    private BottomSheetBehavior mBottomSheet;
    private DeckBuilderListener mDeckBuilderListener;
    private SwipeRefreshLayout mCardDatabaseRefeshLayout;
    private RecyclerView mCardDatabaseRecyclerView;
    private GwentRecyclerViewAdapter mCardDatabaseAdapter;
    private FloatingActionButton mAddCardButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((CardFilterProvider) context).registerCardFilterListener(this);
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);

        mBottomSheet = BottomSheetBehavior.from(bottomSheet);
        mBottomSheet.setPeekHeight(0);
        mBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mDeckBuilderListener.onDeckBuilderStateChanged(false);
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mDeckBuilderListener.onDeckBuilderStateChanged(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mAddCardButton = (FloatingActionButton) rootView.findViewById(R.id.deck_add);
        mAddCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeckBuilderMenu();
            }
        });

        mCardDatabaseRefeshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.cardRefreshContainer);
        mCardDatabaseRecyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(mCardDatabaseRecyclerView.getContext());
        mCardDatabaseRecyclerView.setLayoutManager(linearLayoutManager);
        mCardDatabaseAdapter = onBuildRecyclerView();
        mCardDatabaseRecyclerView.setAdapter(mCardDatabaseAdapter);
    }

    public static UserDeckDetailFragment newInstance(String deckId) {
        UserDeckDetailFragment fragment = new UserDeckDetailFragment();
        fragment.mDeckId = deckId;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_deck_detail;
    }

    @Override
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.DECK)
                .withDeckButtonListener(mButtonListener)
                .build();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        loadCardData();
    }

    @Override
    public void onCardFilterUpdated() {
        mCardDatabaseAdapter.clear();
        loadCardData();
    }

    private void loadCardData() {
        mDecksPresenter.getCards(((CardFilterProvider) getActivity()).getCardFilter())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RxDatabaseEvent<CardDetails>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mCardDatabaseRefeshLayout.setEnabled(true);
                        mCardDatabaseRefeshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onNext(RxDatabaseEvent<CardDetails> value) {
                        switch (value.getEventType()) {
                            case ADDED:
                                mCardDatabaseAdapter.addItem(value.getValue());
                                break;
                            case REMOVED:
                                mCardDatabaseAdapter.removeItem(value.getValue());
                                break;
                        }
                    }

                    @Override
                    public void onComplete() {
                        mCardDatabaseRefeshLayout.setRefreshing(false);
                        mCardDatabaseRefeshLayout.setEnabled(false);
                    }
                });
    }

    @Override
    protected void onDeckLoaded(Deck deck) {
        super.onDeckLoaded(deck);
        getRecyclerViewAdapter().setDeck(deck);
        mCardDatabaseAdapter.setDeck(deck);
    }

    protected void setDeckBuilderListener(DeckBuilderListener listener) {
        mDeckBuilderListener = listener;
    }

    protected void closeDeckBuilderMenu() {
        mBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mAddCardButton.show();
    }

    protected void openDeckBuilderMenu() {
        mBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        mAddCardButton.hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDeckBuilderListener = null;
    }
}
