package com.koko_plan.server.goodtext;

import androidx.room.PrimaryKey;

public class GoodText_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String text;
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

    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
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