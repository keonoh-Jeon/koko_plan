package com.koko_plan;

import android.annotation.SuppressLint;
import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static android.content.ContentValues.TAG;

@Entity(tableName = "memoTable")
public class Memo {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String cycle;

    private int time, hour, min, sec;
    private int playcount;

    public Memo(String title, String cycle, int time, int playcount){
        this.title = title;
        this.cycle = cycle;
        this.time = time;
        this.playcount = playcount;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id;  }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getCycle(){ return cycle; }
    public void setCycle(String cy){ this.cycle = cy; }

    public int getTime(){ return time; }
    public void setTime(int ti){ this.time = ti; }

    public int getHour(){ return hour; }
    public void setHour(int hour){ this.hour = hour; }

    public int getMin(){ return min; }
    public void setMin(int min){ this.min = min; }

    public int getSec(){ return sec; }
    public void setSec(int sec){ this.sec = sec; }

    public int getPlaycount(){ return playcount; }
    public void setPlaycount(int pl){ this.playcount = pl; }

    @Override
    public String toString(){
        return "RecordData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cycle='" + cycle + '\'' +
                ", time='" + time + '\'' +
                ", playcount='" + playcount + '\'' +
                '}';
    }
}

