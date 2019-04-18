package com.dou.document.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class CsrModel {
	private String fullname;
	private String accountId;
	private String salesChnl;
	private String branchNm;
	private Integer count;
	private Integer docNo = new Integer(0);
	private List<TraInfoModel> listDocument = new ArrayList<TraInfoModel>();

	public CsrModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getSalesChnl() {
		return salesChnl;
	}

	public void setSalesChnl(String salesChnl) {
		this.salesChnl = salesChnl;
	}

	public String getBranchNm() {
		return branchNm;
	}

	public void setBranchNm(String branchNm) {
		this.branchNm = branchNm;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getDocNo() {
		return docNo;
	}

	public void setDocNo(Integer docNo) {
		this.docNo = docNo;
	}

	public List<TraInfoModel> getListDocument() {
		return listDocument;
	}

	public void setListDocument(List<TraInfoModel> listDocument) {
		this.listDocument = listDocument;
	}
}
