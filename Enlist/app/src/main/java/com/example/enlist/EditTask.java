package com.example.enlist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;


public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    String currentDateString;
    MediaPlayer done_player,delete_player;

    EditText titlee, descriptionn;
    TextView deadlinee;
    com.google.android.material.floatingactionbutton.FloatingActionButton done_btn;
    ImageButton date_picker_btn;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        FloatingActionButton add_task_btn1 = findViewById(R.id.delete_btn);
        FloatingActionButton add_task_btn2 = findViewById(R.id.update_btn);

        titlee = findViewById(R.id.title_editText);
        descriptionn = findViewById(R.id.description_editText);
        deadlinee = findViewById(R.id.deadline_textView);

        date_picker_btn = findViewById(R.id.open_picker);
        done_btn = findViewById(R.id.done_btn);

        done_player = MediaPlayer.create(EditTask.this,R.raw.done_effect);
        delete_player = MediaPlayer.create(EditTask.this,R.raw.delete_effect);

        titlee.setText(getIntent().getStringExtra("title_extra"));
        descriptionn.setText(getIntent().getStringExtra("description_extra"));
        deadlinee.setText(getIntent().getStringExtra("deadline_extra"));

        final String keyy = getIntent().getStringExtra("key_extra");

        add_task_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Are you sure you want to delete the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("lol","delete success");
                                    Toast.makeText(EditTask.this, "Task deleted", Toast.LENGTH_SHORT).show();
                                    delete_player.start();
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                }
                                else{
                                    Log.d("lol","delete failed");
                                    Toast.makeText(EditTask.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditTask.this, "Task not deleted", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        add_task_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Are you sure you want to update the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                snapshot.getRef().child("title").setValue(titlee.getText().toString());
                                snapshot.getRef().child("description").setValue(descriptionn.getText().toString());
                                snapshot.getRef().child("deadline").setValue(deadlinee.getText().toString());
                                snapshot.getRef().child("key").setValue(keyy);

                                Toast.makeText(EditTask.this, "Task Updated", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(EditTask.this,MainActivity.class);
                                startActivity(intent1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditTask.this, "Task not updated", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(Source.main_user_uid).child(Source.main_class_group).child("Task" + keyy);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Kudos to you!!").setCancelable(false).setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditTask.this, "Task completed", Toast.LENGTH_SHORT).show();
                                    done_player.start();
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                }
                                else{
                                    Toast.makeText(EditTask.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("No, wait", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditTask.this, "Task not completed", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    private void closeKeyboard() {
        View view = EditTask.this.getCurrentFocus();
        if( view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
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