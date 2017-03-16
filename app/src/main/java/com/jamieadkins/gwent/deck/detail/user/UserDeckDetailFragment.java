package com.jamieadkins.gwent.deck.detail.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.detail.BaseDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.filter.FilterableItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class UserDeckDetailFragment extends BaseDeckDetailFragment
        implements DecksContract.View {

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

    private List<CardDetails> mPotentialLeaders;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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

    public static UserDeckDetailFragment newInstance(String deckId, String factionId) {
        UserDeckDetailFragment fragment = new UserDeckDetailFragment();
        fragment.mDeckId = deckId;
        fragment.mFactionId = factionId;
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_deck_detail;
    }

    @Override
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withDeckControls(mButtonListener)
                .build();
    }

    @Override
    public CardFilter initialiseCardFilter() {
        CardFilter filter = new CardFilter();
        filter.put(Type.LEADER_ID, false);
        filter.setCollectibleOnly(true);
        for (Filterable faction : Faction.ALL_FACTIONS) {
            if (!faction.getId().equals(mFactionId)) {
                filter.put(faction.getId(), false);
            }
        }
        filter.put(Faction.NEUTRAL_ID, true);
        filter.setCurrentFilterAsBase();
        return filter;
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        loadCardData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mBottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            setupFilterMenu(menu, inflater);
        }
        if (mPotentialLeaders != null && mPotentialLeaders.size() == 3) {

            String key = getString(R.string.pref_locale_key);
            String locale = PreferenceManager.getDefaultSharedPreferences(getContext())
                    .getString(key, getString(R.string.default_locale));

            inflater.inflate(R.menu.deck_builder, menu);
            menu.findItem(R.id.action_leader_1).setTitle(mPotentialLeaders.get(0).getName(locale));
            menu.findItem(R.id.action_leader_2).setTitle(mPotentialLeaders.get(1).getName(locale));
            menu.findItem(R.id.action_leader_3).setTitle(mPotentialLeaders.get(2).getName(locale));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<FilterableItem> filterableItems = new ArrayList<>();
        String filteringOn;
        Filterable[] filterItems;

        switch (item.getItemId()) {
            case R.id.filter_reset:
                getCardFilter().clearFilters();
                onCardFilterUpdated();
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                Filterable faction = Faction.NORTHERN_REALMS;
                switch (mFactionId) {
                    case Faction.MONSTERS_ID:
                    faction = Faction.MONSTERS;
                    break;
                case Faction.NORTHERN_REALMS_ID:
                    faction = Faction.NORTHERN_REALMS;
                    break;
                case Faction.SCOIATAEL_ID:
                    faction = Faction.SCOIATAEL;
                    break;
                case Faction.SKELLIGE_ID:
                    faction = Faction.SKELLIGE;
                    break;
                case Faction.NILFGAARD_ID:
                    faction = Faction.NILFGAARD;
                    break;
                }
                filterItems = new Filterable[] {faction, Faction.NEUTRAL};
                break;
            case R.id.filter_rarity:
                filteringOn = getString(R.string.rarity);
                filterItems = Rarity.ALL_RARITIES;
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                filterItems = new Filterable[] {Type.BRONZE, Type.SILVER, Type.GOLD};
                break;
            case R.id.action_leader_1:
                mDecksPresenter.setLeader(mDeck, mPotentialLeaders.get(0));
                return true;
            case R.id.action_leader_2:
                mDecksPresenter.setLeader(mDeck, mPotentialLeaders.get(1));
                return true;
            case R.id.action_leader_3:
                mDecksPresenter.setLeader(mDeck, mPotentialLeaders.get(2));
                return true;
            case R.id.action_rename:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_edit_text, null);
                final EditText input = (EditText) view.findViewById(R.id.edit_text);
                input.setText(mDeck.getName());
                input.setHint(R.string.new_name);
                builder.setView(view)
                        .setTitle(R.string.rename)
                        .setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDecksPresenter.renameDeck(mDeck, input.getText().toString());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDecksPresenter.deleteDeck(mDeck);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        for (Filterable filterable : filterItems) {
            filterableItems.add(new FilterableItem(
                    filterable.getId(),
                    getString(filterable.getName()),
                    getCardFilter().get(filterable.getId())));
        }

        showFilterMenu(filteringOn, filterableItems);
        return true;
    }

    @Override
    public void onCardFilterUpdated() {
        mCardDatabaseAdapter.clear();
        loadCardData();
    }

    private void loadCardData() {
        mDecksPresenter.getCards(getCardFilter())
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

        if (mPotentialLeaders == null) {
            mPotentialLeaders = new ArrayList<>();
            mDecksPresenter.getLeadersForFaction(deck.getFactionId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<RxDatabaseEvent<CardDetails>>() {
                        @Override
                        public void onNext(RxDatabaseEvent<CardDetails> value) {
                            mPotentialLeaders.add(value.getValue());
                        }

                        @Override
                        public void onComplete() {
                            if (getActivity() != null) {
                                getActivity().invalidateOptionsMenu();
                            }
                        }
                    });
        }
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
