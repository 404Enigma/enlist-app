package com.example.enlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Student_group extends AppCompatActivity {

    Button b_btn,b1_btn,b2_btn,b3_btn;
    DatabaseReference source_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_group);

        b_btn = findViewById(R.id.b_btn);
        b1_btn = findViewById(R.id.b1_btn);
        b2_btn = findViewById(R.id.b2_btn);
        b3_btn = findViewById(R.id.b3_btn);

        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B";
                startActivity(new Intent(Student_group.this,MainActivity.class));
            }
        });

        b1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B1";
                startActivity(new Intent(Student_group.this,MainActivity.class));
            }
        });

        b2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B2";
                startActivity(new Intent(Student_group.this,MainActivity.class));
            }
        });

        b3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B3";
                startActivity(new Intent(Student_group.this,MainActivity.class));
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