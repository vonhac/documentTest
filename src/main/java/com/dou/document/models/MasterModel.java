package com.dou.document.models;

import com.dou.adm.models.DataFiltering;

import java.util.Date;

public class MasterModel {

    private String branchSip;

    private String createdBy;

    private String modifiedBy;

    private Date createdDate;

    private Date modifiedDate;

    private DataFiltering filtering;

    public String getBranchSip() {
        return branchSip;
    }

    public void setBranchSip(String branchSip) {
        this.branchSip = branchSip;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DataFiltering getFiltering() {
        return filtering;
    }

    public void setFiltering(DataFiltering filtering) {
        this.filtering = filtering;
    }
}
