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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoogleSignIn extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String personEmail;
    private Boolean temp;

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

        FirebaseApp.initializeApp(this);

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
                if (TextUtils.isEmpty(editText_PRN.getText().toString())) {
                    Toast.makeText(GoogleSignIn.this, "Enter PRN", Toast.LENGTH_SHORT).show();
                } else if (!(editText_PRN.getText().toString().length() == 11)) {
                    Toast.makeText(GoogleSignIn.this, "Incorrect PRN", Toast.LENGTH_SHORT).show();
                } else {
                    Source.main_PRN = Long.parseLong(String.valueOf(editText_PRN.getText()));
                    signIn();
                }
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
        } catch (ApiException e) {
            FirebaseGoogleAuth(null);
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
                            temp = check_prn_email();
                            if (Source.main_user_email.equals("sitpune.edu.in")) {
                                if(temp){
                                    Toast.makeText(GoogleSignIn.this, "Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GoogleSignIn.this, StudentGroup.class));
                                    finish();
                                }else{
                                    Toast.makeText(GoogleSignIn.this, "Please enter your PRN", Toast.LENGTH_SHORT).show();
                                    mGoogleSignInClient.signOut();
                                }
                            } else {
                                Toast.makeText(GoogleSignIn.this, "Please login through B.Tech ID", Toast.LENGTH_SHORT).show();
                                mGoogleSignInClient.signOut();
                            }
                        } else {
                            Toast.makeText(GoogleSignIn.this, "Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

                    private Boolean check_prn_email() {
                        switch (editText_PRN.getText().toString()){
                            case "19070122126":if(personEmail.equals("prakhar.agarwal.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122119":if(personEmail.equals("aditya.padir.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122105":if(personEmail.equals("akshay.mategaonkar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122108":if(personEmail.equals("amaan.mithani.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122102":if(personEmail.equals("ashwin.mandilk.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122124":if(personEmail.equals("deep.patoliya.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122073":if(personEmail.equals("jashn.anand.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122074":if(personEmail.equals("jatin.raghuvanshi.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122076":if(personEmail.equals("joseph.anjilimoottil.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122077":if(personEmail.equals("jugal.shroff.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122078":if(personEmail.equals("kanishka.kataria.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122079":if(personEmail.equals("kanishka.mishra.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122080":if(personEmail.equals("kapish.pashine.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122081":if(personEmail.equals("karan.samant.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122082":if(personEmail.equals("kartik.mudaliar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122087":if(personEmail.equals("kashif.ahmed.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122084":if(personEmail.equals("kashif.dar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122085":if(personEmail.equals("kavan.batavia.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122086":if(personEmail.equals("kenil.patwa.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122122":if(personEmail.equals("keny.patel.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122089":if(personEmail.equals("komal.gandhi.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122090":if(personEmail.equals("komil.singhal.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122133":if(personEmail.equals("rachita.sinha.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122134":if(personEmail.equals("rahul.mansharamani.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122083":if(personEmail.equals("raja.kartik.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122135":if(personEmail.equals("rajveer.singh.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122136":if(personEmail.equals("rashmi.meena.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122137":if(personEmail.equals("ratnesh.jain.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122138":if(personEmail.equals("reva.chinchalkar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122139":if(personEmail.equals("riya.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122141":if(personEmail.equals("rohit.raj.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122100":if(personEmail.equals("ronak.malkan.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122142":if(personEmail.equals("rubhav.mahendru.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122116":if(personEmail.equals("rushin.nemlawala.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122143":if(personEmail.equals("s.easwaran.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122145":if(personEmail.equals("sai.ventrapragada.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122094":if(personEmail.equals("sameer.kumar.btech2019@gmail.com")){
                                return true;
                            }
                            case "1907012221":if(personEmail.equals("saumya.paramar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122075":if(personEmail.equals("saurabh.jawanjal.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122112":if(personEmail.equals("shobhit.mudkhedkar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122120":if(personEmail.equals("sudhanshu.pandey.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122088":if(personEmail.equals("suprit.kolse.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122125":if(personEmail.equals("suyash.phatak.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122123":if(personEmail.equals("yash.patil.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122091":if(personEmail.equals("kothari.devendra.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122092":if(personEmail.equals("kshitij.tripathi.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122093":if(personEmail.equals("kumar.himanshu.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122095":if(personEmail.equals("kushagra.maheshwari.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122096":if(personEmail.equals("kushal.limdi.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122097":if(personEmail.equals("lisanne.dlima.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122098":if(personEmail.equals("maanav.bhavsar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122099":if(personEmail.equals("mahamat.chamchadine.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122101":if(personEmail.equals("manasi.seta.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122103":if(personEmail.equals("manish.kumar.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122104":if(personEmail.equals("mannya.sharma.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122106":if(personEmail.equals("mebanphira.cajee.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122107":if(personEmail.equals("midhushi.tiwari.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122109":if(personEmail.equals("mohammad.ahmadi.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122110":if(personEmail.equals("mouzou.essowazam.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122113":if(personEmail.equals("naman.pandya.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122114":if(personEmail.equals("nazia.malik.btech201@gmail.com")){
                                return true;
                            }
                            case "19070122115":if(personEmail.equals("neeraj.chouhan.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122117":if(personEmail.equals("nishita.agrawal.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122118":if(personEmail.equals("nitya.mehta.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122127":if(personEmail.equals("prakhar.patel.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122128":if(personEmail.equals("prasanna.jain.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122129":if(personEmail.equals("pratyush.jain.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122130":if(personEmail.equals("pratyush.sinha.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122131":if(personEmail.equals("pratyush.vats.btech2019@gmail.com")){
                                return true;
                            }
                            case "19070122132":if(personEmail.equals("preksha.asati.btech2019@gmail.com")){
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {

            personEmail = account.getEmail();

            if (personEmail != null) {
                String[] qq = personEmail.split("@");
                Source.main_user_email_name = qq[0];
                Source.main_user_email = qq[1];
            }
        }
    }
}