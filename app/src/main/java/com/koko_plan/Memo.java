package com.koko_plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memoTable")
public class Memo {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String contents;
    private String cycle;

    private int time;

    public Memo(String title, String contents, String cycle, int time){
        this.title = title;
        this.contents = contents;
        this.cycle = cycle;
        this.time = time;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id;  }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getContents(){ return contents; }
    public void setContents(String in){ this.contents = in; }

    public String getCycle(){ return cycle; }
    public void setCycle(String cy){ this.cycle = cy; }

    public int getTime(){ return time; }
    public void setTime(int ti){ this.time = ti; }

    @Override
    public String toString(){
        return "RecordData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cycle='" + cycle + '\'' +
                ", contents='" + contents + '\'' +
                ", time='" + time + '\''
                + '}';
    }
}

