package com.csm.smartcity.model;

/**
 * Created by arundhati on 11/23/2015.
 */
public class CatagoryDataObject {
    private String StrCategoryDesc;
    private String strCategoryID;
    private String strIdeaCount;

    public String getStrCategoryDesc() {
        return StrCategoryDesc;
    }

    public void setStrCategoryDesc(String strCategoryDesc) {
        StrCategoryDesc = strCategoryDesc;
    }

    public String getStrCategoryID() {
        return strCategoryID;
    }

    public void setStrCategoryID(String strCategoryID) {
        this.strCategoryID = strCategoryID;
    }

    public String getStrIdeaCount() {
        return strIdeaCount;
    }

    public void setStrIdeaCount(String strIdeaCount) {
        this.strIdeaCount = strIdeaCount;
    }
}
