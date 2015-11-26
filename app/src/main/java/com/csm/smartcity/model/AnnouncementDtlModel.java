package com.csm.smartcity.model;

/**
 * Created by samarekha on 11/19/2015.
 */
public class AnnouncementDtlModel {
    private String ANNOUNCEMENT_DTL_NAME;
    private String Announcement_ID;

    public String getAnnouncement_ID() {
        return Announcement_ID;
    }

    public void setAnnouncement_ID(String announcement_ID) {
        Announcement_ID = announcement_ID;
    }

    private String getAnnouncementCommentCount;

    public String getANNOUNCEMENT_DTL_NAME() {
        return ANNOUNCEMENT_DTL_NAME;
    }

    public void setANNOUNCEMENT_DTL_NAME(String ANNOUNCEMENT_DTL_NAME) {
        this.ANNOUNCEMENT_DTL_NAME = ANNOUNCEMENT_DTL_NAME;
    }



    public String getGetAnnouncementCommentCount() {
        return getAnnouncementCommentCount;
    }

    public void setGetAnnouncementCommentCount(String getAnnouncementCommentCount) {
        this.getAnnouncementCommentCount = getAnnouncementCommentCount;
    }





}
