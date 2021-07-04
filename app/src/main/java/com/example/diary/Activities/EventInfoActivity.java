package com.example.diary.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.diary.Entitys.Event;
import com.example.diary.RealmUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.example.diary.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Получение события из базы данных
        Realm realm = Realm.getInstance(RealmUtility.getDefaultConfig());
        int eventID = getIntent().getIntExtra("eventID", 0);
        Event e = realm.where(Event.class).equalTo("id", eventID).findFirst();

        //Заполнение информации о событии
        TextView name = findViewById(R.id.name);
        name.setText(e.getName());

        TextView time = findViewById(R.id.time);
        time.setText(e.getTime(this));

        TextView description = findViewById(R.id.description);
        description.setText(e.getDescription());

        //Удаление события из базы данных
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            realm.executeTransaction(realm1 -> {
                RealmResults<Event> result = realm1.where(Event.class).equalTo("id", eventID).findAll();
                result.deleteAllFromRealm();
            });

            onBackPressed();
        });
    }

}
