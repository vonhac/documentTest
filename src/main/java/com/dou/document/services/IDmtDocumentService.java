package com.dou.document.services;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IDmtDocumentService {
	public ResponseObject searchDmtDocument(JwtUser user, DmtDocumentSearchModel searchModel);
	List<TraInfoModel> searchDocumentByIdNo(String idNo);
	public ResponseObject addDmtDocumentModel(JwtUser user, TraInfoModel model);
	public ResponseObject updateDmtDocument(JwtUser user, TraInfoModel model);
	public Boolean updateDocumentByIdNo(TraInfoModel model);
	public Boolean updateDmtDocument(TraInfoModel model);
	public List<BranchModel> getBranchModel(String branchId);
	public List<DocumentStatusModel> getStatusModel();
	public List<DmtProductModel> getProductModel();
	public ResponseObject deleteDmtDocument(int documentCd);
	public ResponseObject importDmtDocuments(JwtUser user, List<TraInfoModel> model);
	public ResponseObject checkSaleAccount(@Param("salesCd") String salesCd,@Param("department")  String department );

	public ResponseObject checkInvalidDocument(JwtUser user, List<TraInfoModel> model);
	public ResponseObject sendCsrDmtDocuments(JwtUser user, List<TraInfoModel> model, String sendToPlaceId);
	public List<SaleAccountModel> getSaleAccountModel(@Param("accountId") String accountId,@Param("department")  String department);
	public ResponseObject getcomboBox(String department);
	void getResultFromExternalSystem(ResultDrsModel customerStatus);
	public Boolean insertDmtDocumentStatus(DocumentDetailStatusModel model);
}
