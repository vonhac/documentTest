package com.dou.document.services.impl;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.DepartmentConstant;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.DmtDocumentMapper;
import com.dou.document.models.*;
import com.dou.document.services.IDmtDocumentService;
import com.dou.document.shared.DocumentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DmtDocumentService implements IDmtDocumentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmtDocumentService.class);

	/* condition of result return after it was check system*/
	private static List<Integer> VALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{0, 34, 35});
    private static List<Integer> INVALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{2, 4, 5, 31, 32});

	/* this casd: document of call center*/
	private static List<Integer> VALID_DOCUMENT_RESULTS_CALL_CENTER = Arrays.asList(new Integer[]{0, 34, 35, 4});
	private static List<Integer> INVALID_DOCUMENT_RESULTS_CALL_CENTER = Arrays.asList(new Integer[]{2, 5, 31, 32});

	@Autowired
	private DmtDocumentMapper mapper;

	@Override
	public ResponseObject searchDmtDocument(JwtUser user, DmtDocumentSearchModel searchModel) {
		ResponseObject responseObject = new ResponseObject();
		try{
			List<TraInfoModel> listData = mapper.searchDmtDocumentModel(searchModel);
            responseObject.setData(listData);
            responseObject.setSuccess(true);
            responseObject.setMessage(CommonStrings.RESP_MSG_SUCCESS);

		}catch(Exception e){
			responseObject.setSuccess(false);
			responseObject.setFailMessage("Dat	a Status : Error Data Invalid");
			LOGGER.error("Error when call searchDmtDocument in DmtDocumentController class: .", e);
		}
		return responseObject;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public ResponseObject addDmtDocumentModel(JwtUser user, TraInfoModel model) {
		ResponseObject responseObject = new ResponseObject();
		model.setStatusDateCurrent(new Date());
		model.setSTATUSENUM(DocumentStatus.ORIGINAL);
		int isAdmin = user.getProfiles().getIsAdmin();
		try{
			if ( isAdmin == 0) {
				if (StringUtils.isEmpty(model.getSalesCd())) {
					return ResponseObject.failResponse("Please input valid sale code!");
				}
			}else if( StringUtils.hasText(model.getSalesCd()) &&  model.getPartner() == false){
				List<SaleAccountModel> modelSale = getSaleAccountModel(model.getSalesCd(),user.getProfiles().getDepartmentId());
				if(modelSale.size() == 0){
					return ResponseObject.failResponse("SaleCode don't belong SaleChannel");
				}
			}
			SaleAccountModel saleName = mapper.retrieveNameSaleById(user.getTargetProfileTable(), model.getSalesCd());
			SaleAccountModel accountModel = mapper.getSaleAccountModelByDM(model.getChannelId());
			model.setSalesChnl(accountModel.getSalesChnl());
			model.setSalesNm(saleName.getFullname());
			model.setManager(saleName.getSupervisorid());
			Boolean type = mapper.addDmtDocumentModel(model);
			DocumentDetailStatusModel statusModel = createStatusObject(user, model, DocumentStatus.ORIGINAL, "");
			Boolean resultStatus = mapper.insertDmtDocumentStatus(statusModel);
			if(type && resultStatus){
				responseObject.setSuccess(true);
				responseObject.setMessage("Registered successful!");
				return responseObject;
			}else{
				responseObject.setSuccess(false);
				responseObject.setMessage("Registered failed!");
				return responseObject;
			}
		}catch(Exception e){
			responseObject.setSuccess(false);
			responseObject.setFailMessage("Data Status : Error ");
	        LOGGER.error("Error when call addDmtDocumentModel in DmtDocumentController class: .", e);
		}
		return ResponseObject.failResponse("Error when add new Document APP");
	}

	@Override
	@Transactional
	public ResponseObject updateDmtDocument(JwtUser user, TraInfoModel model) {
		ResponseObject responseObject = new ResponseObject();
		int isAdmin = user.getProfiles().getIsAdmin();
		try{
			if ( isAdmin == 0) {
				if (StringUtils.isEmpty(model.getSalesCd())) {
					return ResponseObject.failResponse("Please input valid sale code!");
				}
			}else if( StringUtils.hasText(model.getSalesCd()) &&  model.getPartner() == false){
				List<SaleAccountModel> modelSale = getSaleAccountModel(model.getSalesCd(),user.getProfiles().getDepartmentId());
				if(modelSale.size() == 0){
					return ResponseObject.failResponse("Please input valid sale code!");
				}
			}
			//Save document mới bỏ qua Sale Code
			SaleAccountModel saleName = mapper.retrieveNameSaleById(user.getTargetProfileTable(), model.getSalesCd());
			SaleAccountModel accountModel = mapper.getSaleAccountModelByDM(model.getChannelId());
			model.setSalesChnl(accountModel.getSalesChnl());
			model.setSalesNm(saleName.getFullname());
			Boolean type = mapper.updateDmtDocument(model);
			if(type ){
				responseObject.setMessage("Updated successful!");
				return responseObject;
			}else {
				responseObject.setFailMessage("Updated failed!");
				return responseObject;
			}
		}
		catch(Exception e){
			responseObject.setSuccess(false);
			responseObject.setFailMessage("Data Status : Error ");
			LOGGER.error("Error when call Update Documents in DmtDocumentController class: .", e);
		}
		return ResponseObject.failResponse("Error when update Document APP");
	}

	@Override
	public List<BranchModel> getBranchModel(String branchId) {
		List<BranchModel> listData = mapper.getBranchModel(branchId);
        return listData;
	}

	@Override
	@Transactional
	public ResponseObject deleteDmtDocument(int documentCd) {
		ResponseObject responseObject =new ResponseObject();
		int result = 0;
		try {
			result = mapper.deleteDmtDocument(documentCd);
			if(result > 0){
				responseObject.setMessage( CommonStrings.RESP_MSG_DELETE_SUCCESS);
			}
		}catch (Exception e){
			responseObject.setFailMessage(CommonStrings.RESP_MSG_DELETE_FAIL);
			LOGGER.error("Error when call deleteDocumentModel in DmtDocumentController class: .", e);
		}
		return responseObject;
	}


	@Override
	public ResponseObject importDmtDocuments(JwtUser user, List<TraInfoModel> model) {
		ResponseObject responseObject = new ResponseObject();
		ImportErrorModel result = new ImportErrorModel();
		List<ImportDocumentErrorModel> errors = new ArrayList<>();
		List<String> listTemp = new ArrayList<>();
		int isAdmin = user.getProfiles().getIsAdmin();
		if(model.size() > 0) {
			for (TraInfoModel models : model) {
				StringBuilder messageError = new StringBuilder();
				if(models.getIdNo().length() < 8 ){
					messageError.append(" || IdNo invalid length");
				}
				for(int i = 0; i<listTemp.size();i++){
					boolean check = listTemp.get(i).equals(models.getIdNo());
					if(check) {
						messageError.append(" || Duplicate IdNo in this file");
					}else {
						listTemp.add(models.getIdNo());
						break;
					}
				}
				if(listTemp.size() == 0){
					listTemp.add(models.getIdNo());
				}
				if (models.getSalesCd() == null || models.getSalesCd().equals("")) {
					messageError.append(" || SaleCode invalid!");
				}
				else {
					List<SaleAccountModel> saleAccounts = getSaleAccountModel(models.getSalesCd(),user.getProfiles().getDepartmentId());
					if (saleAccounts == null || saleAccounts.size() == 0) {
						messageError.append(" || SaleCode not exist!");
					}else{
						if(isAdmin == 0 && !models.getSalesCd().equalsIgnoreCase(user.getProfiles().getAccountId())){
							messageError.append(" || SaleCode is not true");
						}
					}
				}
				List<BranchModel> branchList = getBranchModel(models.getBranchId());
				if(!user.getProfiles().isCSR()){
					 if(!"".equalsIgnoreCase(models.getBranchId())) {
						 if (branchList.size() == 0 || branchList.size() > 1) {
							 if (branchList.size() == 0) {
								 messageError.append(" || Branch/SIP not exist");
							 } else if (branchList.size() > 1) {
								 messageError.append(" || Branch/SIP duplicate");
							 }
						 }

					 }
				}else if(user.getProfiles().isCSR() && !user.getProfiles().getBranchId().equalsIgnoreCase(branchList.get(0).getBranchId())){
					messageError.append(" || Branch/SIP is not true");
				}
				List<DmtProductModel> listProduct = mapper.getProductModel(models.getProductId());
				if (listProduct.size() == 0 || listProduct.size() > 1) {
					if (listProduct.size() == 0) {
						messageError.append(" || Product ID not exist");
					}
					else {
						messageError.append(" || Product ID duplicate");
					}
				}
				if(!StringUtils.isEmpty(messageError)){
					createImportErrorData(messageError.substring(4), models, errors);
				}

			}
		}
		result.setListError(errors);
		if (errors.size() > 0) {
			responseObject.setSuccess(false);
			result.setImportError(true);
		}
		else {
			result.setImportError(false);
		}
		responseObject.setData(result);
		if(model.size()>0 && errors.size() == 0){
			for (TraInfoModel models : model) {
				List<BranchModel> branchList = null;
				if(user.getProfiles().isCSR()){
					branchList = getBranchModel(user.getProfiles().getBranchId());
				}else{
					branchList = getBranchModel(models.getBranchId());
				}
				List<DmtProductModel> listProduct = mapper.getProductModel(models.getProductId());
				SaleAccountModel saleAccounts = mapper.retrieveNameSaleById(user.getTargetProfileTable(), models.getSalesCd());
				SaleAccountModel accountModel = mapper.getSaleAccountModelByDM(user.getProfiles().getDepartmentId());
				models.setSalesChnl(accountModel.getSalesChnl());
				models.setSalesNm(saleAccounts.getFullname());
				models.setManager(saleAccounts.getSupervisorid());
				models.setChannelId(saleAccounts.getDepartmentId());
				if(user.getProfiles().isCSR()){
					models.setBranchNm(branchList.get(0).getBranchNm());
					models.setBranchId(branchList.get(0).getBranchId());
				}
				models.setProductNm(listProduct.get(0).getProductNm());
				models.setSTATUSENUM(DocumentStatus.ORIGINAL);
				try {
					models.setStatusDateCurrent(new Date());
					Boolean type = mapper.addDmtDocumentModel(models);
					DocumentDetailStatusModel statusModel = createStatusObject(user, models, DocumentStatus.ORIGINAL, "");
					Boolean resultStatus = mapper.insertDmtDocumentStatus(statusModel);
					if (!type && !resultStatus) {
						createImportErrorData("Registered failed!", models, errors);
					}
				} catch (Exception e) {
					createImportErrorData("Data Status : Error ", models, errors);
					LOGGER.error("Error when call importDmtDocuments in DmtDocumentService class: .", e);
				}
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject checkSaleAccount(String salesCd, String department) {
		ResponseObject responseObject = new ResponseObject();
		try{
			if(salesCd == null){
				return ResponseObject.failResponse("Data Invalid");
			}
			SaleAccountModel check = mapper.checkSaleCode(salesCd, department);
			if(check == null){
				responseObject.setMessage(CommonStrings.RESP_MSG_NOT_FOUND);
				responseObject.setSuccess(false);
			}else{
				responseObject.setData(check);
				responseObject.setMessage("Found Data Successful");
			}
		}catch(Exception e){
			responseObject.setMessage(CommonStrings.RESP_MSG_NOT_FOUND);
			responseObject.setSuccess(false);
			LOGGER.error("Error when call checkSaleAccount : ", e);
		}
		return responseObject;
	}

	private String statusCurrent(List<TraInfoModel> documentModels){
		if(documentModels.size() == 0){
			return "ORIGINAL";
		}else{
			return documentModels.get(0).getStatusCd();
		}
	}

	@Override
	public ResponseObject checkInvalidDocument(JwtUser user,
			List<TraInfoModel> models) {
		ResponseObject response = new ResponseObject();
		try {
			List<TraInfoModel> rs = new ArrayList<>();
			DocumentDetailStatusModel statusModel = new DocumentDetailStatusModel();
			for ( int i = 0; i < models.size(); i++){
				boolean isValid = false;
				ResultDrsModel drsModel = new ResultDrsModel();
				drsModel.setIdOrPhone(models.get(i).getIdNo());
				drsModel.setId(models.get(i).getDocumentCd());
				// check Current System
				List<TraInfoModel> result = mapper.check(drsModel.getIdOrPhone());
				if(result.size() == 0){
					isValid = true;
				}
				DocumentStatus prevStatus = DocumentStatus.fromString(statusCurrent(result));
				switch (prevStatus){
					case RECEIVED:
					case DISTRIBUTED:
					case DEFER:
					case MODIFIED:
						Date receiveDate = result.get(0).getReceivedDate1();
						Calendar c = Calendar.getInstance();
						c.setTime(receiveDate);
						c.add(Calendar.DATE, 3);
						receiveDate = c.getTime();
						Date currentDate = new Date();
						if (receiveDate.after(currentDate) || receiveDate.equals(currentDate)) {
							List<TraInfoModel> list = mapper.searchDocumentByDMT(result.get(0).getDocumentCd());
							statusModel = createStatusObject(user, models.get(i), DocumentStatus.INVALID,
									"Invalid  by Document of "  + list.get(0).getSalesChnl());
							models.get(i).setInvalidDescription("Invalid  by Document of " + list.get(0).getSalesChnl());
							models.get(i).setSTATUSENUM(DocumentStatus.INVALID);
						}
						else {
							isValid = true;
						}
						break;
					case SENDING:
					case VALID:
					case CANCEL_STEP_RECEIVED:
					case CANCEL_STEP_PASS:
					case CANCEL_AUTO:
					case CANCEL:
					case PASS:
						isValid = true;
						break;
				}
				//check F1
				if (isValid) {
					drsModel.setIdOrPhone(models.get(i).getIdNo());
					if (drsModel.getIdOrPhone() != null && drsModel.getChannelId() != DepartmentConstant.DEPR_CALLCENTER) {
						getResultFromExternalSystem(drsModel);
						if (VALID_DOCUMENT_RESULTS.contains(drsModel.getSum())) {
							statusModel = createStatusObject(user, models.get(i), DocumentStatus.VALID, "");
							models.get(i).setSTATUSENUM(DocumentStatus.VALID);
						} else if (INVALID_DOCUMENT_RESULTS.contains(drsModel.getSum())) {
							statusModel = createStatusObject(user, models.get(i), DocumentStatus.INVALID, "Invalid on F1 System");
							models.get(i).setInvalidDescription("Invalid on F1 System");
							models.get(i).setSTATUSENUM(DocumentStatus.INVALID);
						}
					} else if (drsModel.getIdOrPhone() != null && drsModel.getChannelId() == DepartmentConstant.DEPR_CALLCENTER) {
						getResultFromExternalSystem(drsModel);
						if (VALID_DOCUMENT_RESULTS_CALL_CENTER.contains(drsModel.getSum())) {
							statusModel = createStatusObject(user, models.get(i), DocumentStatus.VALID, "");
							models.get(i).setSTATUSENUM(DocumentStatus.VALID);
						} else if (INVALID_DOCUMENT_RESULTS_CALL_CENTER.contains(drsModel.getSum())) {
							statusModel = createStatusObject(user, models.get(i), DocumentStatus.INVALID, "Invalid on F1 System");
							models.get(i).setInvalidDescription("Invalid on F1 System");
							models.get(i).setSTATUSENUM(DocumentStatus.INVALID);
						}
					}
				}
				insertDmtDocumentStatus(statusModel);
				rs.add(models.get(i));
			}
			response.setMessage("Registered successful!");
			response.setData(rs);
		}catch (Exception e){
			LOGGER.error("Error when call checkInvalidDocument in DmtDocumentController class: .", e);
			response.setFailMessage("Check Invalid error ");
		}
		return response;
	}

	public boolean checkValidInteger(String value) {
		try {
			Integer.parseInt(value);
		}
		catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	@Override
	public ResponseObject sendCsrDmtDocuments(JwtUser user,
			List<TraInfoModel> models, String sendToPlaceID) {
		ResponseObject response = new ResponseObject();
		try {
            BranchModel branch = null;
		    if (StringUtils.isEmpty(sendToPlaceID)) {
                for (TraInfoModel model : models) {
                    if (StringUtils.isEmpty(model.getBranchId())) {
                        return ResponseObject.failResponse("Please provide a branch before sending");
                    }
                }
            } else {
                List<BranchModel> branchList = getBranchModel(sendToPlaceID);
                if (branchList == null || branchList.size() == 0) {
                    response.setFailMessage("Please provide a correct branch");
                    return response;
                }
                branch = branchList.get(0);
            }

			for (TraInfoModel model : models) {
				model.setSTATUSENUM(DocumentStatus.SENDING);
				if(StringUtils.isEmpty(model.getBranchId()) && branch != null){
					model.setBranchNm(branch.getBranchNm());
					model.setBranchId(branch.getBranchId());
				}
				model.setSendDate(new Date());
				model.setStatusDateCurrent(new Date());
				Boolean rs = mapper.updateDmtDocumentStatus(model);
				DocumentDetailStatusModel statusModel = createStatusObject(user, model, DocumentStatus.SENDING, "");
				Boolean addStatusResult = mapper.insertDmtDocumentStatus(statusModel);
				if (!rs || !addStatusResult) {
					model.setSTATUSENUM(DocumentStatus.VALID);
				}
			}

			response.setMessage("Registered successful!");
			response.setData(models);
		} catch (Exception e) {
			response.setFailMessage("Data Status : Error ");
			LOGGER.error("Error when call ImportEmployee in DmtDocumentController class: .", e);
		}
		return response;
	}

	@Override
	public List<SaleAccountModel> getSaleAccountModel(String accountId, String department) {
		List<SaleAccountModel> listData = mapper.getSaleAccountModel(accountId,department );
        return listData;
	}

	@Override
	public ResponseObject getcomboBox(String department) {
		ResponseObject responseObject = new ResponseObject();
		try{
			DmtDocumentSearchModel model = new DmtDocumentSearchModel();
			List<BranchModel> listModel = getBranchModel("");
			List<DocumentStatusModel> listStatus = mapper.getStatusModel();
			List<DmtProductModel> listProduct =  mapper.getProductModel("");
			model.setListBranch(listModel);
			model.setListStatus(listStatus);
			model.setListProduct(listProduct);
			responseObject.setSuccess(true);
			responseObject.setMessage("Successful!");
			responseObject.setData(model);
		}catch(Exception e){
			LOGGER.error("Error when call  getcomboBox in  class: .", e);
		}
		return responseObject;
	}

	@Override
	public void getResultFromExternalSystem(ResultDrsModel customerStatus) {
		mapper.getResultFromExternalSystem(customerStatus);
	}

	@Override
	public List<DocumentStatusModel> getStatusModel() {
		List<DocumentStatusModel> listData = mapper.getStatusModel();
        return listData;
	}

	@Override
	public List<DmtProductModel> getProductModel() {
		List<DmtProductModel> listData = mapper.getProductModel("");
        return listData;
	}

	@Override
	@Transactional
	public Boolean insertDmtDocumentStatus(DocumentDetailStatusModel model) {
		Boolean result = mapper.insertDmtDocumentStatus(model);
		return result;
	}

	public DocumentDetailStatusModel createStatusObject(JwtUser user, TraInfoModel docmt, DocumentStatus status, String description) {
		DocumentDetailStatusModel model = new DocumentDetailStatusModel();
		model.setDocumentCd(docmt.getDocumentCd());
		model.setStatusCd(status.toString()); // set status
		model.setDescription(description);
		return model;
	}


	public void createImportErrorData(String message, TraInfoModel model, List<ImportDocumentErrorModel> errors) {
		ImportDocumentErrorModel error = new ImportDocumentErrorModel();
		error.setBranchId(model.getBranchId());
		error.setCustomerNm(model.getCustomerNm());
		error.setProductId(model.getProductId());
		error.setSalesCd(model.getSalesCd());
		error.setIdNo(model.getIdNo());
		error.setMessage(message);
		errors.add(error);
	}

	@Override
	public List<TraInfoModel> searchDocumentByIdNo(String idNo) {
		List<TraInfoModel> listData = mapper.searchDocumentByIdNo(idNo);
        return listData;
	}

	@Override
	public Boolean updateDocumentByIdNo(TraInfoModel model) {
		Boolean result = mapper.updateDocumentByIdNo(model);
		return result;
	}

	@Override
	public Boolean updateDmtDocument(TraInfoModel model) {
		Boolean result = mapper.updateDmtDocument(model);
		return result;
	}
}
