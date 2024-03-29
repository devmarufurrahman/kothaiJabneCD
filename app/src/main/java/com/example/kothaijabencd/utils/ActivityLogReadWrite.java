package com.example.kothaijabencd.utils;

public class ActivityLogReadWrite {
    String activity_name, activity_ref, create_by, created_name, created_date, receive_by;
    int active_flag;

    public ActivityLogReadWrite(String activity_name, String activity_ref, String create_by, String created_name, String created_date, String receive_by, int active_flag) {
        this.activity_name = activity_name;
        this.activity_ref = activity_ref;
        this.create_by = create_by;
        this.created_name = created_name;
        this.created_date = created_date;
        this.receive_by = receive_by;
        this.active_flag = active_flag;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivity_ref() {
        return activity_ref;
    }

    public void setActivity_ref(String activity_ref) {
        this.activity_ref = activity_ref;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreated_name() {
        return created_name;
    }

    public void setCreated_name(String created_name) {
        this.created_name = created_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getReceive_by() {
        return receive_by;
    }

    public void setReceive_by(String receive_by) {
        this.receive_by = receive_by;
    }

    public int getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(int active_flag) {
        this.active_flag = active_flag;
    }
}