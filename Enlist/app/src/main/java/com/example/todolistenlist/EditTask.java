package com.example.todolistenlist;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
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


public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "EditTask";
    String currentDateString,keyy,temp_month;
    MediaPlayer done_player,delete_player;
    Vibrator vibrator;
    
    EditText titlee, descriptionn;
    TextView deadlinee;
    com.google.android.material.floatingactionbutton.FloatingActionButton done_btn;
    ImageButton date_picker_btn,back_arrow_btn,mic1,mic2;

    DatabaseReference reference;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
        builder.setMessage("Are you sure you want to discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(EditTask.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditTask.this,MainActivity.class));
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
        setContentView(R.layout.activity_edit_task);

        FloatingActionButton delete_btn = findViewById(R.id.delete_btn);
        FloatingActionButton update_btn = findViewById(R.id.update_btn);

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

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Are you sure you want to discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditTask.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditTask.this,MainActivity.class));
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

        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter title");
                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(EditTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                builder.setMessage("Are you sure you want to delete the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditTask.this, "Task deleted", Toast.LENGTH_SHORT).show();
                                    delete_player.start();
                                    vibrator.vibrate(30);
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                    finish();
                                }
                                else{
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

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setMessage("Are you sure you want to update the current task").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

                                Toast.makeText(EditTask.this, "Task Updated", Toast.LENGTH_SHORT).show();
                                vibrator.vibrate(30);
                                startActivity(new Intent(EditTask.this,MainActivity.class));
                                finish();
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
                builder.setMessage("Kudos!!").setCancelable(false).setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EditTask.this, "Well done", Toast.LENGTH_SHORT).show();
                                    done_player.start();
                                    vibrator.vibrate(30);
                                    startActivity(new Intent(EditTask.this,MainActivity.class));
                                    finish();
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
                        Toast.makeText(EditTask.this, "Task due", Toast.LENGTH_SHORT).show();
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
            case 1:temp_month="Jan";
            case 2:temp_month="Feb";
            case 3:temp_month="Mar";
            case 4:temp_month="Apr";
            case 5:temp_month="May";
            case 6:temp_month="Jun";
            case 7:temp_month="Jul";
            case 8:temp_month="Aug";
            case 9:temp_month="Sep";
            case 10:temp_month="Oct";
            case 11:temp_month="Nov";
            case 12:temp_month="Dec";
        }

        currentDateString = day + " " + temp_month;

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