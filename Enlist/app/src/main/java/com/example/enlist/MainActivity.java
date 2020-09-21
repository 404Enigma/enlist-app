package com.example.enlist;

import androidx.annotation.LongDef;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    BottomNavigationView bottomNavigationView;

    TextView class_group_textView;

    // private EditText editText_search;
     int flag1=0,flag2=0;

    DatabaseReference reference, source_reference;
    RecyclerView recyclerView;
    ArrayList<DataItem> list;
    ItemAdapter itemAdapter;

    //SearchView searchView;
    FloatingActionButton new_task_btn;
    ImageButton back_arrow_btn;

    @Override
    public void onBackPressed() {
      //  startActivity(new Intent(MainActivity.this, StudentGroup.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("lol", "lol4");

        Log.d("qwe", "Create");

        class_group_textView = findViewById(R.id.class_group_textView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        back_arrow_btn = findViewById(R.id.back_arrow_btn);
        new_task_btn = findViewById(R.id.new_task_btn);
        //editText_search = findViewById(R.id.editText_search);
        //searchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(null);
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

        class_group_textView.setText(String.format("Class %s", Source.main_class_group));

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // startActivity(new Intent(MainActivity.this, StudentGroup.class));
                finish();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Source.main_user_uid = user.getUid();

        source_reference = FirebaseDatabase.getInstance().getReference().child("Source");

        if (Source.main_class_group.equals("B")) {
            source_reference = source_reference.child("B");
            source_ref();
        } else if (Source.main_class_group.equals("B1")) {
            source_reference = source_reference.child("B1");
            source_ref();
        } else if (Source.main_class_group.equals("B2")) {
            source_reference = source_reference.child("B2");
            source_ref();
        } else if (Source.main_class_group.equals("B3")) {
            source_reference = source_reference.child("B3");
            source_ref();
        }

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(Source.main_user_uid).child(Source.main_class_group);

        Log.d("qwe","call karo value listener");
        reference.addValueEventListener(new ValueEventListener() {                  //Adding data in recycler view
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(flag2 == 1 || flag1 == 0){
                    flag2=0;
                    Log.d("qwe","call karo1");

                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        Log.d("qwe","call karo2");
                        DataItem p = dataSnapshot.getValue(DataItem.class);
                        Log.d("qwe","call karo2.1");
                        list.add(p);
                        Log.d("qwe","call karo2.2");
                    }

                    Log.d("qwe","call karo3");
                    itemAdapter = new ItemAdapter(MainActivity.this, list);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                /*LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);*/
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                    flag1=1;
                    Log.d("qwe","call karo4");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("qwe","call karo5");
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }
        });

        new_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewTask.class));
                finish();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_double_tick);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(), NavBarSetting.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_double_tick:
                        return true;
                }
                return false;
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

    }           //////////////////////////////////////////END onCreate

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
        Log.d("qwe","SF1");
        source_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("qwe","SF2");

                snapshot.getRef().child(Source.main_user_uid).setValue(Source.main_user_uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("qwe","SF3");
                Log.d("lol", "task added failed");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("qwe", "Destroy");
    }

    public void onPause() {
        super.onPause();
        flag1=1;
        flag2=1;
        Log.d("qwe", "Pause");
    }

    public void onStop() {
        super.onStop();
        Log.d("qwe", "Stop");
    }

    public void onStart() {
        super.onStart();
        Log.d("qwe", "Start");




         //recyclerView.setAdapter(itemAdapter);
    }

    public void onRestart() {
        super.onRestart();
        Log.d("qwe", "Restart");
        // recyclerView.setAdapter(itemAdapter);
    }

    public void onResume() {
        super.onResume();
        Log.d("qwe", "Resume");



        //recyclerView.setAdapter(itemAdapter);
    }
}