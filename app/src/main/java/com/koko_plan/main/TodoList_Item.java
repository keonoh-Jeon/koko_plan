package com.koko_plan.main;

import androidx.room.PrimaryKey;

public class TodoList_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String text;
    private String from;
    private String fromid;
    private String habbittitle;
    private String time;
    private String day;
    private int progress;
    private int count;
    private int num;
    private int curcount;
    private int randomnum;
    private int curtime;
    private float average;
    private boolean isrunning;
    private int totalsec;
    private int hour, min, sec;

    public String getFrom(){ return from; }
    public void setFrom(String from){ this.from = from;  }

    public String getHabbittitle(){ return habbittitle; }
    public void setHabbittitle(String habbittitle){ this.habbittitle = habbittitle; }

    public String getFromid(){ return fromid; }
    public void setFromid(String fromid){ this.fromid = fromid;  }

    public int getCount(){ return count; }
    public void setCount(int count){ this.count = count; }

    public int getNum(){ return num; }
    public void setNum(int num){ this.num = num; }

    public int getTotalsec(){ return totalsec; }
    public void setTotalsec(int totalsec){ this.totalsec = totalsec; }

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

    public int getHour(){ return hour; }
    public void setHour(int hour){ this.hour = hour; }

    public int getMin(){ return min; }
    public void setMin(int min){ this.min = min; }

    public int getSec(){ return sec; }
    public void setSec(int sec){ this.sec = sec; }

    public int getCurtime(){ return curtime; }
    public void setCurtime(int curtime){ this.curtime = curtime; }

    public void setProgress(int progress)
    {
        this.progress = progress;
    }
    public int getProgress()
    {
        return progress;
    }

    public boolean getIsrunning(){ return isrunning; }
    public void setIsrunning(boolean isrunning){ this.isrunning = isrunning; }

    public void setCurcount(int curcount)
    {
        this.curcount = curcount;
    }
    public int getCurcount()
    {
        return curcount;
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