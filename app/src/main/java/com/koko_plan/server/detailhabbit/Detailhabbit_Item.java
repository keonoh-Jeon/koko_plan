package com.koko_plan.server.detailhabbit;

public class Detailhabbit_Item
{
    private String id;
    private String habbittitle;
    private String date;
    private int progress;
    private int curtime;
    private int totalsec;
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

    public void setCurtime(int curtime)
    {
        this.curtime = curtime;
    }
    public int getCurtime()
    {
        return curtime;
    }

    public void setTotalsec(int totalsec)
    {
        this.totalsec = totalsec;
    }
    public int getTotalsec()
    {
        return totalsec;
    }

    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
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