package com.koko_plan.server.totalhabbits;

import androidx.room.PrimaryKey;

public class TotalHabbitsListReady_Item
{
    @PrimaryKey(autoGenerate = true)
    private String id;
    private String habbittitle;
    private String from;
    private String fromid;
    private String time;
    private String day;
    private Boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private int progress;
    private int randomnum;
    private float average;
    private int totalsec;

    public Boolean getMonday(){ return monday; }
    public void setMonday(Boolean monday){ this.monday = monday;  }

    public Boolean getTuesday(){ return tuesday; }
    public void setTuesday(Boolean tuesday){ this.tuesday = tuesday;  }

    public Boolean getWednesday(){ return wednesday; }
    public void setWednesday(Boolean wednesday){ this.wednesday = wednesday;  }

    public Boolean getThursday(){ return thursday; }
    public void setThursday(Boolean thursday){ this.thursday = thursday;  }

    public Boolean getFriday(){ return friday; }
    public void setFriday(Boolean friday){ this.friday = friday;  }

    public Boolean getSaturday(){ return saturday; }
    public void setSaturday(Boolean saturday){ this.saturday = saturday;  }

    public Boolean getSunday(){ return sunday; }
    public void setSunday(Boolean sunday){ this.sunday = sunday;  }

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

    public void setTotalsec(int totalsec)
    {
        this.totalsec = totalsec;
    }
    public int getTotalsec()
    {
        return totalsec;
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