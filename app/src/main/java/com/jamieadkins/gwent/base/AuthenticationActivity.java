package com.jamieadkins.gwent.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.bus.SnackbarBundle;
import com.jamieadkins.gwent.bus.SnackbarRequest;
import com.jamieadkins.gwent.data.FirebaseUtils;

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

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean isAuthenticated() {
        return mSignedIn;
    }

    protected void onSignedIn() {
        mSignedIn = true;
        FirebaseUtils.askForAnalyticsPermission(this);
    }

    protected void onSignedOut() {
        mSignedIn = false;
        RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.sign_out_successful))));
    }

    public void startSignOutProcess() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // User is now signed out.
                        onSignedOut();
                    }
                });
    }

    public boolean checkGooglePlayServices() {
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

        if (!checkGooglePlayServices()) {
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.sign_in_successful))));
        } else {
            ArrayList<AuthUI.IdpConfig> providers = new ArrayList<>();
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(providers)
                            .setLogo(R.drawable.logo)
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
                RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.sign_in_successful))));
                onSignedIn();
                return;
            }

            // Sign in cancelled.
            if (resultCode == RESULT_CANCELED) {
                RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.sign_in_cancelled))));
                return;
            }

            // No network.
            if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.no_internet_connection))));
                return;
            }

            // User is not signed in. Show generic failed snackbar.
            RxBus.INSTANCE.post(new SnackbarRequest(new SnackbarBundle(getString(R.string.sign_in_failed))));
        }
    }
}
