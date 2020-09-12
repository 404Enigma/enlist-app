package com.example.enlist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public class New_task extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String currentDateString;

    EditText title_editText, description_editText;
    TextView  deadline_textView;
    Button add_task_btn,cancel_btn,date_picker_btn;

    DatabaseReference reference;
    Integer taskNumber = new Random().nextInt();
    String key_layout = Integer.toString(taskNumber);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        title_editText = findViewById(R.id.title_editText);
        description_editText = findViewById(R.id.description_editText);
        deadline_textView = findViewById(R.id.deadline_textView);

        date_picker_btn = findViewById(R.id.open_picker);
        add_task_btn = findViewById(R.id.add_task_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Task" + taskNumber);

        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(title_editText.getText().toString())){
                    Toast.makeText(New_task.this, "Enter title", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(currentDateString)){
                    Toast.makeText(New_task.this, "Enter deadline", Toast.LENGTH_SHORT).show();
                }
                else{
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            snapshot.getRef().child("title_layout").setValue(title_editText.getText().toString());
                            snapshot.getRef().child("description_layout").setValue(description_editText.getText().toString());
                            snapshot.getRef().child("deadline_layout").setValue(currentDateString);
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