package com.example.diary.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.diary.Entitys.Event;
import com.example.diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    RecyclerView list;
    EventAdapter adapter;
    JSONArray jsonArray;
    ArrayList<Event> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceEvent) {
        super.onCreate(savedInstanceEvent);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarView = findViewById(R.id.calendar);
        list = findViewById(R.id.recyclerView);

        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            jsonArray = obj.getJSONArray("events");
            arrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Event e = new Event();

                int id = jsonObject.getInt("id");
                e.setId(id);
                String date_start = jsonObject.getString("date_start");
                e.setDate_start(Long.valueOf(date_start));
                String date_finish = jsonObject.getString("date_finish");
                e.setDate_finish(Long.valueOf(date_finish));
                String name = jsonObject.getString("name");
                e.setName(name);
                String description = jsonObject.getString("description");
                e.setDescription(description);

                arrayList.add(e);
            }

            adapter = new EventAdapter(this, arrayList);
            System.out.println(arrayList.get(0).toString());
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                ArrayList<Event> dayList = new ArrayList<>();
                long selectedTime = view.getDate();

                Calendar c1 = Calendar.getInstance();
                c1 = new GregorianCalendar(year, month, dayOfMonth);
                Calendar c2 = Calendar.getInstance();
                //c1.setTimeInMillis(selectedTime);
                for (Event e:arrayList) {
                        c2.setTimeInMillis(e.getDate_start()*1000);
                        if(c1.get(Calendar.DATE)==c2.get(Calendar.DATE)) {
                            dayList.add(e);
                        }
                }
                list.setAdapter(new EventAdapter(getBaseContext(), dayList));
            }
        });

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("events.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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

}

class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Event> Events;
    Context context;

    EventAdapter(Context context, List<Event> Events) {
        this.Events = Events;
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
        Event event = Events.get(position);
        holder.time.setText(event.getTime(context));
        holder.name.setText(event.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DayActivity.class);
                i.putExtra("event", event);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView time;
        final TextView name;

        ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            name = view.findViewById(R.id.name);
        }


    }
}