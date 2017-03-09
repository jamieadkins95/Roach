package com.jamieadkins.gwent.data;

/**
 * Created by jamiea on 01/03/17.
 */

public class Filterable {
    private String mId;
    private int mName;

    protected Filterable(String id, int nameResource) {
        mId = id;
        mName = nameResource;
    }

    public String getId() {
        return mId;
    }

    public int getName() {
        return mName;
    }
}
