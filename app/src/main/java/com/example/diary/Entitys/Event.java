package com.example.diary.Entitys;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject implements Serializable{

    int id;

   long date_start;
   long date_finish;
    String name;
    String description;

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date_start='" + date_start + '\'' +
                ", date_finish='" + date_finish + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }



    public Event(int id, long date_start, long date_finish, String name, String description) {
        this.id = id;
        this.date_start = date_start;
        this.date_finish = date_finish;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate_start() {
        return date_start;
    }

    public void setDate_start(long date_start) {
        this.date_start = date_start;
    }

    public long getDate_finish() {
        return date_finish;
    }

    public void setDate_finish(long date_finish) {
        this.date_finish = date_finish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTime(Context context) {
        Timestamp stamp1 = new Timestamp(date_start);
        Timestamp stamp2 = new Timestamp(date_finish);
        Date timestart = new Date(stamp1.getTime());
        Date timeend = new Date(stamp2.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedTimeStart = sdf.format(timestart);
        String formattedTimeEnd = sdf.format(timeend);
        return formattedTimeStart + " - " + formattedTimeEnd;
    }
    public JSONObject convertToJSON() {
        JSONObject JSONobj = new JSONObject() ;

        try {
            JSONobj.put("id", id);
            JSONobj.put("date_start", date_start);
            JSONobj.put("date_finish", date_finish);
            JSONobj.put("name", name);
            JSONobj.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONobj;
    }
}
