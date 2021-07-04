package com.example.diary.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.diary.Entitys.Event;
import com.example.diary.R;
import com.example.diary.RealmUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceEvent) {
        super.onCreate(savedInstanceEvent);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.recyclerView);
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        initDay(System.currentTimeMillis());

        FloatingActionButton fab = findViewById(R.id.create);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CreationActivity.class);
            startActivity(i);
        });

        CalendarView calendarView = findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar c = new GregorianCalendar(year, month, dayOfMonth);
            initDay(c.getTimeInMillis());

        });

    }

    public void initDay(long dayBegin) {

        Realm realm = Realm.getInstance(RealmUtility.getDefaultConfig());

        RealmResults<Event> ar = realm.where(Event.class).beginGroup()
                .greaterThan("date_start", dayBegin)
                .and()
                .lessThan("date_finish", dayBegin + 24 * 60 * 60 * 1000).endGroup().findAll();

        ArrayList<Event> arrayList = new ArrayList<>(ar);
        list.setAdapter(new EventAdapter(getBaseContext(), arrayList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfEventment
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDay(System.currentTimeMillis());
    }
}

class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Event> arrayList;
    private Context context;

    EventAdapter(Context context, ArrayList<Event> arrayList) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = Realm.getInstance(RealmUtility.getDefaultConfig())
                .where(Event.class).equalTo("id", arrayList.get(position).getId())
                .findFirst();

        holder.time.setText(event.getTime(context));
        holder.name.setText(event.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, EventInfoActivity.class);
            i.putExtra("eventID", event.getId());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView time;
        final TextView name;

        ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            name = view.findViewById(R.id.name);
        }


    }
}