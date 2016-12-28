package com.jamieadkins.gwent.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.BaseCardListFragment;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CollectionFragment extends BaseCardListFragment {

    public CollectionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRecyclerViewAdapter(new CollectionRecyclerViewAdapter(new CollectionCardViewHolder.CollectionButtonListener() {
            @Override
            public void addCard(String cardId) {
                Toast.makeText(getContext(), "Add " + cardId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void removeCard(String cardId) {
                Toast.makeText(getContext(), "Remove " + cardId, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collection;
    }

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
