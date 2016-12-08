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
    private String id;
    private String name;
    private String description;
    private String faction;
    private String rarity;
    private String position;
    private Map<String, Boolean> types;
    private Map<String, Boolean> loyalty;
    private String group;
    private int strength;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public CardDetails(String id, String name, String description, String faction, String rarity,
                       String position, String group, int strength, Map<String, Boolean> types,
                       Map<String, Boolean> loyalty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.faction = faction;
        this.position = position;
        this.group = group;
        this.strength = strength;
        this.types = types;
        this.loyalty = loyalty;
    }

    public String getName() {
        return name;
    }

    public String getFaction() {
        return faction;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getRarity() {
        return rarity;
    }

    public String getPosition() {
        return position;
    }

    public Map<String, Boolean> getTypes() {
        return types;
    }

    public Map<String, Boolean> getLoyalty() {
        return loyalty;
    }

    public String getGroup() {
        return group;
    }

    public int getStrength() {
        return strength;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("faction", faction);
        result.put("description", description);
        result.put("rarity", rarity);
        result.put("position", position);
        result.put("group", group);
        result.put("strength", strength);
        result.put("types", types);
        result.put("loyalty", loyalty);

        return result;
    }
}
