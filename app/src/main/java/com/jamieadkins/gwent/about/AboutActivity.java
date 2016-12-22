package com.jamieadkins.gwent.about;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.detail.DetailContract;
import com.jamieadkins.gwent.detail.DetailPresenter;
import com.jamieadkins.gwent.main.MainActivity;

/**
 * Shows card image and details.
 */

public class AboutActivity extends BaseActivity {

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Same behaviour as back button.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
