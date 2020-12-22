package com.koko_plan.server;

public class Ranking_Item
{
    private String club;
    private int set, loft;
    private float average;

    public int getSet()
    {
        return set;
    }
    public void setSet(int set)
    {
        this.set = set;
    }

    public int getLoft()
    {
        return loft;
    }
    public void setLoft(int loft)
    {
        this.loft = loft;
    }

    public String getClub()
{
    return club;
}
    public void setClub(String club)
    {
        this.club = club;
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