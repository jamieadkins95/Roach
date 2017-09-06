package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;

import java.util.List;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class CardDetails implements RecyclerViewItem {
    private static final String DEFAULT_LOCALE = "en-US";

    private List<String> categories;
    private String faction;
    private Map<String, String> flavor;
    private Map<String, String> info;
    private String ingameId;
    private List<String> loyalties;
    private Map<String, String> name;
    private List<String> positions;
    private List<String> related;
    private boolean released;
    private int strength;
    private String type;
    private Map<String, Variation> variations;

    private String patch;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    @Exclude
    public String getName(String locale) {
        if (name != null) {
            return name.get(locale);
        } else {
            return "";
        }
    }

    @Exclude
    public String getInfo(String locale) {
        if (info != null) {
            return info.get(locale);
        } else {
            return "";
        }
    }

    @Exclude
    public String getFlavor(String locale) {
        if (flavor != null) {
            return flavor.get(locale);
        } else {
            return "";
        }
    }

    public String getFaction() {
        return faction;
    }

    public String getIngameId() {
        return ingameId;
    }

    public String getType() {
        return type;
    }

    public int getStrength() {
        return strength;
    }

    public boolean isReleased() {
        return released;
    }

    public Map<String, Variation> getVariations() {
        return variations;
    }

    public List<String> getRelated() {
        return related;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardDetails) {
            return ingameId.equals(((CardDetails) obj).getIngameId());
        } else {
            return false;
        }
    }

    @Exclude
    @Override
    public String toString() {
        return name.get(DEFAULT_LOCALE);
    }

    @Exclude
    public String getPatch() {
        return patch;
    }

    @Exclude
    public void setPatch(String patch) {
        this.patch = patch;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Map<String, String> getFlavor() {
        return flavor;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public List<String> getLoyalties() {
        return loyalties;
    }

    public Map<String, String> getName() {
        return name;
    }

    public List<String> getPositions() {
        return positions;
    }

    @Exclude
    public String getImage() {
        for (String key : variations.keySet()) {
            return variations.get(key).getArt().getLow();
        }

        return "http://media-seawolf.cursecdn.com/avatars/thumbnails/3/910/800/1048/0icon.png";
    }

    @Exclude
    public String getRarity() {
        for (String key : variations.keySet()) {
            return variations.get(key).getRarity();
        }

        return Rarity.COMMON_ID;
    }

    public static class Variation {

        private String availability;
        private String rarity;
        private Art art;
        private Map<String, Integer> craft;
        private Map<String, Integer> mill;
        private boolean collectible;
        private String variationId;

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

        public Map<String, Integer> getCraft() {
            return craft;
        }

        public Map<String, Integer> getMill() {
            return mill;
        }

        public boolean isCollectible() {
            return collectible;
        }

        public String getVariationId() {
            return variationId;
        }

        @Exclude
        public static int getVariationNumber(String variationId) {
            return Integer.valueOf(variationId.substring(
                    Math.max(variationId.length() - 2, 0),
                    Math.max(variationId.length() - 1, 0)
            )) + 1;
        }
    }

    public static class Art {
        private String original;
        private String high;
        private String medium;
        private String low;
        private String thumbnail;

        private String artist;

        public Art() {
            // Required empty constructor for Firebase.
        }

        public String getArtist() {
            return artist;
        }

        public String getOriginal() {
            return original;
        }

        public String getHigh() {
            return high;
        }

        public String getMedium() {
            return medium;
        }

        public String getLow() {
            return low;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }

    @Override
    public int getItemType() {
        if (type.equals(Type.LEADER_ID)) {
            return GwentRecyclerViewAdapter.TYPE_CARD_LEADER;
        } else {
            return GwentRecyclerViewAdapter.TYPE_CARD;
        }
    }

    @Override
    public boolean areContentsTheSame(RecyclerViewItem other) {
        return other instanceof CardDetails
                && getInfo(DEFAULT_LOCALE).equals(((CardDetails) other).getInfo(DEFAULT_LOCALE));
    }
}
