package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class NavBarSetting extends AppCompatActivity {

    ListView settings_listView;
    String[] items = {"Profile","Class Group","Log out"};
    MyAdapter adapter;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_setting);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(NavBarSetting.this, gso);

        settings_listView = findViewById(R.id.settings_listView);

        adapter = new MyAdapter(NavBarSetting.this, items);
        settings_listView.setAdapter(adapter);

        settings_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        Toast.makeText(NavBarSetting.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1:{
                        startActivity(new Intent(NavBarSetting.this, StudentGroup.class));
                        break;
                    }
                    case 2:{
                        mGoogleSignInClient.signOut();
                        Source.flag=0;
                        Toast.makeText(NavBarSetting.this, "You are logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NavBarSetting.this, GoogleSignIn.class));
                        break;
                    }
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String mHeading[];

        MyAdapter (Context c,String Heading[]){
            super(c,R.layout.custom_list_view_settings,items);
            this.context = c;
            this.mHeading = Heading;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.custom_list_view_settings,parent,false);
            TextView shop_name = row.findViewById(R.id.heading);

            shop_name.setText(mHeading[position]);

            return row;
        }
    }
}