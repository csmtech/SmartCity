package com.csm.smartcity.model;

/**
 * Created by samarekha on 11/19/2015.
 */
public class AnnouncementModel {
    private String ANNOUNCEMENT_NAME;
    private String ANNOUNCEMENT_DATE;
    private String ANNOUNCEMENT_ID;

    public String getANNOUNCEMENT_NAME() {
        return ANNOUNCEMENT_NAME;
    }

    public void setANNOUNCEMENT_NAME(String ANNOUNCEMENT_NAME) {
        this.ANNOUNCEMENT_NAME = ANNOUNCEMENT_NAME;
    }

    public String getANNOUNCEMENT_DATE() {
        return ANNOUNCEMENT_DATE;
    }

    public void setANNOUNCEMENT_DATE(String ANNOUNCEMENT_DATE) {
        this.ANNOUNCEMENT_DATE = ANNOUNCEMENT_DATE;
    }

    public String getANNOUNCEMENT_ID() {
        return ANNOUNCEMENT_ID;
    }

    public void setANNOUNCEMENT_ID(String ANNOUNCEMENT_ID) {
        this.ANNOUNCEMENT_ID = ANNOUNCEMENT_ID;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    private String RowNo;



}
