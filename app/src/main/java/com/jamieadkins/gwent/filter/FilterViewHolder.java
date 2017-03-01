package com.jamieadkins.gwent.filter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jamieadkins.commonutils.ui.BaseViewHolder;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.filter.FilterableItem;

/**
 * Holds much more detail about a card.
 */

public class FilterViewHolder extends BaseViewHolder implements View.OnClickListener {
    private FilterableItem mFilter;
    private TextView mFilterName;
    private CheckBox mCheckbox;
    private FilterBottomSheetDialogFragment.FilterUiListener mListener;

    public FilterViewHolder(View view, FilterBottomSheetDialogFragment.FilterUiListener listener) {
        super(view);
        mFilterName = (TextView) view.findViewById(R.id.name);
        mCheckbox = (CheckBox) view.findViewById(R.id.checkbox);
        mListener = listener;
    }

    @Override
    public void bindItem(RecyclerViewItem item) {
        super.bindItem(item);
        mFilter = (FilterableItem) item;

        // Initially set to provided data.
        mCheckbox.setChecked(mFilter.isChecked());
        mCheckbox.setOnClickListener(this);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckbox.setChecked(!mCheckbox.isChecked());
                FilterViewHolder.this.onClick(v);
            }
        });

        mFilterName.setText(mFilter.toString());
    }

    @Override
    public void onClick(View v) {
        mListener.onFilterChanged(mFilter.getId(), mCheckbox.isChecked());
    }
}
