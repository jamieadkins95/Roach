package com.jamieadkins.gwent.data;

import android.content.Context;

import com.jamieadkins.gwent.R;

/**
 * Contains all of the possible factions.
 */

public class Faction {
    public static final String NORTHERN_REALMS = "northern-realms";
    public static final String SKELLIGE = "skellige";
    public static final String MONSTERS = "monsters";
    public static final String SCOIATAEL = "scoiatael";
    public static final String NEUTRAL = "neutral";

    public static String getFactionIdFromName(String factionName) {
        switch (factionName) {
            case "Northern Realms":
                return NORTHERN_REALMS;
            case "Monsters":
                return MONSTERS;
            case "Skellige":
                return SKELLIGE;
            case "Scoia'tel":
                return SCOIATAEL;
            default:
                return MONSTERS;

        }
    }
}
