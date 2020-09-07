package com.example.to_do_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class New_task extends AppCompatActivity {

    EditText title_editText, description_editText, deadline_editText;
    Button add_task_btn,cancel_btn;

    DatabaseReference reference;
    Integer taskNumber = new Random().nextInt();
    String key_layout = Integer.toString(taskNumber);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        title_editText = findViewById(R.id.title_editText);
        description_editText = findViewById(R.id.description_editText);
        deadline_editText = findViewById(R.id.deadline_editText);

        add_task_btn = findViewById(R.id.add_task_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Task" + taskNumber);

        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        snapshot.getRef().child("title_layout").setValue(title_editText.getText().toString());
                        snapshot.getRef().child("description_layout").setValue(description_editText.getText().toString());
                        snapshot.getRef().child("deadline_layout").setValue(deadline_editText.getText().toString());
                        snapshot.getRef().child("key_layout").setValue(key_layout);
                        Log.d("lol","task added");
                        Toast.makeText(New_task.this, "Task added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(New_task.this,MainActivity.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("lol","task added failed");
                        Toast.makeText(New_task.this, "Error adding", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}