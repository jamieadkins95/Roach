package com.jamieadkins.gwent.data;

import com.jamieadkins.gwent.R;

/**
 * Contains all of the possible loyalties.
 */

public class Loyalty {
    public static final String LOYAL_ID = "Loyal";
    public static final String DISLOYAL_ID = "Disloyal";

    public static final Filterable LOYAL = new Filterable(LOYAL_ID, R.string.loyal);
    public static final Filterable DISLOYAL = new Filterable(DISLOYAL_ID, R.string.disloyal);

    public static final Filterable[] ALL_LOYALTIES = new Filterable[]{LOYAL, DISLOYAL};
}
