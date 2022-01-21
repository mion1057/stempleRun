package com.example.myapplication.data;

import java.util.Date;

public class Gallery {

    private String p_title;
    private int p_num;
    private Date p_day;
    private int p_recommend;
    private String m_name;
    private String pic_file;
    private String m_id;
    private String pic_latitude;
    private String pic_longitude;

    public String getPic_latitude() {
        return pic_latitude;
    }

    public void setPic_latitude(String pic_latitude) {
        this.pic_latitude = pic_latitude;
    }

    public String getPic_longitude() {
        return pic_longitude;
    }

    public void setPic_longitude(String pic_longitude) {
        this.pic_longitude = pic_longitude;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public int getP_num() {
        return p_num;
    }

    public void setP_num(int p_num) {
        this.p_num = p_num;
    }


    public Date getP_day() {
        return p_day;
    }

    public void setP_day(Date p_day) {
        this.p_day = p_day;
    }

    public int getP_recommend() {
        return p_recommend;
    }

    public void setP_recommend(int p_recommend) {
        this.p_recommend = p_recommend;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getPic_file() {
        return pic_file;
    }

    public void setPic_file(String pic_file) {
        this.pic_file = pic_file;
    }

}
