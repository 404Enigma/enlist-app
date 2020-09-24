package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavBarSetting extends AppCompatActivity {

    private static final String TAG = "NavBarSetting";
    ImageButton back_arrow_btn;

    private GoogleSignInClient mGoogleSignInClient;

    BottomNavigationView bottomNavigationView;
    TextView profile_textView,privacy_policy_textView,github_textView,log_out_textView,support_textView,about_textView;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NavBarSetting.this, StudentGroup.class));
        finish();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(NavBarSetting.this);
        builder.setMessage("Are you sure you want to close the application?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_setting);

            Log.d(TAG, "onCreate: ");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(NavBarSetting.this, gso);

        privacy_policy_textView = findViewById(R.id.privacy_policy_textView);
        about_textView = findViewById(R.id.about_textView);
        github_textView = findViewById(R.id.github_textView);
        log_out_textView = findViewById(R.id.logout_textView);
        support_textView = findViewById(R.id.support_textView);
        profile_textView = findViewById(R.id.profile_textView);

        back_arrow_btn = findViewById(R.id.back_arrow_btn);

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavBarSetting.this, StudentGroup.class));
                finish();
            }
        });

        privacy_policy_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavBarSetting.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
            }
        });

        about_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavBarSetting.this, "About Us", Toast.LENGTH_SHORT).show();
            }
        });

        github_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/prakhar-agarwall/enlist";
                Intent url_intent = new Intent(Intent.ACTION_VIEW);
                url_intent.setData(Uri.parse(url));
                startActivity(url_intent);
            }
        });

        log_out_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Source.flag=0;
                Toast.makeText(NavBarSetting.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NavBarSetting.this, GoogleSignIn.class));
                finish();
            }
        });

        support_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavBarSetting.this, "Support", Toast.LENGTH_SHORT).show();
            }
        });

        profile_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavBarSetting.this,Profile.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_setting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_double_tick:
                        startActivity(new Intent(NavBarSetting.this,MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_setting:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
}