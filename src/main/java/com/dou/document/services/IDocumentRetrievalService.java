package com.dou.document.services;

import java.util.List;

import com.dou.document.models.*;
import org.apache.ibatis.annotations.Param;

import com.dou.adm.security.JwtUser;

public interface IDocumentRetrievalService {
	List<TraInfoModel> searchRetrievalDocument(String status, String distributeDate, String distributeCsr);
	public List<TraInfoModel> retrievalDocument(JwtUser user, List<TraInfoModel> model, String csrId);
	public List<DocumentStatusModel> getStatusModel();
}
