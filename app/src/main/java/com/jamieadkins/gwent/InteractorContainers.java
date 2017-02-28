package com.jamieadkins.gwent;

import android.content.Context;

/**
 * Created by jamiea on 28/02/17.
 */

public class InteractorContainers {
    private InteractorContainers() {
        throw new RuntimeException("Instances are not allowed for this class");
    }

    public static InteractorContainer getFromApp(Context context) {
        Context app = context.getApplicationContext();
        InteractorContainerProvider provider = (InteractorContainerProvider) app;
        return provider.getInteractorContainer();
    }
}
