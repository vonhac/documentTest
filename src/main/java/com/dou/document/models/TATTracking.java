package com.dou.document.models;

import java.util.Date;

public class TATTracking extends MasterModel{
    private String fromDt;
    private String toDt;
    private String idNo;
    private String customerNm;
    private String agreeId;
    private String productNm;
    private String salesCd;
    private String dataEntry;
    private String branchSip;
    private Date uploadDt;
    private Date receivedDt;
    private Date distributedDt;
    private Date lastModified;
    private Date pendingDt;
    private Date undDt;
    private Date pdocDt;
    private Date finishDt;
    private String documentSts;
    private String f1Sts;
    private String distributedBy;
    private String baseOnStepData;
    private String app_id_BD2;

    public String getApp_id_BD2() {
        return app_id_BD2;
    }

    public void setApp_id_BD2(String app_id_BD2) {
        this.app_id_BD2 = app_id_BD2;
    }

    public String getBaseOnStepData() {
        return baseOnStepData;
    }

    public void setBaseOnStepData(String baseOnStepData) {
        this.baseOnStepData = baseOnStepData;
    }

    public String getDistributedBy() {
        return distributedBy;
    }

    public void setDistributedBy(String distributedBy) {
        this.distributedBy = distributedBy;
    }

    public String getFromDt() {
        return fromDt;
    }

    public void setFromDt(String fromDt) {
        this.fromDt = fromDt;
    }

    public String getToDt() {
        return toDt;
    }

    public void setToDt(String toDt) {
        this.toDt = toDt;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getAgreeId() {
        return agreeId;
    }

    public void setAgreeId(String agreeId) {
        this.agreeId = agreeId;
    }

    public String getProductNm() {
        return productNm;
    }

    public void setProductNm(String productNm) {
        this.productNm = productNm;
    }

    public String getSalesCd() {
        return salesCd;
    }

    public void setSalesCd(String salesCd) {
        this.salesCd = salesCd;
    }

    public String getDataEntry() {
        return dataEntry;
    }

    public void setDataEntry(String dataEntry) {
        this.dataEntry = dataEntry;
    }

    public String getBranchSip() {
        return branchSip;
    }

    public void setBranchSip(String branchSip) {
        this.branchSip = branchSip;
    }

    public Date getUploadDt() {
        return uploadDt;
    }

    public void setUploadDt(Date uploadDt) {
        this.uploadDt = uploadDt;
    }

    public Date getReceivedDt() {
        return receivedDt;
    }

    public void setReceivedDt(Date receivedDt) {
        this.receivedDt = receivedDt;
    }

    public Date getDistributedDt() {
        return distributedDt;
    }

    public void setDistributedDt(Date distributedDt) {
        this.distributedDt = distributedDt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getPendingDt() {
        return pendingDt;
    }

    public void setPendingDt(Date pendingDt) {
        this.pendingDt = pendingDt;
    }

    public Date getUndDt() {
        return undDt;
    }

    public void setUndDt(Date undDt) {
        this.undDt = undDt;
    }

    public Date getPdocDt() {
        return pdocDt;
    }

    public void setPdocDt(Date pdocDt) {
        this.pdocDt = pdocDt;
    }

    public Date getFinishDt() {
        return finishDt;
    }

    public void setFinishDt(Date finishDt) {
        this.finishDt = finishDt;
    }

    public String getDocumentSts() {
        return documentSts;
    }

    public void setDocumentSts(String documentSts) {
        this.documentSts = documentSts;
    }

    public String getF1Sts() {
        return f1Sts;
    }

    public void setF1Sts(String f1Sts) {
        this.f1Sts = f1Sts;
    }
}
