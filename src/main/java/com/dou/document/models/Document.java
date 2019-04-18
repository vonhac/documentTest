package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class Document {
    private String documentId;
    private String customerNm;
    private String productNm;
    private String saleCd;
    private String saleNm;
    private String saleChannel;
    private String sendDt;
    private String dateEmtry;
    private Date distributeDt;
    private String documentSts;
    private String bpoCheck;
    private String idNo;
    private int documentCd;
    private String distributedCsr;
    private String retrivalCsr;

    public String getDistributedCsr() {
        return distributedCsr;
    }

    public void setDistributedCsr(String distributedCsr) {
        this.distributedCsr = distributedCsr;
    }

    public String getRetrivalCsr() {
        return retrivalCsr;
    }

    public void setRetrivalCsr(String retrivalCsr) {
        this.retrivalCsr = retrivalCsr;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCustomerNm() {
        return customerNm;
    }

    public void setCustomerNm(String customerNm) {
        this.customerNm = customerNm;
    }

    public String getProductNm() {
        return productNm;
    }

    public void setProductNm(String productNm) {
        this.productNm = productNm;
    }

    public String getSaleCd() {
        return saleCd;
    }

    public void setSaleCd(String saleCd) {
        this.saleCd = saleCd;
    }

    public String getSaleNm() {
        return saleNm;
    }

    public void setSaleNm(String saleNm) {
        this.saleNm = saleNm;
    }


    public String getSendDt() {
        return sendDt;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public void setSendDt(String sendDt) {
        this.sendDt = sendDt;
    }

    public String getDateEmtry() {
        return dateEmtry;
    }

    public void setDateEmtry(String dateEmtry) {
        this.dateEmtry = dateEmtry;
    }

    public Date getDistributeDt() {
        return distributeDt;
    }

    public void setDistributeDt(Date distributeDt) {
        this.distributeDt = distributeDt;
    }

    public String getDocumentSts() {
        return documentSts;
    }

    public void setDocumentSts(String documentSts) {
        this.documentSts = documentSts;
    }

    public String getBpoCheck() {
        return bpoCheck;
    }

    public void setBpoCheck(String bpoCheck) {
        this.bpoCheck = bpoCheck;
    }


}
