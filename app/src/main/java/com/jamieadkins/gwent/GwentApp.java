package com.jamieadkins.gwent;

import android.app.Application;

/**
 * Created by jamiea on 28/02/17.
 */

public class GwentApp extends Application implements InteractorContainerProvider {
    private InteractorContainer mInteractorContainer;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the firebase implementation of the InteractorContainer interface
        mInteractorContainer = new FirebaseInteractorContainer();
    }

    @Override
    public InteractorContainer getInteractorContainer() {
        return mInteractorContainer;
    }
}
