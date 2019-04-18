package com.dou.document.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties
public class DistributeModel {
	private List<CsrModel> listCsr;
	private List<TraInfoModel> listDocument;
	private Integer totalReceived;
	private Integer totalDistribute;

	public DistributeModel() {
		super();
	}

	public List<CsrModel> getListCsr() {
		return listCsr;
	}

	public void setListCsr(List<CsrModel> listCsr) {
		this.listCsr = listCsr;
	}

	public List<TraInfoModel> getListDocument() {
		return listDocument;
	}

	public void setListDocument(List<TraInfoModel> listDocument) {
		this.listDocument = listDocument;
	}

	public Integer getTotalReceived() {
		return totalReceived;
	}

	public void setTotalReceived(Integer totalReceived) {
		this.totalReceived = totalReceived;
	}

	public Integer getTotalDistribute() {
		return totalDistribute;
	}

	public void setTotalDistribute(Integer totalDistribute) {
		this.totalDistribute = totalDistribute;
	}
}
