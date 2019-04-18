package com.dou.document.models;

import java.util.List;

public class StaffInforBranchSip extends MasterModel{
    private String staffCd;
    private String staffNm;
    private String branchSip;
    private String branchSipId;
    private int IdStaffBranch;

    public String getStaffCd() {
        return staffCd;
    }

    public void setStaffCd(String staffCd) {
        this.staffCd = staffCd;
    }

    public String getStaffNm() {
        return staffNm;
    }

    public void setStaffNm(String staffNm) {
        this.staffNm = staffNm;
    }

    public String getBranchSip() {
        return branchSip;
    }

    public void setBranchSip(String branchSip) {
        this.branchSip = branchSip;
    }

    public String getBranchSipId() {
        return branchSipId;
    }

    public void setBranchSipId(String branchSipId) {
        this.branchSipId = branchSipId;
    }

    public int getIdStaffBranch() {
        return IdStaffBranch;
    }

    public void setIdStaffBranch(int idStaffBranch) {
        IdStaffBranch = idStaffBranch;
    }
}