package com.rohit.locustryapp.models;

import java.util.List;

/**
 * Created by oust on 6/5/19.
 */

public class ModuleData {
    private String type,id,title;
    private DataMap dataMap;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMap dataMap) {
        this.dataMap = dataMap;
    }

    public boolean isNotNull(){
        if(this==null){
            return false;
        }
        return true;
    }

    public int getTypeInt(){
        String type = this.type;
        if (type.equals("PHOTO")) {
            return 0;
        } else if (type.equals("SINGLE_CHOICE")) {
            return 1;
        } else if (type.equals("COMMENT")) {
            return 2;
        }
        return -1;
    }

}
