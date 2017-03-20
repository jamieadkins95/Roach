package com.jamieadkins.gwent.base;

import android.view.View;

/**
 * Created by jamiea on 20/03/17.
 */

public interface SnackbarShower {
    void showSnackbar(String message);

    void showSnackbar(String message, String actionString, View.OnClickListener action);
}
