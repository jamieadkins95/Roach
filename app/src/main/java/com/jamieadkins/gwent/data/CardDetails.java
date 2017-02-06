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
    private String ingameId;
    private String name;
    private String info;
    private String faction;
    private List<String> lane;
    private String type;
    private List<String> loyalty;
    private int strength;
    private String flavor;
    private boolean released;
    private List<Variation> variations;
    private List<String> category;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public void setIngameId(String ingameId) {
        this.ingameId = ingameId;
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getIngameId() {
        return ingameId;
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

    public int getStrength() {
        return strength;
    }

    public List<String> getLane() {
        return lane;
    }

    public String getFlavor() {
        return flavor;
    }

    public boolean isReleased() {
        return released;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    @Exclude
    public String getImage() {
        for (Variation variation : variations) {
            return variation.getArt().getFullsizeImageUrl();
        }

        return "http://media-seawolf.cursecdn.com/avatars/thumbnails/3/910/800/1048/0icon.png";
    }

    @Exclude
    public String getRarity() {
        for (Variation variation : variations) {
            return variation.getRarity();
        }

        return Rarity.COMMON;
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
    }

    public static class Art {
        private String fullsizeImageUrl;
        private String thumbnailImageUrl;
        private String artist;

        public Art() {
            // Required empty constructor for Firebase.
        }

        public String getFullsizeImageUrl() {
            return fullsizeImageUrl;
        }

        public String getArtist() {
            return artist;
        }

        public String getThumbnailImageUrl() {
            return thumbnailImageUrl;
        }
    }
}
