package com.example.enlist_todolistenlist;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "EditTask";
    String currentDateString,keyy,temp_month;
    MediaPlayer done_player,delete_player;
    Vibrator vibrator;
    int flag=0;
    
    EditText titlee, descriptionn;
    TextView deadlinee;
    com.google.android.material.floatingactionbutton.FloatingActionButton done_btn,add_to_calendar_btn;
    ImageButton date_picker_btn,back_arrow_btn,mic1,mic2;

    DatabaseReference reference;

    @Override
    public void onBackPressed() {
        if(flag == 0){
            startActivity(new Intent(EditTask.this,MainActivity.class));
            finish();
        }
        else if(flag == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
            builder.setMessage("Update the current task?").setCancelable(false).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            snapshot.getRef().child("title").setValue(titlee.getText().toString());
                            if (TextUtils.isEmpty(descriptionn.getText().toString())) {
                                descriptionn.setText(" ");
                            }else{
                                snapshot.getRef().child("description").setValue(descriptionn.getText().toString());
                            }
                            snapshot.getRef().child("deadline").setValue(deadlinee.getText().toString());
                            snapshot.getRef().child("key").setValue(keyy);

                            vibrator.vibrate(30);
                            startActivity(new Intent(EditTask.this,MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        FloatingActionButton delete_btn = findViewById(R.id.delete_btn);
        FloatingActionButton add_to_calendar_btn = findViewById(R.id.add_to_calendar);

        titlee = findViewById(R.id.title_editText);
        descriptionn = findViewById(R.id.description_editText);
        deadlinee = findViewById(R.id.deadline_textView);

        mic1 = findViewById(R.id.mic_title);
        mic2 = findViewById(R.id.mic_description);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        date_picker_btn = findViewById(R.id.open_picker);
        done_btn = findViewById(R.id.done_btn);
        back_arrow_btn = findViewById(R.id.back_arrow_btn);

        done_player = MediaPlayer.create(EditTask.this,R.raw.done_effect);
        delete_player = MediaPlayer.create(EditTask.this,R.raw.delete_effect);

        titlee.setText(getIntent().getStringExtra("title_extra"));
        descriptionn.setText(getIntent().getStringExtra("description_extra"));
        deadlinee.setText(getIntent().getStringExtra("deadline_extra"));

        keyy = getIntent().getStringExtra("key_extra");

        titlee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flag=1;
                if(titlee.getText().toString().length() == 21){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(titlee.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        descriptionn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flag=1;
                if(descriptionn.getText().toString().length() == 35){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(descriptionn.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        add_to_calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(titlee.getText().toString())) {
                    Toast.makeText(EditTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(descriptionn.getText().toString())) {
                    descriptionn = null;
                }else{
                    Intent intent_task = new Intent(Intent.ACTION_INSERT);
                    intent_task.setData(CalendarContract.Events.CONTENT_URI);
                    intent_task.putExtra(CalendarContract.Events.TITLE, titlee.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.DESCRIPTION, descriptionn.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.ALL_DAY, true);

                    if(intent_task.resolveActivity(getPackageManager()) != null){
                        startActivity(intent_task);
                    }
                    else{
                        Toast.makeText(EditTask.this, "No app found to support this action", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0){
                    startActivity(new Intent(EditTask.this,MainActivity.class));
                    finish();
                }
                else if(flag == 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                    builder.setMessage("Update the current task?").setCancelable(false).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    snapshot.getRef().child("title").setValue(titlee.getText().toString());
                                    if (TextUtils.isEmpty(descriptionn.getText().toString())) {
                                        descriptionn.setText(" ");
                                    }else{
                                        snapshot.getRef().child("description").setValue(descriptionn.getText().toString());
                                    }
                                    snapshot.getRef().child("deadline").setValue(deadlinee.getText().toString());
                                    snapshot.getRef().child("key").setValue(keyy);

                                    vibrator.vibrate(30);
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
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
            }
        });

        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter title");
                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e){
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter description");
                try {
                    startActivityForResult(intent,2);
                }catch (ActivityNotFoundException e){
                    Log.d(TAG,e.toString());
                }
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Delete the current task?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    delete_player.start();
                                    vibrator.vibrate(30);
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                    finish();
                                }
                                else{
                                    Log.d(TAG, "Failed");
                                }
                            }
                        });

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
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
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
                builder.setMessage("Kudos!!").setCancelable(false).setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    done_player.start();
                                    vibrator.vibrate(30);
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                    finish();
                                }
                                else{
                                    Log.d(TAG, "Failed");
                                }
                            }
                        });

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

        month+=1;
        switch (month){
            case 1:{
                temp_month="Jan";
                break;
            }
            case 2:{
                temp_month="Feb";
                break;
            }
            case 3:{
                temp_month="Mar";
                break;
            }
            case 4:{
                temp_month="Apr";
                break;
            }
            case 5:{
                temp_month="May";
                break;
            }
            case 6:{
                temp_month="Jun";
                break;
            }
            case 7:{
                temp_month="Jul";
                break;
            }
            case 8:{
                temp_month="Aug";
                break;
            }
            case 9:{
                temp_month="Sep";
                break;
            }
            case 10:{
                temp_month="Oct";
                break;
            }
            case 11:{
                temp_month="Nov";
                break;
            }
            case 12:{
                temp_month="Dec";
                break;
            }
        }
        if(day == 1 || day == 2 || day == 3 || day == 4 || day == 5 || day == 6 || day == 7 || day == 8 || day == 9){
            currentDateString = "0" + day + " " + temp_month;
        }
        else{
            currentDateString = day + " " + temp_month;
        }
        deadlinee.setText(currentDateString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        titlee.setText(result.get(0));
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        descriptionn.setText(result.get(0));
                    }
                }
                break;
        }
    }
}