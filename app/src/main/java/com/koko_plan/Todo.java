package com.koko_plan;

import android.annotation.SuppressLint;
import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static android.content.ContentValues.TAG;

@Entity(tableName = "todoTable")
public class Todo {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="num")
    private int num;

    private int curtime, curcount, count;
    private int totalsec;
    private int hour, min, sec;
    private boolean isrunning;

    public Todo(int num, String date, String title, int curtime, int curcount, int count, int hour, int min, int sec, int totalsec, boolean isrunning){
        this.id = id;
        this.date = date;
        this.num = num;
        this.title = title;
        this.curtime = curtime;
        this.count = count;
        this.curcount = curcount;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.totalsec = totalsec;
        this.isrunning = isrunning;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id;  }

    public int getNum(){ return num; }
    public void setNum(int num){ this.num = num;  }

    public String getDate(){ return date; }
    public void setDate(String date){ this.date = date; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public int getCurtime(){ return curtime; }
    public void setCurtime(int curtime){ this.curtime = curtime; }

    public int getCurcount(){ return curcount; }
    public void setCurcount(int curcount){ this.curcount = curcount; }

    public int getCount(){ return count; }
    public void setCount(int count){ this.count = count; }

    public int getHour(){ return hour; }
    public void setHour(int hour){ this.hour = hour; }

    public int getMin(){ return min; }
    public void setMin(int min){ this.min = min; }

    public int getSec(){ return sec; }
    public void setSec(int sec){ this.sec = sec; }

    public int getTotalsec(){ return totalsec; }
    public void setTotalsec(int totalsec){ this.totalsec = totalsec; }

    public boolean getIsrunning(){ return isrunning; }
    public void setIsrunning(boolean isrunning){ this.isrunning = isrunning; }
}

