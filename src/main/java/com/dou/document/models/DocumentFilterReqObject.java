package com.dou.document.models;

import com.dou.document.shared.DocumentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class DocumentFilterReqObject extends MasterModel {
    private String departmentCd;
    private Boolean bpoChecking;
    private DocumentStatus statusCd;
    private Date distributedDate;
    private Date sendingDate;
    private String saleCd;
    private String documentCd;
    private String idNo;
    private String addAppID;

    public DocumentFilterReqObject() {}

    public DocumentFilterReqObject(String departmentCd, Boolean bpoChecking, DocumentStatus statusCd, Date distributedDate, String saleCd ,Date sendingDate,String documentCd, String idNo) {
        this.departmentCd = departmentCd;
        this.bpoChecking = bpoChecking;
        this.statusCd = statusCd;
        this.distributedDate = distributedDate;
        this.sendingDate = sendingDate;
        this.saleCd = saleCd;
        this.documentCd = documentCd;
        this.idNo = idNo;

    }

    public String getAddAppID() {
        return addAppID;
    }

    public void setAddAppID(String addAppID) {
        this.addAppID = addAppID;
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

    public Boolean getBpoChecking() {
        return bpoChecking;
    }

    public String getSaleCd() {
        return saleCd;
    }

    public void setSaleCd(String saleCd) {
        this.saleCd = saleCd;
    }

    public String getDepartmentCd() {
        return departmentCd;
    }

    public void setDepartmentCd(String departmentCd) {
        this.departmentCd = departmentCd;
    }


    public void setBpoChecking(Boolean bpoChecking) {
        this.bpoChecking = bpoChecking;
    }

    public DocumentStatus getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(DocumentStatus statusCd) {
        this.statusCd = statusCd;
    }

    public Date getDistributedDate() {
        return distributedDate;
    }

    public void setDistributedDate(Date distributedDate) {
        this.distributedDate = distributedDate;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }
}
