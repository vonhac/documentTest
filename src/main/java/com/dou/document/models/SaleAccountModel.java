package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class SaleAccountModel extends MasterModel {

	private Long stt;
	private String accountId;
	private String fullname;
	private String branchId;
	private String departmentId;
	private String salesChnl;
	private String productCd;
	private String saleNm;
	private String supervisorid;

	public SaleAccountModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSaleNm() {
		return saleNm;
	}

	public void setSaleNm(String saleNm) {
		this.saleNm = saleNm;
	}

	public String getSupervisorid() {
		return supervisorid;
	}

	public void setSupervisorid(String supervisorid) {
		this.supervisorid = supervisorid;
	}

	public String getProductCd() {
		return productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public Long getStt() {
		return stt;
	}

	public void setStt(Long stt) {
		this.stt = stt;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getSalesChnl() {
		return salesChnl;
	}

	public void setSalesChnl(String salesChnl) {
		this.salesChnl = salesChnl;
	}
}
