package com.jamieadkins.gwent.filter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.bus.CloseFilterMenuEvent;
import com.jamieadkins.gwent.bus.RxBus;

import java.util.List;

public class FilterBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private List<FilterableItem> mItems;

    public static FilterBottomSheetDialogFragment newInstance(List<FilterableItem> items) {
        FilterBottomSheetDialogFragment dialogFragment = new FilterBottomSheetDialogFragment();
        dialogFragment.mItems = items;
        return dialogFragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.recycler_view_only, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter();
        adapter.setFilters(mItems);
        recyclerView.setAdapter(adapter);

        dialog.setContentView(contentView);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        RxBus.INSTANCE.post(new CloseFilterMenuEvent());
    }
}
