package com.dou.document.controllers;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.*;
import com.dou.document.services.IDmtDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@RestController
@RequestMapping("/api/import-document")
public class DmtDocumentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DmtDocumentController.class);
	
	@Autowired
    IDmtDocumentService iDmtDocumentService;

    @PostMapping("/search")
    public ResponseObject searchDmtDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user,@RequestBody DmtDocumentSearchModel model){
        return iDmtDocumentService.searchDmtDocument(user, model);
    }
    
    @PostMapping("/add")
    public ResponseObject addDmtDocumentModel(@RequestAttribute(value = JWT_USER, required = false) JwtUser user,
                                              @RequestBody TraInfoModel model){
        return iDmtDocumentService.addDmtDocumentModel(user, model) ;
    }
	
    @PostMapping("/update")
    public ResponseObject updateDmtDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user,
                                            @RequestBody TraInfoModel model){
        return iDmtDocumentService.updateDmtDocument(user,model);
    }
	
    @PostMapping("/getlistcomboboxmodel")
    public ResponseObject getListCombobox(@RequestAttribute(JWT_USER) JwtUser user ){
        String department = user.getProfiles().getDepartmentId();
        return iDmtDocumentService.getcomboBox(department);
    }
	
	@PostMapping("/{document_cd}/delete")
    public  ResponseObject deleteDocument(@PathVariable("document_cd")int documentCd) {
        return  iDmtDocumentService.deleteDmtDocument(documentCd);
    }

    @PostMapping("{sales_cd}/checksalecode")
    public  ResponseObject checkSaleCode(@RequestAttribute(value = JWT_USER, required = false) JwtUser user,@PathVariable("sales_cd") String salesCd) {
        return  iDmtDocumentService.checkSaleAccount(salesCd,user.getProfiles().getDepartmentId());
    }

    @PostMapping("/importDocument")
    public ResponseObject ImportEmployee(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody List<TraInfoModel> model){
	    return iDmtDocumentService.importDmtDocuments(user,model);
    }
    
    @PostMapping("/checkInvalidDocument")
    public ResponseObject checkInvalidDocument(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody List<TraInfoModel> model){
	    return iDmtDocumentService.checkInvalidDocument(user,model);
    }

    @PostMapping("/sendCSR")
    public ResponseObject sendCSR(@RequestAttribute(value = JWT_USER, required = false) JwtUser user, @RequestBody SendCSRReqObject reqObject){
	    return iDmtDocumentService.sendCsrDmtDocuments(user, reqObject.getModels(), reqObject.getSendToPlace());
    }
    
	@PostMapping("/getExternalResult/{id}")
    public  ResponseObject getExternalResult(@PathVariable("id")String id) {
        ResponseObject responseObject  = new ResponseObject();
        int result = 0;
        try {
        	ResultDrsModel model = new ResultDrsModel();
        	model.setIdOrPhone(id);
            iDmtDocumentService.getResultFromExternalSystem(model);
            if(result > 0){
                responseObject.setMessage( CommonStrings.RESP_MSG_DELETE_SUCCESS);
            }
        }catch (Exception e){
            responseObject.setFailMessage(CommonStrings.RESP_MSG_DELETE_FAIL);
            LOGGER.error("Error when call deleteDocumentModel in DmtDocumentController class: .", e);
        }
        return  responseObject;
    }
    
}
