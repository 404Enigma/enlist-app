package com.example.todolistenlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignIn extends AppCompatActivity {

    private static final String TAG = "GoogleSignIn";
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String personEmail;
    private Boolean temp;
    private TextView btechid_textView;
    private Button verify_btn;
    private EditText editText_PRN;
    private int RC_SIGN_IN = 1,flag=0;

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

        btechid_textView = findViewById(R.id.btechid_textView);
        verify_btn = findViewById(R.id.verify_btn);
        signInButton = findViewById(R.id.google_login_btn);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            flag=1;
            btechid_textView.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
        }
        else{
            flag=2;
            verify_btn.setVisibility(View.INVISIBLE);
        }

        editText_PRN = findViewById(R.id.editText_PRN);

        editText_PRN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText_PRN.getText().toString().length() == 3){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText_PRN.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(GoogleSignIn.this, gso);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText_PRN.getText().toString())) {
                    Toast.makeText(GoogleSignIn.this, "Enter PRN", Toast.LENGTH_SHORT).show();
                } else if (!(editText_PRN.getText().toString().length() == 3)) {
                    Toast.makeText(GoogleSignIn.this, "Incorrect PRN", Toast.LENGTH_SHORT).show();
                } else {
                    Source.main_PRN = Integer.parseInt(String.valueOf(editText_PRN.getText()));
                    signIn();
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText_PRN.getText().toString())) {
                    Toast.makeText(GoogleSignIn.this, "Enter PRN", Toast.LENGTH_SHORT).show();
                } else if (!(editText_PRN.getText().toString().length() == 3)) {
                    Toast.makeText(GoogleSignIn.this, "Incorrect PRN", Toast.LENGTH_SHORT).show();
                } else {
                    Source.main_PRN = Integer.parseInt(String.valueOf(editText_PRN.getText()));
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
                                    startActivity(new Intent(GoogleSignIn.this, StudentGroup.class));
                                    finish();
                                }else{
                                    Toast.makeText(GoogleSignIn.this, "Please enter your PRN", Toast.LENGTH_SHORT).show();
                                    if (flag == 1){
                                        return;
                                    }else if(flag == 2){
                                        mGoogleSignInClient.signOut();
                                    }
                                }
                            } else {
                                Toast.makeText(GoogleSignIn.this, "Please login through B.Tech ID", Toast.LENGTH_SHORT).show();
                                mGoogleSignInClient.signOut();
                            }
                        } else {
                            Log.d(TAG, "Failed");
                            updateUI(null);
                        }
                    }

                    private Boolean check_prn_email() {
                        switch (editText_PRN.getText().toString()){
                            case "126":if(personEmail.equals("prakhar.agarwal.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "119":if(personEmail.equals("aditya.padir.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "105":if(personEmail.equals("akshay.mategaonkar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "108":if(personEmail.equals("amaan.mithani.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "102":if(personEmail.equals("ashwin.mandilk.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "124":if(personEmail.equals("deep.patoliya.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "073":if(personEmail.equals("jashn.anand.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "074":if(personEmail.equals("jatin.raghuvanshi.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "076":if(personEmail.equals("joseph.anjilimoottil.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "077":if(personEmail.equals("jugal.shroff.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "078":if(personEmail.equals("kanishka.kataria.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "079":if(personEmail.equals("kanishka.mishra.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "080":if(personEmail.equals("kapish.pashine.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "081":if(personEmail.equals("karan.samant.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "082":if(personEmail.equals("kartik.mudaliar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "087":if(personEmail.equals("kashif.ahmed.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "084":if(personEmail.equals("kashif.dar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "085":if(personEmail.equals("kavan.batavia.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "086":if(personEmail.equals("kenil.patwa.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "122":if(personEmail.equals("keny.patel.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "089":if(personEmail.equals("komal.gandhi.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "090":if(personEmail.equals("komil.singhal.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "133":if(personEmail.equals("rachita.sinha.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "134":if(personEmail.equals("rahul.mansharamani.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "083":if(personEmail.equals("raja.kartik.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "135":if(personEmail.equals("rajveer.singh.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "136":if(personEmail.equals("rashmi.meena.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "137":if(personEmail.equals("ratnesh.jain.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "138":if(personEmail.equals("reva.chinchalkar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "139":if(personEmail.equals("riya.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "141":if(personEmail.equals("rohit.raj.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "100":if(personEmail.equals("ronak.malkan.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "142":if(personEmail.equals("rubhav.mahendru.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "116":if(personEmail.equals("rushin.nemlawala.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "143":if(personEmail.equals("s.easwaran.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "145":if(personEmail.equals("sai.ventrapragada.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "094":if(personEmail.equals("sameer.kumar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "121":if(personEmail.equals("saumya.paramar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "075":if(personEmail.equals("saurabh.jawanjal.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "112":if(personEmail.equals("shobhit.mudkhedkar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "120":if(personEmail.equals("sudhanshu.pandey.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "088":if(personEmail.equals("suprit.kolse.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "125":if(personEmail.equals("suyash.phatak.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "123":if(personEmail.equals("yash.patil.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "091":if(personEmail.equals("kothari.devendra.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "092":if(personEmail.equals("kshitij.tripathi.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "093":if(personEmail.equals("kumar.himanshu.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "095":if(personEmail.equals("kushagra.maheshwari.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "096":if(personEmail.equals("kushal.limdi.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "097":if(personEmail.equals("lisanne.dlima.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "098":if(personEmail.equals("maanav.bhavsar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "099":if(personEmail.equals("mahamat.chamchadine.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "101":if(personEmail.equals("manasi.seta.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "103":if(personEmail.equals("manish.kumar.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "104":if(personEmail.equals("mannya.sharma.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "106":if(personEmail.equals("mebanphira.cajee.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "107":if(personEmail.equals("midhushi.tiwari.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "109":if(personEmail.equals("mohammad.ahmadi.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "110":if(personEmail.equals("mouzou.essowazam.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "113":if(personEmail.equals("naman.pandya.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "114":if(personEmail.equals("nazia.malik.btech201@sitpune.edu.in")){
                                return true;
                            }
                            case "115":if(personEmail.equals("neeraj.chouhan.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "117":if(personEmail.equals("nishita.agrawal.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "118":if(personEmail.equals("nitya.mehta.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "127":if(personEmail.equals("prakhar.patel.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "128":if(personEmail.equals("prasanna.jain.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "129":if(personEmail.equals("pratyush.jain.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "130":if(personEmail.equals("pratyush.sinha.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "131":if(personEmail.equals("pratyush.vats.btech2019@sitpune.edu.in")){
                                return true;
                            }
                            case "132":if(personEmail.equals("preksha.asati.btech2019@sitpune.edu.in")){
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