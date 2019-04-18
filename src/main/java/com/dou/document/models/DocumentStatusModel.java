package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class DocumentStatusModel {

	private String statusDmtCd;
	private String statusDmtNm;
	public DocumentStatusModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatusDmtCd() {
		return statusDmtCd;
	}

	public void setStatusDmtCd(String statusDmtCd) {
		this.statusDmtCd = statusDmtCd;
	}

	public String getStatusDmtNm() {
		return statusDmtNm;
	}

	public void setStatusDmtNm(String statusDmtNm) {
		this.statusDmtNm = statusDmtNm;
	}
}
