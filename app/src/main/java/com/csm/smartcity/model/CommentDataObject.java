package com.csm.smartcity.model;

/**
 * Created by arundhati on 11/23/2015.
 */
public class CommentDataObject {
    private String COMMENTS;
    private String COMMENT_DATE;
    private String USERID;
    private String USER_AREA_NAME;
    private String USER_NAME;
    private String USER_PIC;
    private String USER_PIC_FLAG;
    private String ID;

    public String getCOMMENTS() {
        return COMMENTS;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCOMMENTS(String COMMENTS) {
        this.COMMENTS = COMMENTS;
    }

    public String getCOMMENT_DATE() {
        return COMMENT_DATE;
    }

    public void setCOMMENT_DATE(String COMMENT_DATE) {
        this.COMMENT_DATE = COMMENT_DATE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSER_AREA_NAME() {
        return USER_AREA_NAME;
    }

    public void setUSER_AREA_NAME(String USER_AREA_NAME) {
        this.USER_AREA_NAME = USER_AREA_NAME;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_PIC() {
        return USER_PIC;
    }

    public void setUSER_PIC(String USER_PIC) {
        this.USER_PIC = USER_PIC;
    }

    public String getUSER_PIC_FLAG() {
        return USER_PIC_FLAG;
    }

    public void setUSER_PIC_FLAG(String USER_PIC_FLAG) {
        this.USER_PIC_FLAG = USER_PIC_FLAG;
    }


}
