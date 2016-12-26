package com.jamieadkins.gwent.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
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
    private String rarity;
    private Map<String, Boolean> position;
    private String type;
    private String loyalty;
    private String group;
    private String image;
    private String strength;
    private boolean collectible;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public CardDetails(String cardid, String name, String info, String faction, String rarity,
                       Map<String, Boolean> position, String group, String strength, String type,
                       String loyalty, String image) {
        this.cardid = cardid;
        this.name = name;
        this.info = info;
        this.rarity = rarity;
        this.faction = faction;
        this.position = position;
        this.group = group;
        this.strength = strength;
        this.type = type;
        this.image = image;
        this.loyalty = loyalty;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
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

    public String getRarity() {
        return rarity;
    }

    public Map<String, Boolean> getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getLoyalty() {
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardid", cardid);
        result.put("name", name);
        result.put("faction", faction);
        result.put("info", info);
        result.put("rarity", rarity);
        result.put("position", position);
        result.put("group", group);
        result.put("strength", strength);
        result.put("type", type);
        result.put("image", image);
        result.put("loyalty", loyalty);
        result.put("collectible", collectible);

        return result;
    }
}
