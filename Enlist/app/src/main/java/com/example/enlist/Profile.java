package com.example.enlist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    TextView user_email,user_prn,user_class_group,user_name;
    String email,name;
    ImageButton back_arrow_btn;
    private static final String TAG = "Profile";
    String rrr;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_prn = findViewById(R.id.user_prn);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email_id);
        user_class_group = findViewById(R.id.user_class_group);

        back_arrow_btn = findViewById(R.id.back_arrow_btn);

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, NavBarSetting.class));
                finish();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);

        if(Source.main_user_duo_class.equals("b1")){
            user_class_group.setText("Enrolled in class B & B1");
        }else if(Source.main_user_duo_class.equals("b2")){
            user_class_group.setText("Enrolled in class B & B2");
        }else if(Source.main_user_duo_class.equals("b3")){
            user_class_group.setText("Enrolled in class B & B3");
        }
        user_prn.setText(String.valueOf("19070122" + Source.main_PRN));
        user_name.setText(name);
        user_email.setText(email);
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            name = account.getDisplayName();
            email = account.getEmail();
            if(name != null){
             /*   String[] ww = name.split(".");
                Log.d(TAG, ww[0]);
//                Log.d(TAG, ww[1]);
//                Log.d(TAG, ww[2]);
//                Log.d(TAG, ww[3]);
                //String[] ee = ww[1].split("");
                name = ww[0] + " " + ww[1];
                Log.d(TAG, name);*/
       /*      String[] lll = name.split(" ");
                Log.d(TAG, lll[0]);
                rrr = lll[0];*/
//             String[] qqq = rrr.split(".");
//                Log.d(TAG, qqq[0]);
//                Log.d(TAG,qqq[1]);
            }
            if(email != null){
                String[] qq = email.split("@");
                email = qq[0];
            }
        }
    }
}