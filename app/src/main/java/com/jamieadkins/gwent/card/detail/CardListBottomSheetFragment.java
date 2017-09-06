package com.jamieadkins.gwent.card.detail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.commonutils.ui.SubHeader;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.bus.SnackbarBundle;
import com.jamieadkins.gwent.bus.SnackbarRequest;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows a list of cards in a bottom sheet.
 */

public class CardListBottomSheetFragment extends BottomSheetDialogFragment
        implements CardsContract.View {
    private static final String STATE_CARD_IDS = "com.jamieadkins.gwent.cardids";
    private CardsContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshContainer;
    private GwentRecyclerViewAdapter mAdapter;

    private ArrayList<String> mCardIds;

    public static CardListBottomSheetFragment newInstance(ArrayList<String> cardIds) {
        CardListBottomSheetFragment dialogFragment = new CardListBottomSheetFragment();
        dialogFragment.mCardIds = cardIds;
        return dialogFragment;
    }

    @Override
    public void showItems(@NotNull List<? extends RecyclerViewItem> items) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showGenericErrorMessage() {
        RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.general_error))));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCardIds = savedInstanceState.getStringArrayList(STATE_CARD_IDS);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_related_cards, null);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GwentRecyclerViewAdapter.Builder().build();
        //mAdapter.addItem(0, new SubHeader(getString(R.string.related_cards)));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshContainer = (SwipeRefreshLayout) contentView.findViewById(R.id.refreshContainer);
        mRefreshContainer.setColorSchemeResources(R.color.gwentAccent);

        dialog.setContentView(contentView);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRefreshContainer.setEnabled(true);
        mRefreshContainer.setRefreshing(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(STATE_CARD_IDS, mCardIds);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setLoadingIndicator(boolean loading) {
        mRefreshContainer.setRefreshing(true);
    }
}
