package com.jamieadkins.gwent.filter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.commonutils.ui.SubHeader;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.model.CardColour;
import com.jamieadkins.gwent.model.GwentFaction;
import com.jamieadkins.gwent.model.Loyalty;
import com.jamieadkins.gwent.model.Rarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds card filter checkboxes.
 */

public class FilterBottomSheetDialogFragment<F> extends BottomSheetDialogFragment {
    private FilterUiListener mListener;
    private List<FilterableItem<F>> mItems;
    private String mFilteringOn;

    public interface FilterUiListener {
        void onFilterDismissed(boolean filtersChanged);
        void onFactionFilterChanged(GwentFaction filter, boolean enabled);
        void onColourFilterChanged(CardColour filter, boolean enabled);
        void onRarityFilterChanged(Rarity filter, boolean enabled);
        void onLoyaltyFilterChanged(Loyalty filter, boolean enabled);
    }

    public static FilterBottomSheetDialogFragment newInstance(String filteringOn,
                                                              List<FilterableItem> items,
                                                              FilterUiListener listener) {
        FilterBottomSheetDialogFragment dialogFragment = new FilterBottomSheetDialogFragment();
        dialogFragment.mListener = listener;
        dialogFragment.mItems = items;
        dialogFragment.mFilteringOn = filteringOn;
        return dialogFragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.recycler_view_only, null);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter();
        ArrayList<RecyclerViewItem> items = new ArrayList<>();
        items.add(new SubHeader(mFilteringOn));
        items.addAll(mItems);
        adapter.setItems(items);
        adapter.setFilterListener(mListener);

        recyclerView.setAdapter(adapter);

        dialog.setContentView(contentView);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mListener.onFilterDismissed(true);
        super.onDismiss(dialog);
    }
}
