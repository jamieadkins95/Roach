package com.jamieadkins.gwent.data.card;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Filterable;

/**
 * Contains all of the possible rarities.
 */

public class Rarity {
    public static final String COMMON_ID = "Common";
    public static final String RARE_ID = "Rare";
    public static final String EPIC_ID = "Epic";
    public static final String LEGENDARY_ID = "Legendary";

    public static final Filterable COMMON = new Filterable(COMMON_ID, R.string.common);
    public static final Filterable RARE = new Filterable(RARE_ID, R.string.rare);
    public static final Filterable EPIC = new Filterable(EPIC_ID, R.string.epic);
    public static final Filterable LEGENDARY = new Filterable(LEGENDARY_ID, R.string.legendary);

    public static final Filterable[] ALL_RARITIES = new Filterable[]{COMMON, RARE, EPIC, LEGENDARY};

}
