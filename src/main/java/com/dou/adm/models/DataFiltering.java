package com.dou.adm.models;

import java.util.List;

public class DataFiltering {

    private String branchSip;

    private String accountId;

    private String author;

    private List<String> lsAcc;

    private List<String> lsBranchSip;

    private String departmentId;

    public String getBranchSip() {
        return branchSip;
    }

    public void setBranchSip(String branchSip) {
        this.branchSip = branchSip;
    }

    public DataFiltering() {}

    public DataFiltering(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getLsAcc() {
        return lsAcc;
    }

    public void setLsAcc(List<String> lsAcc) {
        this.lsAcc = lsAcc;
    }

    public List<String> getLsBranchSip() {
        return lsBranchSip;
    }

    public void setLsBranchSip(List<String> lsBranchSip) {
        this.lsBranchSip = lsBranchSip;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
