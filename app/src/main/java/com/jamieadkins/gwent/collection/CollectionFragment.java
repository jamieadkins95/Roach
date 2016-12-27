package com.jamieadkins.gwent.collection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.card.BaseCardListFragment;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.card.CardsPresenter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.main.MainActivity;
import com.jamieadkins.gwent.main.PresenterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CollectionFragment extends BaseCardListFragment {

    public CollectionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CardRecyclerViewAdapter(CardRecyclerViewAdapter.Detail.COLLECTION));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    public void setupViews(View rootView) {
        super.setupViews(rootView);

        FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.new_keg);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open keg.
            }
        });
    }
}
