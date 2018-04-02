package com.jamieadkins.gwent.filter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jamieadkins.commonutils.mvp2.BasePresenter;
import com.jamieadkins.commonutils.mvp3.MvpBottomSheetDialogFragment;
import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilterBottomSheetDialogFragment extends
        MvpBottomSheetDialogFragment<FilterContract.View> implements FilterContract.View {
    private FilterType filterType;
    private FilterRecyclerViewAdapter adapter = new FilterRecyclerViewAdapter();

    private static final String KEY_FILTER_TYPE = "filter";

    public static FilterBottomSheetDialogFragment newInstance(FilterType filterType) {
        FilterBottomSheetDialogFragment dialogFragment = new FilterBottomSheetDialogFragment();
        dialogFragment.filterType = filterType;
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            filterType = (FilterType) savedInstanceState.getSerializable(KEY_FILTER_TYPE);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.recycler_view_only, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        dialog.setContentView(contentView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_FILTER_TYPE, filterType);
        super.onSaveInstanceState(outState);
    }

    @NotNull
    @Override
    public BasePresenter<FilterContract.View> setupPresenter() {
        return new FilterPresenter(filterType,
                Injection.INSTANCE.provideSchedulerProvider(),
                Injection.INSTANCE.provideFilterRepository());
    }

    @Override
    public void showFilters(@NotNull List<? extends FilterableItem> filterableItems) {
        adapter.setFilters(filterableItems);
    }
}
