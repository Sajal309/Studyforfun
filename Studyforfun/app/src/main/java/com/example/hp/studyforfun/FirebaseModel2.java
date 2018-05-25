package com.example.hp.studyforfun;

public class FirebaseModel2 {

    private String id2;
    private String send_by;
    private String user;
    private String msg;
    private String time;
    private String time1;
    private String time2;

    public String getTime1() {
        return time1;
    }

    public String getTime2() {
        return time2;
    }

    public String getId2() {
        return id2;
    }

    public String getTime() {
        return time;
    }

    public String getFile() {
        return file;
    }

    private String file;


    public FirebaseModel2(){

    }
    public String getId() {
        return id2;
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

    public FirebaseModel2(String id2, String send_by, String user, String msg,String time,String file,String time1) {
        this.id2 = id2;
        this.send_by = send_by;
        this.user = user;
        this.msg = msg;
        this.time = time;
        this.file = file;
        this.time1 = time1;
    }
}