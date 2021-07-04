package com.example.diary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

    long date_start, date_finish;
    long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TimePicker time = findViewById(R.id.time);
        EditText name = findViewById(R.id.name);
        EditText description = findViewById(R.id.description);

        Button dateBtn = findViewById(R.id.date);
        dateBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), (view, year, monthOfYear, dayOfMonth) -> {
                Calendar c1 = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                date = c1.getTimeInMillis();
                dateBtn.setText(dayOfMonth + "." + monthOfYear + "." + year);
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Button since = findViewById(R.id.since);
        since.setOnClickListener(v -> {
            date_start = date + time.getCurrentHour() * 60 * 60 * 1000 + time.getCurrentMinute() * 60 * 1000;

            since.setText(time.getCurrentHour() + ":" + time.getCurrentMinute());
        });

        Button to = findViewById(R.id.to);
        to.setOnClickListener(v -> {
            date_finish = date + time.getCurrentHour() * 60 * 60 * 1000 + time.getCurrentMinute() * 60 * 1000;
            to.setText(time.getCurrentHour() + ":" + time.getCurrentMinute());
        });

        Button create = findViewById(R.id.create);
        create.setOnClickListener(v -> {

            Realm realm = Realm.getInstance(RealmUtility.getDefaultConfig());
            Number newId = realm.where(Event.class).max("id");
            if(newId==null) {
                newId =0;
            }

            realm.beginTransaction();

            Event e = realm.createObject(Event.class);
            e.setId(newId.intValue() + 1);
            e.setDate_start(date_start);
            e.setDate_finish(date_finish);
            e.setName(name.getText().toString());
            e.setDescription(description.getText().toString());

            realm.commitTransaction();

            onBackPressed();
        });

    }
}
