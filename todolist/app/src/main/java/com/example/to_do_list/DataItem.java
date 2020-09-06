package com.example.to_do_list;

public class DataItem {

    String title_layout;
    String description_layout;
    String deadline_layout;

    public DataItem() {
    }

    public DataItem(String title_layout, String description_layout, String deadline_layout) {
        this.title_layout = title_layout;
        this.description_layout = description_layout;
        this.deadline_layout = deadline_layout;
    }

    public String getTitle_layout() {
        return title_layout;
    }

    public void setTitle_layout(String title_layout) {
        this.title_layout = title_layout;
    }

    public String getDescription_layout() {
        return description_layout;
    }

    public void setDescription_layout(String description_layout) {
        this.description_layout = description_layout;
    }

    public String getDeadline_layout() {
        return deadline_layout;
    }

    public void setDeadline_layout(String deadline_layout) {
        this.deadline_layout = deadline_layout;
    }
}
