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
    public static final String NORTHERN_REALMS = "Northern Realms";
    public static final String SKELLIGE = "Skellige";
    public static final String MONSTERS = "Monsters";
    public static final String SCOIATAEL = "Scoia'tael";
    public static final String NEUTRAL = "Neutral";

    public static final Map<Integer, String> CONVERT_INT;
    public static final Map<String, Integer> CONVERT_STRING;

    static {
        Map<Integer, String> intToString = new HashMap<>();
        intToString.put(0, NEUTRAL);
        intToString.put(1, NORTHERN_REALMS);
        intToString.put(2, SKELLIGE);
        intToString.put(3, SCOIATAEL);
        intToString.put(4, MONSTERS);
        CONVERT_INT = Collections.unmodifiableMap(intToString);

        Map<String, Integer> stringToInt = new HashMap<>();
        stringToInt.put(NEUTRAL, 0);
        stringToInt.put(NORTHERN_REALMS, 1);
        stringToInt.put(SKELLIGE, 2);
        stringToInt.put(SCOIATAEL, 3);
        stringToInt.put(MONSTERS, 4);
        CONVERT_STRING = Collections.unmodifiableMap(stringToInt);
    }
}
