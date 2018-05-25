package com.example.hp.studyforfun;

/**
 * Created by hp on 29-12-2017.
 */

public class Model {


    private String descr;
    private String file;
    private String profile;
    private String name;
    private String like;
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLike_stat() {
        return like_stat;
    }

    public void setLike_stat(String like_stat) {
        this.like_stat = like_stat;
    }

    private String like_stat;

    public String getLike() {
        return like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setLike(String like) {
        this.like = like;
    }
}