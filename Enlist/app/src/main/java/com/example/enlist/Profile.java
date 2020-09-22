package com.example.enlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    TextView user_email,user_prn,user_class_group,user_name;
    String email,name;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_class_group = findViewById(R.id.user_class_group);
        user_email = findViewById(R.id.user_email_id);
        user_name = findViewById(R.id.user_name);
        user_prn = findViewById(R.id.user_prn);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);

        user_class_group.setText(String.format("Class %s", Source.main_class_group));
        user_prn.setText(String.valueOf(Source.main_PRN));
        user_name.setText(name);
        user_email.setText(email);
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            name = account.getDisplayName();
            email = account.getEmail();
        }
    }
}