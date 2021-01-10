package com.koko_plan.server.totalhabbits;

import androidx.room.PrimaryKey;

public class TotalHabbitsList_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String habbittitle;
    private String from;
    private String fromid;
    private String time;
    private String day;
    private int progress;
    private int randomnum;
    private float average;

    public String getFrom(){ return from; }
    public void setFrom(String from){ this.from = from;  }

    public String getFromid(){ return fromid; }
    public void setFromid(String fromid){ this.fromid = fromid;  }

    public String getTime(){ return time; }
    public void setTime(String time){ this.time = time;  }

    public String getDay(){ return day; }
    public void setDay(String day){ this.day = day;  }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id;  }

    public void setRandomnum(int randomnum)
    {
        this.randomnum = randomnum;
    }
    public int getRandomnum()  {     return randomnum;   }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }
    public int getProgress()
    {
        return progress;
    }

    public String getHabbittitle()
    {
        return habbittitle;
    }
    public void setHabbittitle(String habbittitle)
    {
        this.habbittitle = habbittitle;
    }

    public float getAverage()
    {
        return average;
    }
    public void setAverage(float average)
    {
        this.average = average;
    }
}