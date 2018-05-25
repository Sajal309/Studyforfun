package com.example.hp.studyforfun;

/**
 * Created by hp on 17-01-2018.
 */

public class FirebaseModel {

    private String id1;
    private String send_by;
    private String user;
    private String msg;
    private String time;
    private String read;

    public String getRead() {
        return read;
    }

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    private String time1;
    private String time2;

    public String getId1() {
        return id1;
    }

    public String getTime() {
        return time;
    }

    public String getFile() {
        return file;
    }

    private String file;


    public FirebaseModel(){

    }
    public String getId() {
        return id1;
    }

    public String getSend_by() {
        return send_by;
    }

    public String getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }

    public FirebaseModel(String id1, String send_by, String user, String msg,String time,String file,String time1) {
        this.id1 = id1;
        this.send_by = send_by;
        this.user = user;
        this.msg = msg;
        this.time = time;
        this.file = file;
        this.time1 = time1;
    }
}