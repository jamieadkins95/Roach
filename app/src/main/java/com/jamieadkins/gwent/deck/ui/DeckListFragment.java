package com.jamieadkins.gwent.deck.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.cardui.CardRecyclerViewAdapter;
import com.jamieadkins.gwent.model.Deck;

import java.util.ArrayList;

/**
 * UI fragment that shows a list of the users decks.
 */

public class DeckListFragment extends Fragment {
    private RecyclerView mCardListView;
    private CardRecyclerViewAdapter mAdapter;

    public DeckListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);

        mCardListView = (RecyclerView) rootView.findViewById(R.id.results);
        setupRecyclerView(mCardListView);

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(new Deck());
        decks.add(new Deck());
        decks.add(new Deck());
        decks.add(new Deck());
        decks.add(new Deck());
        decks.add(new Deck());
        decks.add(new Deck());
        DeckRecyclerViewAdapter adapter = new DeckRecyclerViewAdapter(decks);
        mCardListView.setAdapter(adapter);
    }
}
