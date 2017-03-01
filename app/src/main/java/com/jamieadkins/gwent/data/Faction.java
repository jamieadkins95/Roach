package com.jamieadkins.gwent.data;

import android.content.Context;

import com.jamieadkins.gwent.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static final Faction NORTHERN_REALMS = new Faction(NORTHERN_REALMS_ID, R.string.northern_realms);
    public static final Faction SKELLIGE = new Faction(SKELLIGE_ID, R.string.skellige);
    public static final Faction SCOIATAEL = new Faction(SCOIATAEL_ID, R.string.scoiatael);
    public static final Faction NEUTRAL = new Faction(NEUTRAL_ID, R.string.neutral);
    public static final Faction NILFGAARD = new Faction(NILFGAARD_ID, R.string.nilfgaard);
    public static final Faction MONSTERS = new Faction(MONSTERS_ID, R.string.monster);

    public static final Faction[] ALL_FACTIONS = new Faction[] {
            MONSTERS, NORTHERN_REALMS, SCOIATAEL, SKELLIGE, NILFGAARD, NEUTRAL};

    private String mId;
    private int mName;

    private Faction(String id, int nameResource){
        mId = id;
        mName = nameResource;
    }

    public String getId() {
        return mId;
    }

    public int getName() {
        return mName;
    }
}
