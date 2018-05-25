package com.example.hp.studyforfun;

/**
 * Created by hp on 15-01-2018.
 */

public class MessageModel {


        private String name;
        private String profile;
        private String on_off;
        private String message;
        private String time;
        private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        public String getOn_off() {
            return on_off;
        }

        public void setOn_off(String on_off) {
            this.on_off = on_off;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


}
