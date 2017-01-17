package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class CardDetails {
    private String cardid;
    private String name;
    private String info;
    private String faction;
    private List<String> lane;
    private String type;
    private List<String> loyalty;
    private String group;
    private String strength;
    private Map<String, String> craft;
    private Map<String, String> mill;
    private boolean collectible;
    private String flavor;
    private List<Variation> variations;
    private List<String> category;


    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getCardid() {
        return cardid;
    }

    public String getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }

    public List<String> getLoyalty() {
        return loyalty;
    }

    public String getGroup() {
        return group;
    }

    public String getStrength() {
        return strength;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public List<String> getLane() {
        return lane;
    }

    public Map<String, String> getCraft() {
        return craft;
    }

    public Map<String, String> getMill() {
        return mill;
    }

    public String getFlavor() {
        return flavor;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    @Exclude
    public String getImage() {
        return variations.get(0).getArt().getFullsizeImageUrl();
    }

    @Exclude
    public String getRarity() {
        return variations.get(0).getRarity();
    }

    public List<String> getCategory() {
        return category;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardid", cardid);
        result.put("name", name);
        result.put("faction", faction);
        result.put("info", info);
        result.put("lane", lane);
        result.put("group", group);
        result.put("strength", strength);
        result.put("type", type);
        result.put("loyalty", loyalty);
        result.put("collectible", collectible);
        result.put("lane", lane);
        result.put("craft", craft);
        result.put("mill", mill);
        result.put("flavor", flavor);
        result.put("variations", variations);
        result.put("category", category);

        return result;
    }

    public static class Variation {

        private String availability;
        private String rarity;
        private Art art;

        public Variation() {
            // Required empty constructor for Firebase.
        }

        public String getAvailability() {
            return availability;
        }

        public String getRarity() {
            return rarity;
        }

        public Art getArt() {
            return art;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("availability", availability);
            result.put("rarity", rarity);
            result.put("art", art);
            return result;
        }
    }

    public static class Art {
        private String fullsizeImageUrl;

        public Art() {
            // Required empty constructor for Firebase.
        }

        public String getFullsizeImageUrl() {
            return fullsizeImageUrl;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("fullsizeImageUrl", fullsizeImageUrl);
            return result;
        }
    }
}
