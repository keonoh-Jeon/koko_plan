package com.koko_plan.server.habbitlist;

import androidx.room.PrimaryKey;

public class HabbitList_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String title;
    private String from;
    private String time;
    private int progress;
    private int randomnum;
    private float average;

    public String getFrom(){ return from; }
    public void setFrom(String from){ this.from = from;  }

    public String getTime(){ return time; }
    public void setTime(String time){ this.time = time;  }

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

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
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