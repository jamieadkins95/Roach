package com.jamieadkins.gwent.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.list.BaseCardListFragment;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase;

import java.util.Map;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CollectionFragment extends BaseCardListFragment<CollectionContract.View> implements CollectionContract.View {
    CollectionContract.Presenter collectionPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.my_collection));
    }

    @Override
    public void setupPresenter() {
        setPresenter(new CollectionPresenter(
                Injection.INSTANCE.provideCollectionInteractor(),
                Injection.INSTANCE.provideCardsInteractor(getContext())));
        collectionPresenter = (CollectionContract.Presenter) getPresenter();
    }

    @Override
    public CardFilter initialiseCardFilter() {
        CardFilter filter = new CardFilter();
        filter.setCollectibleOnly(true);
        filter.setCurrentFilterAsBase();
        return filter;
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

    @Override
    public void showCollection(String key, Map<String, Long> collection) {
        getRecyclerViewAdapter().updateCollection(key, collection);
    }

    @Override
    public GwentRecyclerViewAdapter onBuildRecyclerView() {
        return new GwentRecyclerViewAdapter.Builder()
                .withControls(GwentRecyclerViewAdapter.Controls.COLLECTION)
                .build();
    }
}
