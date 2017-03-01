package com.jamieadkins.gwent.deck.list;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Prompts user for new deck name and faction.
 */

public class NewDeckDialog extends DialogFragment {

    public interface NewDeckDialogListener {
        void createNewDeck(String name, String faction, CardDetails leader);
    }

    NewDeckDialogListener mListener;
    DecksContract.Presenter mDeckPresenter;

    public static NewDeckDialog newInstance(DecksContract.Presenter deckPresenter) {
        NewDeckDialog dialog = new NewDeckDialog();
        dialog.mDeckPresenter = deckPresenter;
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NewDeckDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement " + NewDeckDialogListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.dialog_new_deck, null);

        final CardFilter cardFilter = new CardFilter();

        final String[] factions = getActivity().getResources().getStringArray(R.array.factions_array);

        final Spinner leaderSpinner = (Spinner) rootView.findViewById(R.id.leaders);
        final ArrayAdapter<CardDetails> leaderAdapter = new ArrayAdapter<CardDetails>(getActivity(),
                android.R.layout.simple_spinner_item);
        leaderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaderSpinner.setAdapter(leaderAdapter);

        final Spinner factionSpinner = (Spinner) rootView.findViewById(R.id.factions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.factions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        factionSpinner.setAdapter(adapter);
        factionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Faction leader cards only.
                leaderAdapter.clear();
                cardFilter.clearFilters();
                cardFilter.put("Bronze", false);
                cardFilter.put("Silver", false);
                cardFilter.put("Gold", false);
                for (Filterable faction : Faction.ALL_FACTIONS) {
                    if (!faction.getId().equals(Faction.ALL_FACTIONS[i].getId())) {
                        cardFilter.put(faction.getId(), false);
                    }
                }

                mDeckPresenter.getCards(cardFilter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RxDatabaseEvent<CardDetails>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(RxDatabaseEvent<CardDetails> value) {
                                switch (value.getEventType()) {
                                    case ADDED:
                                        leaderAdapter.add(value.getValue());
                                        break;
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(rootView)
                .setTitle(R.string.new_deck)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText deckName = (EditText) rootView.findViewById(R.id.deck_name);
                        Spinner faction = (Spinner) rootView.findViewById(R.id.factions);
                        mListener.createNewDeck(
                                deckName.getText().toString(),
                                Faction.ALL_FACTIONS[faction.getSelectedItemPosition()].getId(),
                                (CardDetails) leaderSpinner.getSelectedItem());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }
}
