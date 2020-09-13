package com.example.enlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Student_group extends AppCompatActivity {

    Button b_btn,b1_btn,b2_btn,b3_btn;

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
}