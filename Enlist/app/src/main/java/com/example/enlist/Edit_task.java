package com.example.enlist;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;


public class Edit_task extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String currentDateString;

    EditText titlee, descriptionn;
    TextView deadlinee;
    Button update_btn,delete_btn,date_picker_btn,done_btn;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        titlee = findViewById(R.id.title_editText);
        descriptionn = findViewById(R.id.description_editText);
        deadlinee = findViewById(R.id.deadline_textView);

        date_picker_btn = findViewById(R.id.open_picker);
        update_btn = findViewById(R.id.update_btn);
        delete_btn = findViewById(R.id.delete_btn);
        done_btn = findViewById(R.id.done_btn);

        titlee.setText(getIntent().getStringExtra("title_extra"));
        descriptionn.setText(getIntent().getStringExtra("description_extra"));
        deadlinee.setText(getIntent().getStringExtra("deadline_extra"));

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String useruid=user.getUid();

        final String keyy = getIntent().getStringExtra("key_extra");

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(useruid).child("Task" + keyy);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_task.this);
                builder.setMessage("Are you sure you want to delete the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("lol","delete success");
                                    Toast.makeText(Edit_task.this, "Task deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Edit_task.this,MainActivity.class));
                                }
                                else{
                                    Log.d("lol","delete failed");
                                    Toast.makeText(Edit_task.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //Toast.makeText(Edit_task.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Edit_task.this, "Task not deleted", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_task.this);
                builder.setMessage("Kudos to you!!").setCancelable(false).setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Edit_task.this, "Task completed", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Edit_task.this,MainActivity.class));
                                }
                                else{
                                    Toast.makeText(Edit_task.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //Toast.makeText(Edit_task.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                }).setNegativeButton("No, wait", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Edit_task.this, "Task not completed", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_task.this);
                builder.setMessage("Are you sure you want to update the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

                        //Toast.makeText(Edit_task.this, "Text deleted", Toast.LENGTH_SHORT).show();
                        //finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Edit_task.this, "Task not updated", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        deadlinee.setText(currentDateString);
    }
}