package com.koko_plan.server.ranking;

import androidx.room.PrimaryKey;

public class Ranking_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String name;
    private int progress;
    private float average;

    public String getId(){ return id; }
    public void setId(String id){ this.id = id;  }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }
    public int getProgress()
    {
        return progress;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
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