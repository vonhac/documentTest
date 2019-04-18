package com.dou.document.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DmtDocumentSearchModel extends MasterModel{

	private String idNo;
	private String salesCd;
	private String statusCd;
	private String createdDateSearch;

	public String getCreatedDateSearch() {
		return createdDateSearch;
	}

	public void setCreatedDateSearch(String createdDateSearch) {
		this.createdDateSearch = createdDateSearch;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getSalesCd() {
		return salesCd;
	}

	public void setSalesCd(String salesCd) {
		this.salesCd = salesCd;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}


	private String dateParam;
	private String distributeDate;
	private String distributeCsr;
	private String branchId;
	private String sendToPlaceId;
	private List<BranchModel> listBranch;
	private List<SaleAccountModel> listSaleAccount;
	private List<CsrModel> listCsr;
	private List<DocumentStatusModel> listStatus;
	private List<DmtProductModel> listProduct;
	private List<ImportDocumentErrorModel> listError;
	private Boolean importError;
	private Boolean checkInvalidError;

	public String getDateParam() {
		return dateParam;
	}

	public void setDateParam(String dateParam) {
		this.dateParam = dateParam;
	}

	public String getDistributeDate() {
		return distributeDate;
	}

	public void setDistributeDate(String distributeDate) {
		this.distributeDate = distributeDate;
	}

	public String getDistributeCsr() {
		return distributeCsr;
	}

	public void setDistributeCsr(String distributeCsr) {
		this.distributeCsr = distributeCsr;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSendToPlaceId() {
		return sendToPlaceId;
	}

	public void setSendToPlaceId(String sendToPlaceId) {
		this.sendToPlaceId = sendToPlaceId;
	}

	public List<BranchModel> getListBranch() {
		return listBranch;
	}

	public void setListBranch(List<BranchModel> listBranch) {
		this.listBranch = listBranch;
	}

	public List<SaleAccountModel> getListSaleAccount() {
		return listSaleAccount;
	}

	public void setListSaleAccount(List<SaleAccountModel> listSaleAccount) {
		this.listSaleAccount = listSaleAccount;
	}

	public List<CsrModel> getListCsr() {
		return listCsr;
	}

	public void setListCsr(List<CsrModel> listCsr) {
		this.listCsr = listCsr;
	}

	public List<DocumentStatusModel> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<DocumentStatusModel> listStatus) {
		this.listStatus = listStatus;
	}

	public List<DmtProductModel> getListProduct() {
		return listProduct;
	}

	public void setListProduct(List<DmtProductModel> listProduct) {
		this.listProduct = listProduct;
	}

	public List<ImportDocumentErrorModel> getListError() {
		return listError;
	}

	public void setListError(List<ImportDocumentErrorModel> listError) {
		this.listError = listError;
	}

	public Boolean getImportError() {
		return importError;
	}

	public void setImportError(Boolean importError) {
		this.importError = importError;
	}

	public Boolean getCheckInvalidError() {
		return checkInvalidError;
	}

	public void setCheckInvalidError(Boolean checkInvalidError) {
		this.checkInvalidError = checkInvalidError;
	}
}
