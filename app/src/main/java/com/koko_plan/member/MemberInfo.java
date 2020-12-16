package com.koko_plan.member;

public class MemberInfo {

    private String name;
    private String birthday;
    private String gender;

    public MemberInfo(String name, String birthday, String gender ){
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
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

}
