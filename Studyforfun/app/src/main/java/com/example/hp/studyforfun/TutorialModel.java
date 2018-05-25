package com.example.hp.studyforfun;

/**
 * Created by hp on 02-02-2018.
 */

public class TutorialModel {

    private String id;
    private String name;
    private String descr;



    private String email;
    private String file;
    private String up_stat;
    private String down_stat;
    private String sub_stat;

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    private String time1;
    private String time2;

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
    public String getSub_stat() {
        return sub_stat;
    }

    public void setSub_stat(String sub_stat) {
        this.sub_stat = sub_stat;
    }
    private String views;

    public String getUp_stat() {
        return up_stat;
    }

    public void setUp_stat(String up_stat) {
        this.up_stat = up_stat;
    }

    public String getDown_stat() {
        return down_stat;
    }

    public void setDown_stat(String down_stat) {
        this.down_stat = down_stat;
    }

    public String getSubs() {
        return subs;
    }

    public void setSubs(String subs) {
        this.subs = subs;
    }

    private String subs;

    public String getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(String downvotes) {
        this.downvotes = downvotes;
    }

    private String votes;
    private String downvotes;
    private String time;
    private String profile;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
