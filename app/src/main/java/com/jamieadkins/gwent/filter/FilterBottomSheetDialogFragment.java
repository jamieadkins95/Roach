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

import java.util.ArrayList;
import java.util.List;

/**
 * Holds card filter checkboxes.
 */

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private FilterUiListener mListener;
    private List<FilterableItem> mItems;
    private String mFilteringOn;

    public interface FilterUiListener {
        void onFilterChanged(String key, boolean checked);
        void onFilterDismissed(boolean filtersChanged);
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
        super.setupDialog(dialog, style);
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
