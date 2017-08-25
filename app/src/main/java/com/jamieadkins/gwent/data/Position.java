package com.jamieadkins.gwent.data;

import com.jamieadkins.gwent.R;

/**
 * Contains all of the possible positions.
 */

public class Position {
    public static final String MELEE_ID = "Melee";
    public static final String RANGED_ID = "Ranged";
    public static final String SIEGE_ID = "Siege";
    public static final String EVENT_ID = "Event";

    public static final Filterable MELEE = new Filterable(MELEE_ID, R.string.melee);
    public static final Filterable RANGED = new Filterable(RANGED_ID, R.string.ranged);
    public static final Filterable SIEGE = new Filterable(SIEGE_ID, R.string.siege);
    public static final Filterable EVENT = new Filterable(EVENT_ID, R.string.event);

    public static final Filterable[] ALL_POSITIONS = new Filterable[]{MELEE, RANGED, SIEGE, EVENT};
}
