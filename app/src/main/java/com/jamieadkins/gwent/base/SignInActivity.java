package com.jamieadkins.gwent.base;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.main.MainActivity;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 3294;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button signIn = (Button) findViewById(R.id.button_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayServicesAvailable()) {
                    startSignInProcess();
                }
            }
        });
    }

    private boolean isPlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        RC_SIGN_IN + 1).show();
            }

            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            onSignedIn();
        }
    }

    private void startSignInProcess() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            onSignedIn();
        } else {
            ArrayList<AuthUI.IdpConfig> providers = new ArrayList<>();
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
            providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successful sign in!
                onSignedIn();
                return;
            }

            // Sign in cancelled.
            if (resultCode == RESULT_CANCELED) {
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            // No network.
            if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            // User is not signed in. Show generic failed snackbar.
            showSnackbar(R.string.sign_in_failed);
        }
    }

    private void onSignedIn() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showSnackbar(int stringId) {
        Snackbar.make(
                findViewById(R.id.coordinator_layout),
                getString(stringId),
                Snackbar.LENGTH_LONG)
                .show();
    }
}
