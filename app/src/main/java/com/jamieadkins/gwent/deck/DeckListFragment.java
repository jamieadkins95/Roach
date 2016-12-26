package com.jamieadkins.gwent.deck;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseFragment;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.main.PresenterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * UI fragment that shows a list of the users decks.
 */

public class DeckListFragment extends BaseFragment<Deck> implements DecksContract.View,
        NewDeckDialog.NewDeckDialogListener {
    private static final int REQUEST_CODE = 3414;
    private DecksContract.Presenter mDecksPresenter;

    public DeckListFragment() {
    }

    private PresenterFactory<DecksContract.Presenter> mPresenterFactory =
            new PresenterFactory<DecksContract.Presenter>() {
                @NonNull
                @Override
                public DecksContract.Presenter createPresenter() {
                    return new DecksPresenter();
                }
            };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecksPresenter = getPresenterCache().getPresenter(getClass().getSimpleName(), mPresenterFactory);
        setRecyclerViewAdapter(new DeckRecyclerViewAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck_list, container, false);

        setupViews(rootView);

        FloatingActionButton buttonNewDeck =
                (FloatingActionButton) rootView.findViewById(R.id.new_deck);

        buttonNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new NewDeckDialog();
                newFragment.setTargetFragment(DeckListFragment.this, REQUEST_CODE);
                newFragment.show(getActivity().getSupportFragmentManager(),
                        newFragment.getClass().getSimpleName());
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDecksPresenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDecksPresenter.unbindView();
    }

    @Override
    public void onStart() {
        super.onStart();
        onLoadData();
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mDecksPresenter.getDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    public void onStop() {
        super.onStop();
        getRecyclerViewAdapter().clear();
        mDecksPresenter.stop();
    }

    @Override
    public void createNewDeck(String name, String faction) {
        mDecksPresenter.createNewDeck(name, faction);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        setLoading(active);
    }
}
