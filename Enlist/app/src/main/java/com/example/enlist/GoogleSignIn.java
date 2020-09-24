package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class GoogleSignIn extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String personEmail;
    private Boolean temp_check;

    private EditText editText_PRN;

    private int RC_SIGN_IN = 1;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoogleSignIn.this);
        builder.setMessage("Are you sure you want to close the application?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GoogleSignIn.this.finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        signInButton = findViewById(R.id.google_login_btn);
        mAuth = FirebaseAuth.getInstance();

        editText_PRN = findViewById(R.id.editText_PRN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(GoogleSignIn.this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editText_PRN.getText().toString())){
                    Toast.makeText(GoogleSignIn.this, "Enter PRN", Toast.LENGTH_SHORT).show();
                }
                else if(!(editText_PRN.getText().toString().length() == 11)){
                    Toast.makeText(GoogleSignIn.this, "Incorrect PRN", Toast.LENGTH_SHORT).show();
                }
                else{
                    Source.main_PRN = Long.parseLong(String.valueOf(editText_PRN.getText()));
                    signIn();
                }
//                Source.main_PRN = Long.parseLong(String.valueOf(editText_PRN.getText()));
//                signIn();
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
            Task<GoogleSignInAccount> task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acco = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acco);
            //Toast.makeText(GoogleSignIn.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (ApiException e) {
            FirebaseGoogleAuth(null);
            Toast.makeText(GoogleSignIn.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(GoogleSignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            temp_check = check_email_prn(personEmail,editText_PRN.getText().toString());
                            if(Source.main_user_email.equals("sitpune.edu.in")){
                                if(temp_check){
                                    Toast.makeText(GoogleSignIn.this, "Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GoogleSignIn.this, StudentGroup.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(GoogleSignIn.this, "Please enter your PRN", Toast.LENGTH_SHORT).show();
                                    mGoogleSignInClient.signOut();
                                }
                            }
                            else{
                                Toast.makeText(GoogleSignIn.this, "PLease login through B.Tech ID", Toast.LENGTH_SHORT).show();
                                mGoogleSignInClient.signOut();
                                //updateUI(null);
                            }
                        }
                        else{
                            Toast.makeText(GoogleSignIn.this, "Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private boolean check_email_prn(String personEmail, String toString) {
        switch (toString){
            case "19070122126":if(personEmail.equals("prakhar.agarwal.btech2019@sitpune.edu.in")){
                return true;
            }
            case "19070122129":if(personEmail.equals("pratyush.jain.btech2019@sitpune.edu.in")){
                return true;
            }
            case "19070122120":if(personEmail.equals("sudhanshu.pandey.btech2019@sitpune.edu.in")){
                return true;
            }
            case "19070122134":if(personEmail.equals("rahul.mansharamani.btech2019@sitpune.edu.in")){
                return true;
            }
        }
        return false;
    }



    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            //String personName = account.getDisplayName();
//            String personGivenName = account.getGivenName();
//            String personFamilyName = account.getFamilyName();
                personEmail = account.getEmail();
//            String personId = account.getId();
//            Uri personPhoto = account.getPhotoUrl();

            if(personEmail != null){
                String[] qq = personEmail.split("@");
                Source.main_user_email_name = qq[0];
                Source.main_user_email = qq[1];
            }
            //Toast.makeText(GoogleSignIn.this, personName + " + " + personEmail, Toast.LENGTH_SHORT).show();
        }
    }
}