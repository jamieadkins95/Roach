package com.jamieadkins.gwent.data;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Misc utils for firebase.
 */

public class FirebaseUtils {

    private static boolean persistanceSet = false;

    public static FirebaseDatabase getDatabase() {
        if (!persistanceSet) {
            // Enable offline use. This has to be done before any other firebase database work.
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            persistanceSet = true;
        }

        return FirebaseDatabase.getInstance();
    }
}
