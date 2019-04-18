package com.dou.document.services;

import java.util.List;

import com.dou.adm.security.JwtUser;
import com.dou.document.models.CsrModel;
import com.dou.document.models.DistributeModel;
import com.dou.document.models.TraInfoModel;

public interface IDocumentDistributeService {

	List<TraInfoModel> searchDistributeDocument(String status, String dateParam, String branch);

	List<TraInfoModel> receivedDocument(JwtUser user, List<TraInfoModel> model);

	List<CsrModel> getCsrList(String branch, String table);
	
	DistributeModel distributeDocument(JwtUser user, DistributeModel model);
	
}
