package com.koko_plan;

public class Todo_list_Item
{
    private String club, date, green;
    private int set, hole, shot;
    private double distance, shotheight;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getGreen() { return green; }
    public void setGreen(String green) { this.green = green; }

    public int getSet()
    {
        return set;
    }
    public void setSet(int set)
    {
        this.set = set;
    }

    public int getShot() { return shot;  }
    public void setShot(int shot) {  this.shot = shot;   }

    public double getDistance() { return distance;  }
    public void setDistance(double distance) {  this.distance = distance;   }

    public String getClub()
    {
        return club;
    }
    public void setClub(String club)
    {
        this.club = club;
    }

    public double getShotheight() { return shotheight;  }
    public void setShotheight(double shotheight) {  this.shotheight = shotheight;   }

    public int getHole() { return hole;  }
    public void setHole(int hole) {  this.hole = hole;   }

}