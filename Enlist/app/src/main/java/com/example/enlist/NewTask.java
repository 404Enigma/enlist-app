package com.example.enlist;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public class NewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String currentDateString,kkk,splited[];

    EditText title_editText, description_editText;
    TextView  deadline_textView;
    ImageButton date_picker_btn,back_arrow_btn;
    FloatingActionButton add_task_btn;

    DatabaseReference reference,users_reference;
    Integer taskNumber = new Random().nextInt();
    String key_layout = Integer.toString(taskNumber);

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
        builder.setMessage("Are you sure you want to discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(NewTask.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NewTask.this,MainActivity.class));
            }
        }).setNegativeButton("No, wait", new DialogInterface.OnClickListener() {
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
        setContentView(R.layout.activity_new_task);

        title_editText = findViewById(R.id.title_editText);
        description_editText = findViewById(R.id.description_editText);
        deadline_textView = findViewById(R.id.deadline_textView);
        back_arrow_btn = findViewById(R.id.back_arrow_btn);

        date_picker_btn = findViewById(R.id.open_picker);
        add_task_btn = findViewById(R.id.add_task_btn);

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewTask.this,MainActivity.class));
                finish();
            }
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

             add_task_btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     if(TextUtils.isEmpty(title_editText.getText().toString())){
                         Toast.makeText(NewTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                     }
                     else if(TextUtils.isEmpty(currentDateString)){
                         Toast.makeText(NewTask.this, "Enter deadline", Toast.LENGTH_SHORT).show();
                     }
                     else {

                         users_reference = FirebaseDatabase.getInstance().getReference().child("Source").child(Source.main_class_group);

                         users_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {

                                 kkk = "";
                                 for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                     kkk += snapshot1.getKey() + " ";
                                     String splited[] = kkk.split(" ");

                                     for (int i = 0; i < splited.length; i++) {

                                         reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(splited[i]).child(Source.main_class_group).child("Task" + taskNumber);

                                         reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                 snapshot.getRef().child("title").setValue(title_editText.getText().toString());
                                                 snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                                 snapshot.getRef().child("deadline").setValue(currentDateString);
                                                 snapshot.getRef().child("key").setValue(key_layout);

                                                 Toast.makeText(NewTask.this, "Task added", Toast.LENGTH_SHORT).show();
                                                 startActivity(new Intent(NewTask.this, MainActivity.class));

                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                                 Toast.makeText(NewTask.this, "Error adding", Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                     }
                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {
                             }
                         });
                     }
                 }
             });
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        deadline_textView.setText(currentDateString);
    }
}