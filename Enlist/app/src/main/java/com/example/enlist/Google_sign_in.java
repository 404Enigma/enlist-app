package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Google_sign_in extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "lol";
    private FirebaseAuth mAuth;

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        signInButton = findViewById(R.id.google_login_btn);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Google_sign_in.this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acco = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acco);
            Toast.makeText(Google_sign_in.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (ApiException e) {
            FirebaseGoogleAuth(null);
            Toast.makeText(Google_sign_in.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Google_sign_in.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Google_sign_in.this, "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Google_sign_in.this,Student_group.class));
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(Google_sign_in.this, "Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(Google_sign_in.this, personName + " + " + personEmail, Toast.LENGTH_SHORT).show();
        }
    }
}