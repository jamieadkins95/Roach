package com.jamieadkins.gwent.base;

import android.os.Bundle;

import com.jamieadkins.jgaw.GwentApiClient;

public abstract class GwentApiActivity extends BaseActivity {
    private GwentApiClient mGwentApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGwentApiClient = new GwentApiClient();
    }

    public GwentApiClient getGwentApiClient() {
        return mGwentApiClient;
    }
}
