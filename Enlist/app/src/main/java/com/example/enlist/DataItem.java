package com.example.enlist;

import android.widget.CheckBox;

public class DataItem {

    String title;
    String description;
    String deadline;
    String key;

    public DataItem() {
    }

    public DataItem(String title, String description, String deadline, String key) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
