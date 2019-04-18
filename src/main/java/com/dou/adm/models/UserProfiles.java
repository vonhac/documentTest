package com.dou.adm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class UserProfiles {

    @JsonIgnore
    private int id;

    private String accountId;

    @JsonIgnore
    private String mafcCode;

    @JsonIgnore
    private String drsCode;

    private String fullname;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private String mobile;

    private String positionCompanyId;

    private String branchId;

    private String area;

    @JsonProperty("department")
    private String departmentId;

    private String group;

    private String team;

    @JsonIgnore
    private String joiningDate;

    @JsonIgnore
    private String endDate;

    @JsonIgnore
    private String createdDate;

    @JsonIgnore
    private String modifiedDate;

    @JsonIgnore
    private String deletedDate;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private String modifiedBy;

    @JsonIgnore
    private String deletedBy;

    private int level;

    @JsonIgnore
    private String personalId;

    @JsonIgnore
    private String staffType;

    private String superVisorId;

    @JsonIgnore
    private String status;

    @JsonProperty("admin")
    private int isAdmin;

    @JsonIgnore
    private int isDeleted;

    private boolean isCSR;

    @JsonIgnore
    private String departmentNm;

    public UserProfiles() {}

    public UserProfiles(User user) {
        this.accountId = user.getAccountId();
        this.departmentId = user.getDepartmentId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMafcCode() {
        return mafcCode;
    }

    public void setMafcCode(String mafcCode) {
        this.mafcCode = mafcCode;
    }

    public String getDrsCode() {
        return drsCode;
    }

    public void setDrsCode(String drsCode) {
        this.drsCode = drsCode;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPositionCompanyId() {
        return positionCompanyId;
    }

    public void setPositionCompanyId(String positionCompanyId) {
        this.positionCompanyId = positionCompanyId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
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

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getSuperVisorId() {
        return superVisorId;
    }

    public void setSuperVisorId(String superVisorId) {
        this.superVisorId = superVisorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isCSR() {
        return isCSR;
    }

    public void setCSR(boolean CSR) {
        isCSR = CSR;
    }

    public String getDepartmentNm() {
        return departmentNm;
    }

    public void setDepartmentNm(String departmentNm) {
        this.departmentNm = departmentNm;
    }
}
