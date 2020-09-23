package com.example.enlist;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class NewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RadioGroup radioGroup_privacy;
    int type_privacy=0;

    String currentDateString;
    String kkk;
    String[] splited;

    EditText title_editText, description_editText;
    TextView deadline_textView;
    ImageButton date_picker_btn, back_arrow_btn, mic1, mic2;
    FloatingActionButton add_task_btn,add_to_calendar_btn;
    Integer qqq, i;

    DatabaseReference reference, users_reference;
    Integer taskNumber = new Random().nextInt();
    String key_layout = Integer.toString(taskNumber);

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
        builder.setMessage("Are you sure you want to discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(NewTask.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                //  startActivity(new Intent(NewTask.this,MainActivity.class));
                finish();
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
        mic1 = findViewById(R.id.mic_title);
        mic2 = findViewById(R.id.mic_description);
        radioGroup_privacy = findViewById(R.id.radioGroup_privacy);
        add_to_calendar_btn = findViewById(R.id.add_to_calendar);

        date_picker_btn = findViewById(R.id.open_picker);
        add_task_btn = findViewById(R.id.add_task_btn);

        radioGroup_privacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_personal:
                        type_privacy=1;
                        break;
                    case R.id.radioButton_shared:
                        type_privacy=2;
                        break;
                }
            }
        });

        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak");
                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(NewTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak");
                try {
                    startActivityForResult(intent,2);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(NewTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
                builder.setMessage("Are you sure you want to discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(NewTask.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(NewTask.this,MainActivity.class));
                        finish();
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
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        add_to_calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title_editText.getText().toString())) {
                    Toast.makeText(NewTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(description_editText.getText().toString())) {
                    description_editText = null;
                }else{
                    Intent intent_task = new Intent(Intent.ACTION_INSERT);
                    intent_task.setData(CalendarContract.Events.CONTENT_URI);
                    intent_task.putExtra(CalendarContract.Events.TITLE, title_editText.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.DESCRIPTION, description_editText.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.ALL_DAY, true);

                    if(intent_task.resolveActivity(getPackageManager()) != null){
                        startActivity(intent_task);
                    }
                    else{
                        Toast.makeText(NewTask.this, "No app found to support this action", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(title_editText.getText().toString())) {
                    Toast.makeText(NewTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(currentDateString)) {
//                    Toast.makeText(NewTask.this, "Enter deadline", Toast.LENGTH_SHORT).show();
                    currentDateString = null;
                } else if (TextUtils.isEmpty(description_editText.getText().toString())) {
                    description_editText = null;
                } else if(type_privacy == 0){
                    Toast.makeText(NewTask.this, "Choose privacy type", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(type_privacy == 1){
                        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(Source.main_user_uid).child(Source.main_class_group).child("Task" + taskNumber);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                snapshot.getRef().child("title").setValue(title_editText.getText().toString());
                                snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                snapshot.getRef().child("deadline").setValue(currentDateString);
                                snapshot.getRef().child("key").setValue(key_layout);
                                Log.d("lol","task added");
                                Toast.makeText(NewTask.this, "Task added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NewTask.this,MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("lol","task added failed");
                                Toast.makeText(NewTask.this, "Error adding", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(type_privacy == 2){
                        Log.d("zzz", "lol1");
                        users_reference = FirebaseDatabase.getInstance().getReference().child("Source").child(Source.main_class_group);

                        users_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("zzz", "lol2");
                                kkk = "";
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                    kkk += snapshot1.getKey() + " ";
                                    Log.d("zzz", "lol3");
                                    splited = kkk.split(" ");
                                    qqq = splited.length;
                                    Log.d("zzz", "lol4");

                                    for (i = 0; i < splited.length; i++) {

                                        Log.d("zzz", "lol5");
                                        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(splited[i]).child(Source.main_class_group).child("Task" + taskNumber);

                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Log.d("zzz", "lol6");
                                                snapshot.getRef().child("title").setValue(title_editText.getText().toString());
                                                snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                                snapshot.getRef().child("deadline").setValue(currentDateString);
                                                snapshot.getRef().child("key").setValue(key_layout);
                                                Log.d("zzz", "lol7");

                                                if (i.equals(qqq)) {
                                                    Log.d("zzz", "lol8");
                                                    Toast.makeText(NewTask.this, "Task added", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(NewTask.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    Log.d("zzz", String.valueOf(i));
                                                    Log.d("zzz", String.valueOf(qqq));
                                                    Log.d("zzz", "lol9");
                                                }
                                                Log.d("zzz", "lol10");
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                Log.d("zzz", "lol11");

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
            }
        });
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        month+=1;
        if(month >= 10 && month <=12){
            currentDateString = day + "-" + month + "-" + year;
        }
        else{
            currentDateString = day + "-" + "0" + month + "-" + year;
        }
        deadline_textView.setText(currentDateString);
    }

    private void closeKeyboard() {
        View view = NewTask.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        title_editText.setText(result.get(0));
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        description_editText.setText(result.get(0));
                    }
                }
                break;
        }
    }
}