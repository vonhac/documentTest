package com.dou.document.services.impl;

import com.dou.adm.security.JwtUser;
import com.dou.document.mappers.DocumentDistributeMapper;
import com.dou.document.mappers.ParameterMapper;
import com.dou.document.models.*;
import com.dou.document.services.IDocumentDistributeService;
import com.dou.document.shared.DMTConfigurationCode;
import com.dou.document.shared.DocumentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DocumentDistributeService implements IDocumentDistributeService{
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDistributeService.class);

	private static List<Integer> VALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{0, 34, 35});
	private static List<Integer> INVALID_DOCUMENT_RESULTS = Arrays.asList(new Integer[]{2, 4, 5, 31, 32});

	@Autowired
	private DmtDocumentService docmtService;

	@Autowired
	private DocumentDistributeMapper mapper;

	@Autowired
	private ParameterMapper parameterMapper;


	@Override
	@Transactional
	public List<TraInfoModel> searchDistributeDocument(String status, String dateParam, String branch) {
		List<TraInfoModel> listData =  mapper.searchDistributeDocument(status, dateParam, null, false, branch);
		return listData;
	}

	@Override
	@Transactional
	public List<TraInfoModel> receivedDocument(JwtUser user,
											   List<TraInfoModel> models) {
		ParameterModel paramDeadlineModel = parameterMapper.getParam(DMTConfigurationCode.PARAM_DEADLINE);

		for (TraInfoModel model : models) {
			String status = model.getStatus();
			DocumentDetailStatusModel statusModel = new DocumentDetailStatusModel();
			DocumentStatus prevStatus = DocumentStatus.fromString(status);
			boolean changeStatus = false;

			if (status == null || status.equals("")) {
				changeStatus = true;
				statusModel = docmtService.createStatusObject(user, model, DocumentStatus.CANCEL_STEP_RECEIVED, "Empty status");
				model.setInvalidDescription("Empty status");
				model.setSTATUSENUM(DocumentStatus.CANCEL_STEP_RECEIVED);
			}

			if (DocumentStatus.SENDING.equals(prevStatus)) {
				changeStatus = true;
				model.setReceivedFlag("receiveBy");
				statusModel = docmtService.createStatusObject(user, model, DocumentStatus.RECEIVED, "");
				model.setSTATUSENUM(DocumentStatus.RECEIVED);
			}

			if (changeStatus) {
				model.setStatusDateCurrent(new Date());
				if (DocumentStatus.SENDING.equals(prevStatus) && paramDeadlineModel != null) {
					String valueDay = paramDeadlineModel.getValue();
					boolean isInteger = checkValidInteger(valueDay);
					if (isInteger) {
						int days = Integer.parseInt(valueDay);
						Date deadline = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(deadline);
						c.add(Calendar.DATE, days);
						deadline = c.getTime();
						model.setDeadline(deadline);
					}
				}
				Boolean updateDocmt = docmtService.updateDmtDocument(model);
				Boolean updateResult = docmtService.insertDmtDocumentStatus(statusModel);
				if (!updateResult) {
					model.setSTATUSENUM(prevStatus);
					model.setInvalidDescription("");
				}
				else {
					List<TraInfoModel> listData = mapper.searchDistributeDocument("", "", model.getIdNo(), true,"");
					for (TraInfoModel item : listData) {
						DocumentDetailStatusModel cancelStatus = docmtService.createStatusObject(user, item, DocumentStatus.CANCEL_STEP_RECEIVED, "Be Duplicated At Channel: "+user.getProfiles().getDepartmentNm());
						docmtService.insertDmtDocumentStatus(cancelStatus);
					}
				}
			}
		}

		return models;
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
	public List<CsrModel> getCsrList(String branch, String table) {
		List<CsrModel> listData =  mapper.getCsrList(branch, table);
		return listData;
	}

	@Override
	public DistributeModel distributeDocument(JwtUser user, DistributeModel model) {
		List<CsrModel> listCsr = model.getListCsr();
		List<TraInfoModel> listDocument = model.getListDocument();
		int totalDistribute = model.getTotalDistribute();
		int totalReceived = model.getTotalReceived();
		if (totalDistribute == totalReceived) {
			for (CsrModel csr : listCsr) {
				List<TraInfoModel> listCsrDocument = csr.getListDocument();
				int count = csr.getCount();
				for (int i = 0; i < count; i++) {
					Iterator<TraInfoModel> iterateDoc = listDocument.iterator();
					if (iterateDoc.hasNext()) {
						TraInfoModel doc = iterateDoc.next();
						listCsrDocument.add(doc);
						iterateDoc.remove();
					}
				}
			}
			for (CsrModel csr : listCsr) {
				List<TraInfoModel> listCsrDocument = csr.getListDocument();
				for (TraInfoModel doc : listCsrDocument) {
					doc.setDistributeCsr(csr.getAccountId());
					doc.setModifiedDate(new Date());
					doc.setModifiedBy(user.getUsername());
					doc.setSTATUSENUM(DocumentStatus.DISTRIBUTED);
					doc.setStatusDateCurrent(new Date());
					docmtService.updateDmtDocument(doc);
					DocumentDetailStatusModel distributeStatus = docmtService.createStatusObject(user, doc, DocumentStatus.DISTRIBUTED, "");
					docmtService.insertDmtDocumentStatus(distributeStatus);
				}
			}
		}
		else {
			autoDistributed (listCsr,listDocument, user);
		}


		return model;
	}

	public void sortListCsr(List<CsrModel> listCsr) {
		Collections.sort(listCsr, (CsrModel csr1, CsrModel csr2) -> {
			int docNo1 = csr1.getDocNo();
			int docNo2 = csr2.getDocNo();
			if ( docNo1 == docNo2 ) {
				return 0;
			}
			else if ( docNo1 > docNo2 ) {
				return 1;
			}
			return -1;
		});
	}

	// Start Auto Distributed
	public static int TotaldocumentCSR1 = 0;
	public static int TotaldocumentCSR2 = 0;
	public static int TotaldocumentCSR3 = 0;
	public static int AmountDocCSRLack1 = 0;
	public static int AmountDocCSRLack2 = 0;

	public void autoDistributed (List<CsrModel> listCsr, List<TraInfoModel> listDocument, JwtUser user){
		Iterator<TraInfoModel> iterateDoc = listDocument.iterator();
		int totalDocumentSize =  listDocument.size();
		for(int y = 0; y < totalDocumentSize; y++){
			sortListCsr(listCsr);
			for (int i = 0; i < listCsr.size(); i++){
				List<TraInfoModel> listCsrDocument = listCsr.get(i).getListDocument();
				TotaldocumentCSR1 = listCsr.get(i).getDocNo();
				if(listCsr.size() > 2){
					if(i <= 1){
						TotaldocumentCSR2 = listCsr.get(i+1).getDocNo();
					}
					if(i < 1){
						TotaldocumentCSR3 = listCsr.get(i+2).getDocNo();
					}
				}else if(listCsr.size() == 2){
					if(i < 1){
						TotaldocumentCSR2 = listCsr.get(i+1).getDocNo();
					}

				}
				if(listCsr.size() <= 2){
					if(TotaldocumentCSR1 == TotaldocumentCSR2 || listCsr.size() == 1){
						TotaldocumentCSR1 = TotaldocumentCSR1+1;
						if (iterateDoc.hasNext()) {
							TraInfoModel doc = iterateDoc.next();
							listCsrDocument.add(doc);
							iterateDoc.remove();
						}
						listCsr.get(i).setDocNo(TotaldocumentCSR1);
						break;
					}
				}
				if(TotaldocumentCSR1 == TotaldocumentCSR2 && TotaldocumentCSR2 < TotaldocumentCSR3){
					AmountDocCSRLack2 = AmountDocCSRLack1-TotaldocumentCSR2;
					for (int j = 0; j < AmountDocCSRLack2; j++) {
						TotaldocumentCSR2 += 1;
						if (iterateDoc.hasNext()) {
							TraInfoModel doc = iterateDoc.next();
							listCsrDocument.add(doc);
							iterateDoc.remove();
						}
						listCsr.get(i).setDocNo(TotaldocumentCSR2);
						break;
					}
				}else if(TotaldocumentCSR1  < TotaldocumentCSR2){
					AmountDocCSRLack1 = TotaldocumentCSR2 - TotaldocumentCSR1;
					for (int j = 0; j < AmountDocCSRLack1; j++) {
						TotaldocumentCSR1 += 1;
						if (iterateDoc.hasNext()) {
							TraInfoModel doc = iterateDoc.next();
							listCsrDocument.add(doc);
							iterateDoc.remove();
						}
						listCsr.get(i).setDocNo(TotaldocumentCSR1);
					}
				}else if (TotaldocumentCSR1 == TotaldocumentCSR2 && TotaldocumentCSR2 == TotaldocumentCSR3){
					TotaldocumentCSR1 = TotaldocumentCSR1+1;
					if (iterateDoc.hasNext()) {
						TraInfoModel doc = iterateDoc.next();
						listCsrDocument.add(doc);
						iterateDoc.remove();
					}
					listCsr.get(i).setDocNo(TotaldocumentCSR1);
					break;
				}
			}
			if(listDocument.size() == 0){
				break;
			}
		}
		for (CsrModel csr : listCsr) {
			List<TraInfoModel> listCsrDocument = csr.getListDocument();
			for (TraInfoModel doc : listCsrDocument) {
				doc.setDistributeCsr(csr.getAccountId());
				doc.setModifiedDate(new Date());
				doc.setModifiedBy(user.getUsername());
				doc.setSTATUSENUM(DocumentStatus.DISTRIBUTED);
				doc.setStatusDateCurrent(new Date());
				docmtService.updateDmtDocument(doc);
				DocumentDetailStatusModel distributeStatus = docmtService.createStatusObject(user, doc, DocumentStatus.DISTRIBUTED, "");
				docmtService.insertDmtDocumentStatus(distributeStatus);
			}
		}
	}
	//End Auto Distributed
}
