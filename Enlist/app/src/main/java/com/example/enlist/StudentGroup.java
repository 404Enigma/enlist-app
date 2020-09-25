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

    Button b_btn,b1_btn,b2_btn,b3_btn;

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

        b_btn = findViewById(R.id.b_btn);
        b1_btn = findViewById(R.id.b1_btn);
        b2_btn = findViewById(R.id.b2_btn);
        b3_btn = findViewById(R.id.b3_btn);

        if(Source.main_PRN >= 73 && Source.main_PRN <= 95){
            Source.main_user_duo_class = "b1";
            b2_btn.setEnabled(false);
            b3_btn.setEnabled(false);
        }
        else if(Source.main_PRN >= 96 && Source.main_PRN <= 119){
            Source.main_user_duo_class = "b2";
            b1_btn.setEnabled(false);
            b3_btn.setEnabled(false);
        }
        else if(Source.main_PRN >= 120 && Source.main_PRN <= 145){
            Source.main_user_duo_class = "b3";
            b1_btn.setEnabled(false);
            b2_btn.setEnabled(false);
        }
        else{
            b_btn.setEnabled(false);
            b1_btn.setEnabled(false);
            b2_btn.setEnabled(false);
            b3_btn.setEnabled(false);
        }

        b_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B";
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
                finish();
            }
        });

        b1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B1";
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
                finish();
            }
        });

        b2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B2";
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
                finish();
            }
        });

        b3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Source.main_class_group="B3";
                startActivity(new Intent(StudentGroup.this,MainActivity.class));
                finish();
            }
        });
    }
}