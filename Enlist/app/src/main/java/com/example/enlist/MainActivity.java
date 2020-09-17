package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

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
   // private EditText editText_search;

    DatabaseReference reference,source_reference,source_count_users;
    RecyclerView recyclerView;
    ArrayList<DataItem> list;
    ItemAdapter itemAdapter;

    //SearchView searchView;
    FloatingActionButton new_task_btn;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to close the application").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finishAffinity();
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
        //editText_search = findViewById(R.id.editText_search);
        //searchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        list = new ArrayList<DataItem>();

       /* editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(MainActivity.this, gso);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Source.flag=0;
                Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, GoogleSignIn.class));
            }
        });

        choose_class_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudentGroup.class));
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        Source.main_user_uid = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(Source.main_user_uid).child(Source.main_class_group);

        source_reference = FirebaseDatabase.getInstance().getReference().child("Source");

        if (Source.main_class_group.equals("B")) {
            source_reference = source_reference.child("B");
            source_ref();
        }
        else if (Source.main_class_group.equals("B1")) {
            source_reference = source_reference.child("B1");
            source_ref();
        }
        else if (Source.main_class_group.equals("B2")) {
            source_reference = source_reference.child("B2");
            source_ref();
        }
        else if (Source.main_class_group.equals("B3")) {
            source_reference = source_reference.child("B3");
            source_ref();
        }

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
                startActivity(new Intent(MainActivity.this, NewTask.class));
            }
        });

    /*    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                itemAdapter.getFilter(ArrayList<DataItem> filteredList);
                return false;
            }
        });*/

    }

  /*  private void filter(String text) {

        ArrayList<DataItem> filteredList = new ArrayList<>();

        for (DataItem item : list){
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this, list);
        itemAdapter.filterList(filteredList);

    }*/

    private void source_ref() {

        source_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child(Source.main_user_uid).setValue(Source.main_user_uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("lol","task added failed");
            }
        });

    }

    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(itemAdapter);
    }
}