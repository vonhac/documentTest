package com.dou.document.controllers;

import java.util.List;

import com.dou.document.models.TraInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.CsrModel;
import com.dou.document.models.DmtDocumentSearchModel;
import com.dou.document.models.DocumentStatusModel;
import com.dou.document.services.IDocumentDistributeService;
import com.dou.document.services.IDocumentRetrievalService;

import org.springframework.web.bind.annotation.*;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@RestController
@RequestMapping("/api/retrieval-document")
public class DocumentRetrievalController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRetrievalController.class);
	
	@Autowired
	IDocumentRetrievalService iDocumentRetrievalService;
	@Autowired
	IDocumentDistributeService iDocumentDistributeService;
	
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseObject<List<TraInfoModel>> searchDmtDocument(@RequestBody DmtDocumentSearchModel model){
		ResponseObject<List<TraInfoModel>> responseObject = new ResponseObject<List<TraInfoModel>>();
        try {
            List<TraInfoModel> listData =  iDocumentRetrievalService.searchRetrievalDocument(model. getStatusCd(), model.getDistributeDate(), model.getDistributeCsr());
            responseObject.setData(listData);
            responseObject.setSuccess(true);
            responseObject.setMessage(CommonStrings.RESP_MSG_SUCCESS);
        }catch (Exception e){
        	responseObject.setSuccess(false);
            responseObject.setFailMessage("Data Status : Error "+e.getMessage());
            LOGGER.error("Error when call searchDmtDocument in DmtDocumentController class: .", e);
        }
        return responseObject;
    }
    
	@RequestMapping(value = "/getlistcomboboxmodel", method = RequestMethod.POST)
    public ResponseEntity<?> getListCombobox(@RequestAttribute (JWT_USER) JwtUser user){
        ResponseObject response = new ResponseObject();
        try {
            String branch = user.getProfiles().getBranchId();
            DmtDocumentSearchModel model = new DmtDocumentSearchModel();
            String table = user.getTargetProfileTable();
            List<CsrModel> listCsr = iDocumentDistributeService.getCsrList(branch, table);
            List<DocumentStatusModel> listStatus = iDocumentRetrievalService.getStatusModel();
            model.setListCsr(listCsr);
            model.setListStatus(listStatus);
            response.setSuccess(true);
            response.setMessage("Registered successful!");
            response.setData(model);
		}catch (Exception e){
			response.setSuccess(false);
			response.setFailMessage("Data Status : Error "+e.getMessage());
	        LOGGER.error("Error when call addDmtDocumentModel in DmtDocumentController class: .", e);
	    }
        
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/retrievalDocument/{csrId}", method = RequestMethod.POST)
    public ResponseEntity<?> retrievalDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody List<TraInfoModel> model, @PathVariable("csrId")String id){
    	ResponseObject response = new ResponseObject();
        try {
        	List<TraInfoModel> result = iDocumentRetrievalService.retrievalDocument(user, model, id);
        	response.setSuccess(true);
            response.setMessage("Registered successful!");
            response.setData(result);
		}catch (Exception e){
			response.setSuccess(false);
			response.setFailMessage("Data Status : Error "+e.getMessage());
	        LOGGER.error("Error when call ImportEmployee in DmtDocumentController class: .", e);
	    }
	    
	    return ResponseEntity.ok(response);
    }
}
