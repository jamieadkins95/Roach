package com.jamieadkins.gwent.data;

import com.jamieadkins.gwent.R;

/**
 * Contains all of the possible groups.
 */

public class Type {
    public static final String BRONZE_ID = "Bronze";
    public static final String SILVER_ID = "Silver";
    public static final String GOLD_ID = "Gold";
    public static final String LEADER_ID = "Leader";

    public static final Filterable BRONZE = new Filterable(BRONZE_ID, R.string.bronze);
    public static final Filterable SILVER = new Filterable(SILVER_ID, R.string.silver);
    public static final Filterable GOLD = new Filterable(GOLD_ID, R.string.gold);
    public static final Filterable LEADER = new Filterable(LEADER_ID, R.string.leader);

    public static final Filterable[] ALL_TYPES = new Filterable[]{BRONZE, SILVER, GOLD, LEADER};

}
