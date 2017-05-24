package com.jamieadkins.gwent.card.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.SnackbarShower;

/**
 * UI fragment that shows a list of the users decks.
 */

public class CardListFragment extends BaseCardListFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.card_database));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            SnackbarShower snackbarShower = (SnackbarShower) getActivity();
            if (snackbarShower != null) {
                snackbarShower.showSnackbar(getString(R.string.open_beta),
                        getString(R.string.info),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.open_beta_message)
                                        .setTitle(R.string.open_beta)
                                .setNegativeButton(getString(android.R.string.ok), null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
            }
        }
    }
}
