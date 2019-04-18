package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class DMTDeferDetailModel extends MasterModel {
    private String id;
    private String idNo;
    private String documentCd;
    private String groupCd;
    private String deferCd;
    private String deferNm;
    private String description;
    private boolean deferCheck;

    public DMTDeferDetailModel(){}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGroupCd() {
        return groupCd;
    }

    public boolean isDeferCheck() {
        return deferCheck;
    }

    public void setDeferCheck(boolean deferCheck) {
        this.deferCheck = deferCheck;
    }

    public void setGroupCd(String groupCd) {
        this.groupCd = groupCd;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDocumentCd() {
        return documentCd;
    }

    public void setDocumentCd(String documentCd) {
        this.documentCd = documentCd;
    }

    public String getDeferCd() {
        return deferCd;
    }

    public void setDeferCd(String deferCd) {
        this.deferCd = deferCd;
    }

    public String getDeferNm() {
        return deferNm;
    }

    public void setDeferNm(String deferNm) {
        this.deferNm = deferNm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
