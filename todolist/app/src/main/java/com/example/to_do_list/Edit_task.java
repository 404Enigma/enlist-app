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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Edit_task extends AppCompatActivity {

    EditText titlee, descriptionn, deadlinee;
    Button update_btn,delete_btn;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        titlee = findViewById(R.id.title_editText);
        descriptionn = findViewById(R.id.description_editText);
        deadlinee = findViewById(R.id.deadline_editText);

        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);

        titlee.setText(getIntent().getStringExtra("title_extra"));
        descriptionn.setText(getIntent().getStringExtra("description_extra"));
        deadlinee.setText(getIntent().getStringExtra("deadline_extra"));

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String useruid=user.getUid();

        final String keyy = getIntent().getStringExtra("key_extra");

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(useruid).child("Task" + keyy);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("lol","delete success");
                            Toast.makeText(Edit_task.this, "Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Edit_task.this,MainActivity.class));
                        }
                        else{
                            Log.d("lol","delete failed");
                            Toast.makeText(Edit_task.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        snapshot.getRef().child("title_layout").setValue(titlee.getText().toString());
                        snapshot.getRef().child("description_layout").setValue(descriptionn.getText().toString());
                        snapshot.getRef().child("deadline_layout").setValue(deadlinee.getText().toString());
                        snapshot.getRef().child("key_layout").setValue(keyy);

                        Log.d("lol","lol1");
                        Toast.makeText(Edit_task.this, "Task Updated", Toast.LENGTH_SHORT).show();
                        Log.d("lol","lol2");
                       // startActivity(new Intent(Edit_task.this,MainActivity.class));
                        Intent intent1 = new Intent(Edit_task.this,MainActivity.class);
                        startActivity(intent1);
                        Log.d("lol","lol3");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });
    }
}