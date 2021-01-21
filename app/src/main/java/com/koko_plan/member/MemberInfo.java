package com.koko_plan.member;

public class MemberInfo {

    private String name;
    private String birthday;
    private String gender;
    private String id;
    private int getcount;


    public MemberInfo(String name, String birthday, String gender, String id, int getcount){
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getBirthday(){
        return this.birthday;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }

    public int getGetcount(){
        return this.getcount;
    }
    public void setGetcount(int getcount){
        this.getcount = getcount;
    }

}
