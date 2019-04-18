package com.dou.document.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class DocumentDetailStatusModel extends MasterModel{
	private int id;
	private String description;
	private String fullname;
	private String statusCd;
	private int documentCd;
	
	public DocumentDetailStatusModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public int getDocumentCd() {
		return documentCd;
	}

	public void setDocumentCd(int documentCd) {
		this.documentCd = documentCd;
	}
}
