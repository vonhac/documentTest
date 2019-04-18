package com.dou.document.services.impl;

import com.dou.adm.security.JwtUser;
import com.dou.document.mappers.DocumentRetrievalMapper;
import com.dou.document.models.TraInfoModel;
import com.dou.document.services.IDocumentRetrievalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dou.document.models.DocumentStatusModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DocumentRetrievalService implements IDocumentRetrievalService{
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRetrievalService.class);
    
	@Autowired
	DocumentRetrievalMapper mapper;
	
	@Autowired
	DmtDocumentService docmtService;

	@Override
	public List<TraInfoModel> searchRetrievalDocument(String status,
													  String distributeDate, String distributeCsr) {
		List<TraInfoModel> listData = new ArrayList<TraInfoModel>();
		listData =  mapper.searchRetrievalDocument(status, distributeDate, distributeCsr);
        return listData;
	}

	@Override
	public List<TraInfoModel> retrievalDocument(JwtUser user,
			List<TraInfoModel> models, String csrId) {

		String distributed = "";
		for (TraInfoModel model : models) {
			distributed = model.getDistributeCsr();
			model.setDistributeCsr(csrId);
			model.setRetrievalCsr(distributed);
//			model.setRetrievalCsr(csrId);
			model.setStatusDateCurrent(new Date());
			docmtService.updateDmtDocument(model);
		}
		return models;
	}

	@Override
	public List<DocumentStatusModel> getStatusModel() {
		List<DocumentStatusModel> listData = mapper.getStatusModel();
        return listData;
	}

}
