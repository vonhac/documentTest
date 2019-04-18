package com.dou.document.models;


import java.util.List;

public class ImportErrorModel {

	private List<ImportDocumentErrorModel> listError;
	private Boolean importError;

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
}
