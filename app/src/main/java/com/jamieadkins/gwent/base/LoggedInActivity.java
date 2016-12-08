package com.jamieadkins.gwent.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jamieadkins.gwent.R;
import com.jamieadkins.jgaw.GwentApiClient;

public abstract class LoggedInActivity extends BaseActivity {
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            mUserId = auth.getCurrentUser().getUid();
        } else {
            // User is not signed in.
            returnToSignInScreen();
        }
    }

    public String getFirebaseUserId() {
        return mUserId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // User is now signed out.
                                returnToSignInScreen();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void returnToSignInScreen() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
