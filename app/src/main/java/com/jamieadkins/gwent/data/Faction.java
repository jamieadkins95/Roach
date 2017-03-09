package com.jamieadkins.gwent.data;

import com.jamieadkins.gwent.R;

/**
 * Contains all of the possible factions.
 */

public class Faction {
    public static final String NORTHERN_REALMS_ID = "Northern Realms";
    public static final String SKELLIGE_ID = "Skellige";
    public static final String SCOIATAEL_ID = "Scoiatael";
    public static final String NEUTRAL_ID = "Neutral";
    public static final String NILFGAARD_ID = "Nilfgaard";
    public static final String MONSTERS_ID = "Monster";

    public static final Filterable NORTHERN_REALMS = new Filterable(NORTHERN_REALMS_ID, R.string.northern_realms);
    public static final Filterable SKELLIGE = new Filterable(SKELLIGE_ID, R.string.skellige);
    public static final Filterable SCOIATAEL = new Filterable(SCOIATAEL_ID, R.string.scoiatael);
    public static final Filterable NEUTRAL = new Filterable(NEUTRAL_ID, R.string.neutral);
    public static final Filterable NILFGAARD = new Filterable(NILFGAARD_ID, R.string.nilfgaard);
    public static final Filterable MONSTERS = new Filterable(MONSTERS_ID, R.string.monster);

    public static final Filterable[] ALL_FACTIONS = new Filterable[]{
            MONSTERS, NORTHERN_REALMS, SCOIATAEL, SKELLIGE, NILFGAARD, NEUTRAL};
}
