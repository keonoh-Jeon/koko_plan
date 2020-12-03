package com.koko_plan;

import android.annotation.SuppressLint;
import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static android.content.ContentValues.TAG;

@Entity(tableName = "todoTable")
public class Todo {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int count;
    private int hour, min, sec;
    private int playcount;

    public Todo(int id, String title, int count, int hour, int min, int sec, int playcount){
        this.id = id;
        this.title = title;
        this.count = count;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.playcount = playcount;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id;  }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public int getCount(){ return count; }
    public void setCount(int count){ this.count = count; }

    public int getHour(){ return hour; }
    public void setHour(int hour){ this.hour = hour; }

    public int getMin(){ return min; }
    public void setMin(int min){ this.min = min; }

    public int getSec(){ return sec; }
    public void setSec(int sec){ this.sec = sec; }

    public int getPlaycount(){ return playcount; }
    public void setPlaycount(int pl){ this.playcount = pl; }
}

