/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.models;

import com.dou.document.shared.DocumentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class TraInfoModel  extends MasterModel{
    private int documentCd;
    private String idNo;
    private String customerNm;
    private String productId;
    private String productNm;
    private String manager;
    private String salesChnl;
    private String salesCd;
    private String salesNm;
    private String description;
    private String branchId;
    private String branchNm;
    private Boolean noteBpo;
    private String receivedBy;
    private String distributeCsr;
    private String retrievalCsr;
    private Boolean isDeleted;
    private Boolean partner;
    private Boolean bpoCheck;
    private String statusCd;
    private Date sendDate;
    private Date distributedDate;
    private  String statusNm;
    private  Date deadline;
    private  String channelId;
    private  String appId;
    private String customerId;
    private int type;
    private Date statusDateCurrent;
    private String invalidDescription;
    private String csrCd;
    private Date receivedDate;
    private Date cancelStepReceiveDate;
    private String status;
    private Date receivedDate1;

    public String getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(String receivedFlag) {
        this.receivedFlag = receivedFlag;
    }

    private String receivedFlag;


    public TraInfoModel()  {}

    public void setSTATUSENUM(DocumentStatus status) {
        this.status = status.getDescription();
        statusCd = status.toString();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getReceivedDate1() {
        return receivedDate1;
    }

    public void setReceivedDate1(Date receivedDate1) {
        this.receivedDate1 = receivedDate1;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStatusNm() {
        return statusNm;
    }

    public void setStatusNm(String statusNm) {
        this.statusNm = statusNm;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getDistributedDate() {
        return distributedDate;
    }

    public void setDistributedDate(Date distributedDate) {
        this.distributedDate = distributedDate;
    }

    public int getDocumentCd() {
        return documentCd;
    }

    public void setDocumentCd(int documentCd) {
        this.documentCd = documentCd;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductNm() {
        return productNm;
    }

    public void setProductNm(String productNm) {
        this.productNm = productNm;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getSalesChnl() {
        return salesChnl;
    }

    public void setSalesChnl(String salesChnl) {
        this.salesChnl = salesChnl;
    }

    public String getSalesCd() {
        return salesCd;
    }

    public void setSalesCd(String salesCd) {
        this.salesCd = salesCd;
    }

    public String getSalesNm() {
        return salesNm;
    }

    public void setSalesNm(String salesNm) {
        this.salesNm = salesNm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchNm() {
        return branchNm;
    }

    public void setBranchNm(String branchNm) {
        this.branchNm = branchNm;
    }

    public Boolean getNoteBpo() {
        return noteBpo;
    }

    public void setNoteBpo(Boolean noteBpo) {
        this.noteBpo = noteBpo;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getDistributeCsr() {
        return distributeCsr;
    }

    public void setDistributeCsr(String distributeCsr) {
        this.distributeCsr = distributeCsr;
    }

    public String getRetrievalCsr() {
        return retrievalCsr;
    }

    public void setRetrievalCsr(String retrievalCsr) {
        this.retrievalCsr = retrievalCsr;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getPartner() {
        return partner;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    public Boolean getBpoCheck() {
        return bpoCheck;
    }

    public void setBpoCheck(Boolean bpoCheck) {
        this.bpoCheck = bpoCheck;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getStatusDateCurrent() {
        return statusDateCurrent;
    }

    public void setStatusDateCurrent(Date statusDateCurrent) {
        this.statusDateCurrent = statusDateCurrent;
    }

    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public String getCsrCd() {
        return csrCd;
    }

    public void setCsrCd(String csrCd) {
        this.csrCd = csrCd;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getCancelStepReceiveDate() {
        return cancelStepReceiveDate;
    }

    public void setCancelStepReceiveDate(Date cancelStepReceiveDate) {
        this.cancelStepReceiveDate = cancelStepReceiveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
