package com.jamieadkins.gwent.deck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Deck;

/**
 * UI fragment that shows a list of the users decks.
 */

public class DeckListFragment extends Fragment implements DecksContract.View {
    private DecksContract.Presenter mDecksPresenter;
    private RecyclerView mDeckListView;
    private DeckRecyclerViewAdapter mViewAdapter;

    public DeckListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewAdapter = new DeckRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck_list, container, false);

        mDeckListView = (RecyclerView) rootView.findViewById(R.id.results);
        setupRecyclerView(mDeckListView);

        FloatingActionButton buttonNewDeck =
                (FloatingActionButton) rootView.findViewById(R.id.new_deck);

        buttonNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDecksPresenter.createNewDeck();
            }
        });

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDecksPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewAdapter.clear();
        mDecksPresenter.stop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showDeck(Deck deck) {
        mViewAdapter.addDeck(deck);
    }

    @Override
    public void setPresenter(DecksContract.Presenter presenter) {
        mDecksPresenter = presenter;
    }
}
