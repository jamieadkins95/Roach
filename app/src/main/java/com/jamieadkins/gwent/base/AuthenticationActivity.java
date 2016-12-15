package com.jamieadkins.gwent.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.jgaw.GwentApiClient;

import java.util.ArrayList;

public abstract class AuthenticationActivity extends BaseActivity {
    private static final int REQUEST_CODE_SIGN_IN = 3294;
    private static final int REQUEST_CODE_PLAY_SERVICES = 3295;
    private String mUserId;
    private boolean mSignedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        mSignedIn = auth.getCurrentUser() != null;
        if (mSignedIn) {
            mUserId = auth.getCurrentUser().getUid();
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
                                onSignedOut();
                            }
                        });
                return true;
            case R.id.action_sign_in:
                startSignInProcess();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isAuthenticated() {
        return mSignedIn;
    }

    protected void onSignedIn() {
        mSignedIn = true;
    }

    protected void onSignedOut() {
        mSignedIn = false;
        showSnackbar(getString(R.string.sign_out_successful));
    }

    public boolean isPlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        REQUEST_CODE_PLAY_SERVICES).show();
            }

            return false;
        }

        return true;
    }

    public void startSignInProcess() {

        if (!isPlayServicesAvailable()) {
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            showSnackbar(getString(R.string.sign_in_successful));
        } else {
            ArrayList<AuthUI.IdpConfig> providers = new ArrayList<>();
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(providers)
                            .setTosUrl("https://github.com/jamieadkins95/Yennefr/blob/master/TOS.md")
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setTheme(R.style.Theme_Gwent)
                            .build(),
                    REQUEST_CODE_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successful sign in!
                showSnackbar(getString(R.string.sign_in_successful));
                onSignedIn();
                return;
            }

            // Sign in cancelled.
            if (resultCode == RESULT_CANCELED) {
                showSnackbar(getString(R.string.sign_in_cancelled));
                return;
            }

            // No network.
            if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                showSnackbar(getString(R.string.no_internet_connection));
                return;
            }

            // User is not signed in. Show generic failed snackbar.
            showSnackbar(getString(R.string.sign_in_failed));
        }
    }
}
