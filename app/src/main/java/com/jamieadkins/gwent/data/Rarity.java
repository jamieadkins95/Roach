package com.jamieadkins.gwent.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all of the possible rarities.
 */

public class Rarity {
    public static final String COMMON = "Common";
    public static final String EPIC = "Epic";
    public static final String LEGENDARY = "Legendary";
    public static final String RARE = "Rare";

    public static final String[] ALL_RARITIES = new String[] {COMMON, RARE, EPIC, LEGENDARY};

    public static final Map<Integer, String> CONVERT_INT;
    public static final Map<String, Integer> CONVERT_STRING;

    static {
        Map<Integer, String> intToString = new HashMap<>();
        intToString.put(0, COMMON);
        intToString.put(1, RARE);
        intToString.put(2, EPIC);
        intToString.put(3, LEGENDARY);
        CONVERT_INT = Collections.unmodifiableMap(intToString);

        Map<String, Integer> stringToInt = new HashMap<>();
        stringToInt.put(COMMON, 0);
        stringToInt.put(RARE, 1);
        stringToInt.put(EPIC, 2);
        stringToInt.put(LEGENDARY, 3);
        CONVERT_STRING = Collections.unmodifiableMap(stringToInt);
    }
}
