package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private Button btnsignout,choose_class_group;

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<DataItem> list;
    ItemAdapter itemAdapter;

    FloatingActionButton new_task_btn;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to close the application").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finishAffinity();
                //finish();
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
        setContentView(R.layout.activity_main);

        Log.d("lol","lol4");

        btnsignout = findViewById(R.id.signout_btn);
        new_task_btn = findViewById(R.id.new_task_btn);
        choose_class_group = findViewById(R.id.choose_class_group);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        list = new ArrayList<DataItem>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Google_sign_in.class));
            }
        });

        choose_class_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Student_group.class));
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String useruid=user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(useruid).child(Source.main_class_group);

        reference.addValueEventListener(new ValueEventListener() {                  //Adding data in recycler view
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    DataItem p = dataSnapshot.getValue(DataItem.class);
                    list.add(p);
                }

                itemAdapter = new ItemAdapter(MainActivity.this, list);
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }
        });

        new_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,New_task.class));
            }
        });

    }
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(itemAdapter);
    }
}