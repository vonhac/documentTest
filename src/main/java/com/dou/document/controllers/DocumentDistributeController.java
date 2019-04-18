package com.dou.document.controllers;

import java.util.List;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;

import com.dou.document.models.TraInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dou.document.models.CsrModel;
import com.dou.document.models.DistributeModel;
import com.dou.document.models.DmtDocumentSearchModel;
import com.dou.document.services.IDocumentDistributeService;
import com.dou.adm.shared.CommonStrings;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@RestController
@RequestMapping("/api/distribute-document")
public class DocumentDistributeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDistributeController.class);
	
	@Autowired
	IDocumentDistributeService iDocumentDistributeService;
	
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseObject<List<TraInfoModel>> searchDmtDocument(@RequestBody DmtDocumentSearchModel model, @RequestAttribute (JWT_USER) JwtUser user){
		ResponseObject<List<TraInfoModel>> responseObject = new ResponseObject<List<TraInfoModel>>();
        try {
            List<TraInfoModel> listData =  iDocumentDistributeService.searchDistributeDocument(model.getStatusCd(), model.getDateParam(), user.getProfiles().getBranchId());
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
    
    @RequestMapping(value = "/receivedDocument", method = RequestMethod.POST)
    public ResponseEntity<?> receivedDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody List<TraInfoModel> model){
    	ResponseObject response = new ResponseObject();
        try {
        	List<TraInfoModel> result = iDocumentDistributeService.receivedDocument(user, model);
    		response.setSuccess(true);
            response.setMessage("Registered successful!");
            response.setData(result);
		}catch (Exception e){
			response.setSuccess(false);
			response.setFailMessage("Data Status : Error "+e.getMessage());
	        LOGGER.error("Error when call checkInvalidDocument in DmtDocumentController class: .", e);
	    }
	    
	    return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/getCsrList", method = RequestMethod.POST)
    public ResponseEntity<?> getCsrList(@RequestAttribute (JWT_USER) JwtUser user){
    	ResponseObject response = new ResponseObject();
        try {
            String branch = user.getProfiles().getBranchId();
            String table = user.getTargetProfileTable();
        	List<CsrModel> result = iDocumentDistributeService.getCsrList(branch,table);
    		response.setSuccess(true);
            response.setMessage("Registered successful!");
            response.setData(result);
		}catch (Exception e){
			response.setSuccess(false);
			response.setFailMessage("Data Status : Error "+e.getMessage());
	        LOGGER.error("Error when call checkInvalidDocument in DmtDocumentController class: .", e);
	    }
	    
	    return ResponseEntity.ok(response);
    }
    
	@RequestMapping(value = "/distribute", method = RequestMethod.POST)
    public ResponseEntity<?> distributeDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody DistributeModel model){
        ResponseObject response = new ResponseObject();
        try {
			iDocumentDistributeService.distributeDocument(user, model);
            response.setSuccess(true);
            response.setMessage("Distribute successful!");
            response.setData(model);
        }catch (Exception e){
			response.setSuccess(false);
			response.setFailMessage("Data Status : Error "+e.getMessage());
	        LOGGER.error("Error when call distributeDocument in DocumentDistributeController class: .", e);
	    }

        return ResponseEntity.ok(response);
    }
}
