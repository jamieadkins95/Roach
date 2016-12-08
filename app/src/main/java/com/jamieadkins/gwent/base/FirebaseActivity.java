package com.jamieadkins.gwent.base;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;

import java.util.ArrayList;

public abstract class FirebaseActivity extends BaseActivity {
    private static final int RC_SIGN_IN = 3294;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
        } else {

            ArrayList<AuthUI.IdpConfig> providers = new ArrayList<>();
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(providers)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setTheme(R.style.Theme_Gwent)
                            .build(),
                    RC_SIGN_IN);
        }
    }
}
