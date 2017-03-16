package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.jamieadkins.commonutils.ui.RecyclerViewItem;
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Class that models what a deck is.
 */
@IgnoreExtraProperties
public class CardDetails implements RecyclerViewItem {
    private String ingameId;
    private Map<String, String> name;
    private Map<String, String> info;
    private String faction;
    private List<String> lane;
    private String type;
    private List<String> loyalty;
    private int strength;
    private Map<String, String> flavor;
    private boolean released;
    private Map<String, Variation> variations;
    private List<String> category;
    private List<String> related;

    private String patch;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public Map<String, String> getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getIngameId() {
        return ingameId;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }

    public List<String> getLoyalty() {
        return loyalty;
    }

    public int getStrength() {
        return strength;
    }

    public List<String> getLane() {
        return lane;
    }

    public Map<String, String> getFlavor() {
        return flavor;
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
        return getName(Locale.US.toString());
    }

    @Exclude
    public String getName(String locale) {
        return name.get(locale);
    }

    @Exclude
    public String getInfo(String locale) {
        return info.get(locale);
    }

    @Exclude
    public String getFlavor(String locale) {
        return flavor.get(locale);
    }

    @Exclude
    public String getPatch() {
        return patch;
    }

    @Exclude
    public void setPatch(String patch) {
        this.patch = patch;
    }

    @Exclude
    public String getImage() {
        for (String key : variations.keySet()) {
            return variations.get(key).getArt().getLowImage();
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

    public List<String> getCategory() {
        return category;
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
        private String highImage;
        private String mediumImage;
        private String lowImage;

        private String artist;

        public Art() {
            // Required empty constructor for Firebase.
        }

        public String getArtist() {
            return artist;
        }

        public String getHighImage() {
            return highImage;
        }

        public String getMediumImage() {
            return mediumImage;
        }

        public String getLowImage() {
            return lowImage;
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
}
