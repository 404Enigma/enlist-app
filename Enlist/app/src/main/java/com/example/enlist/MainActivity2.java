package com.example.enlist;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/*import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;*/
//import com.rilixtech.widget.countrycodepicker.CountryCodePicker;


public class MainActivity2 extends AppCompatActivity {

    String number;
    Button send_otp,facebook_login_btn;
    EditText phone_number;

   // private CallbackManager mCallbackManager;

    SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "lol";
    private FirebaseAuth mAuth;
    //CountryCodePicker codePicker;
    // private Button btnsignout;
    private int RC_SIGN_IN = 1;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*facebook_login_btn = findViewById(R.id.facebook_login_btn);
        send_otp = findViewById(R.id.send_otp);
        phone_number = findViewById(R.id.edit_text_phone_number);
        codePicker = findViewById(R.id.country_code);

        mCallbackManager = CallbackManager.Factory.create();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!phone_number.getText().toString().isEmpty() && phone_number.getText().toString().length() == 10){

                    source.main_number=phone_number.getEditableText().toString();
                    number = "+" + codePicker.getSelectedCountryCode() + phone_number.getEditableText().toString();
                    Intent intent = new Intent(getApplicationContext(),otp.class);
                    intent.putExtra("phone__number",number);
                    startActivity(intent);
                }
                else{
                    phone_number.setError("Phone number is not valid");
                }
            }
        });*/

    /*    facebook_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* LoginManager.getInstance().logInWithReadPermissions(register.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);

                    }
                });

                return;

            }
        });*/


        signInButton = findViewById(R.id.google_login_btn);
        mAuth = FirebaseAuth.getInstance();

        //btnsignout = findViewById(R.id.signout_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity2.this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        /*btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(MainActivity2.this, "You are logged out", Toast.LENGTH_SHORT).show();
                btnsignout.setVisibility(View.INVISIBLE);
            }
        }); */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }


  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI_facebook(currentUser);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

   /* private void updateUI_facebook(FirebaseUser currentUser) {
        Toast.makeText(register.this, "Logged In", Toast.LENGTH_LONG).show();

        Intent facebook_intent = new Intent(register.this,log_out.class);
        startActivity(facebook_intent);
        finish();
    }*/

   /* private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken: FACEBOOK" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success FACEBOOK" );
                            Intent facebook_intent = new Intent(register.this,personal_details.class);
                            startActivity(facebook_intent);
                            finish();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI_facebook(user);
                        } else {

                            Log.w(TAG, "signInWithCredential:failure FACEBOOK", task.getException());
                            Toast.makeText(register.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI_facebook(null);
                        }
                    }
                });
    }*/

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }







    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            Log.d(TAG, "Google Sign In failed.");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acco = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acco);
            Toast.makeText(MainActivity2.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            FirebaseGoogleAuth(null);
            Toast.makeText(MainActivity2.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity2.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent google_intent = new Intent(MainActivity2.this,MainActivity.class);
                            startActivity(google_intent);
                            finish();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI_google(user);
                        } else {
                            Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                            //updateUI_google(null);
                        }
                    }
                });
    }


    private void updateUI_google(FirebaseUser user) {
        //btnsignout.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(MainActivity2.this, personName + " + " + personEmail, Toast.LENGTH_SHORT).show();
        }
    }
}