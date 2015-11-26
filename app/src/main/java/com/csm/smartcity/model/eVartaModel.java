package com.csm.smartcity.model;

/**
 * Created by samarekha on 11/19/2015.
 */
public class eVartaModel {
    private String EVARTA_NAME;
    private String POST_DATE;
    private String EVARTA_ID;
    private String RowNo;
    private String geteVartaCommentCount;
    private String geteVartaDocument;
    private String pdf_name;

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getEVARTA_NAME() {
        return EVARTA_NAME;
    }

    public void setEVARTA_NAME(String EVARTA_NAME) {
        this.EVARTA_NAME = EVARTA_NAME;
    }

    public String getPOST_DATE() {
        return POST_DATE;
    }

    public void setPOST_DATE(String POST_DATE) {
        this.POST_DATE = POST_DATE;
    }

    public String getEVARTA_ID() {
        return EVARTA_ID;
    }

    public void setEVARTA_ID(String EVARTA_ID) {
        this.EVARTA_ID = EVARTA_ID;
    }

    public String getRowNo() {
        return RowNo;
    }

    public void setRowNo(String rowNo) {
        RowNo = rowNo;
    }

    public String getGeteVartaCommentCount() {
        return geteVartaCommentCount;
    }

    public void setGeteVartaCommentCount(String geteVartaCommentCount) {
        this.geteVartaCommentCount = geteVartaCommentCount;
    }

    public String getGeteVartaDocument() {
        return geteVartaDocument;
    }

    public void setGeteVartaDocument(String geteVartaDocument) {
        this.geteVartaDocument = geteVartaDocument;
    }
}
