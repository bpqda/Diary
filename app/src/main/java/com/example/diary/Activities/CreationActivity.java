package com.example.diary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diary.Entitys.Event;
import com.example.diary.R;
import com.example.diary.RealmUtility;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

public class CreationActivity extends AppCompatActivity {
    TimePicker time;
    EditText name, description;
    Button create, since, to, dateBtn;
    long date_start, date_finish;
    long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        time = findViewById(R.id.time);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        create = findViewById(R.id.create);
        to = findViewById(R.id.to);
        since = findViewById(R.id.since);
        dateBtn = findViewById(R.id.date);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        date = c.getTimeInMillis();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        since.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_start =  date + time.getCurrentHour() * 60 * 60 * 1000 + time.getCurrentMinute() * 60 * 1000;
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_finish =  date + time.getCurrentHour() * 60 * 60 * 1000 + time.getCurrentMinute() * 60 * 1000;
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Event e = new Event(0,
                 //        date_start,
                 //        date_finish,
                 //        name.getText().toString(),
                 //        description.getText().toString());
                 //System.out.println(e.toString());

                Realm realm = Realm.getInstance(RealmUtility.getDefaultConfig());
                realm.beginTransaction();
                Event e = realm.createObject(Event.class);
                e.setDate_start(date_start);
                e.setDate_finish(date_finish);
                e.setName(name.getText().toString());
                e.setDescription(description.getText().toString());
                System.out.println(e.toString());
                realm.commitTransaction();
                Intent i = new Intent(CreationActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
