package com.example.enlist_todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    TextView class_group_textView,current_date_textView;

    int flag1=0,flag2=0,flag3=0,flag4=0;

    DatabaseReference reference, source_reference;
    RecyclerView recyclerView;
    ArrayList<DataItem> list;
    ItemAdapter itemAdapter;

    FloatingActionButton new_task_btn;
    ImageButton back_arrow_btn;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat format_date = new SimpleDateFormat("dd MMM YYYY");
    String date = format_date.format(calendar.getTime());

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, StudentGroup.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        class_group_textView = findViewById(R.id.class_group_textView);
        current_date_textView = findViewById(R.id.current_date);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        back_arrow_btn = findViewById(R.id.back_arrow_btn);
        new_task_btn = findViewById(R.id.new_task_btn);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        list = new ArrayList<DataItem>();

        class_group_textView.setText(String.format("Class %s", Source.main_class_group));
        current_date_textView.setText(date);

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudentGroup.class));
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

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(flag2 == 1 || flag1 == 0 || flag3 == 0 || flag4 == 1){
                    flag2=0;
                    flag4=0;
                    flag1=1;
                    flag3=1;

                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        DataItem p = dataSnapshot.getValue(DataItem.class);
                        list.add(p);
                    }

                    itemAdapter = new ItemAdapter(MainActivity.this, list);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                    flag1=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "No data");
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

    }

    private void source_ref() {
        source_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child(Source.main_user_uid).setValue(Source.main_user_uid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onPause() {
        super.onPause();
        flag2=1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag4=1;
    }
}