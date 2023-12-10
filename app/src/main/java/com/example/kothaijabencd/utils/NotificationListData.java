package com.example.kothaijabencd.utils;

public class NotificationListData {
    String title, desc, time;
    int img;


    public NotificationListData(String title, String desc, String time, int img) {
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.img = img;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }


}
