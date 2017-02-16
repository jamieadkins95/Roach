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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Faction;

/**
 * Prompts user for new deck name and faction.
 */

public class NewDeckDialog extends DialogFragment {

    public interface NewDeckDialogListener {
        void createNewDeck(String name, String faction);
    }

    NewDeckDialogListener mListener;

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

        Spinner faction = (Spinner) rootView.findViewById(R.id.factions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.factions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faction.setAdapter(adapter);

        builder.setView(rootView)
                .setTitle(R.string.new_deck)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText deckName = (EditText) rootView.findViewById(R.id.deck_name);
                        Spinner faction = (Spinner) rootView.findViewById(R.id.factions);
                        mListener.createNewDeck(deckName.getText().toString(),
                                Faction.ALL_FACTIONS[faction.getSelectedItemPosition()]);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }
}
