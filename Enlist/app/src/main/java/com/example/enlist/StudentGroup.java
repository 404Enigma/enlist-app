package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentGroup extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Integer flag=0;

    Button b_btn,b1_btn,b2_btn,b3_btn;
    DatabaseReference source_reference;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentGroup.this);
        builder.setMessage("Are you sure you want to close the application?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StudentGroup.this.finishAffinity();
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
        setContentView(R.layout.activity_student_group);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        b_btn = findViewById(R.id.b_btn);
        b1_btn = findViewById(R.id.b1_btn);
        b2_btn = findViewById(R.id.b2_btn);
        b3_btn = findViewById(R.id.b3_btn);

        if(Source.main_PRN >= 19070122073L && Source.main_PRN <= 19070122095L){
            b2_btn.setEnabled(false);
            b3_btn.setEnabled(false);
        }
        else if(Source.main_PRN >= 19070122096L && Source.main_PRN <= 19070122119L){
            b1_btn.setEnabled(false);
            b3_btn.setEnabled(false);
        }
        else if(Source.main_PRN >= 19070122120L && Source.main_PRN <= 19070122145L){
            b1_btn.setEnabled(false);
            b2_btn.setEnabled(false);
        }

        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B";
                flag=1;
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
            }
        });

        b1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B1";
                flag=1;
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
            }
        });

        b2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B2";
                flag=1;
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
            }
        });

        b3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B3";
                flag=1;
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_class_group);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_setting:
                        if(flag==0){
                            Toast.makeText(StudentGroup.this, "Choose Class Group First", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        else if(flag==1){
                            startActivity(new Intent(getApplicationContext(),NavBarSetting.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                    case R.id.nav_class_group:
                        return true;
                    case R.id.nav_double_tick:
                        if(flag==0){
                            Toast.makeText(StudentGroup.this, "Choose Class Group First", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        else if(flag==1){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    private void source_insert_uid() {

        source_reference = FirebaseDatabase.getInstance().getReference().child("Source");

        source_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child(Source.main_user_uid).setValue(Source.main_user_uid);
                if(!snapshot.exists()){
                    Source.main_count_users+=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("lol","task added failed");
            }
        });
    }
}