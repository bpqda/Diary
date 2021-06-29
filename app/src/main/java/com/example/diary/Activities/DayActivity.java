package com.example.diary.Activities;

import android.os.Bundle;

import com.example.diary.Entitys.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.diary.R;

import org.w3c.dom.Text;

public class DayActivity extends AppCompatActivity {

    TextView name, time, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        description = findViewById(R.id.description);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Event e = (Event) getIntent().getSerializableExtra("event");
        name.setText(e.getName());
        time.setText(e.getTime(this));
        description.setText(e.getDescription());
    }

}
