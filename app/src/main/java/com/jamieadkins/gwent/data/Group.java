package com.jamieadkins.gwent.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all of the possible groups.
 */

public class Group {
    public static final String BRONZE = "Bronze";
    public static final String SILVER = "Silver";
    public static final String GOLD = "Gold";
    public static final String LEADER = "Leader";

    public static final Map<Integer, String> CONVERT_INT;
    public static final Map<String, Integer> CONVERT_STRING;

    static {
        Map<Integer, String> intToString = new HashMap<>();
        intToString.put(0, BRONZE);
        intToString.put(1, SILVER);
        intToString.put(2, GOLD);
        intToString.put(3, LEADER);
        CONVERT_INT = Collections.unmodifiableMap(intToString);

        Map<String, Integer> stringToInt = new HashMap<>();
        stringToInt.put(BRONZE, 0);
        stringToInt.put(SILVER, 1);
        stringToInt.put(GOLD, 2);
        stringToInt.put(LEADER, 3);
        CONVERT_STRING = Collections.unmodifiableMap(stringToInt);
    }
}
