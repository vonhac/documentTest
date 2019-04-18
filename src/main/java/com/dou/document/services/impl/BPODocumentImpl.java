package com.dou.document.services.impl;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.BPODocumentMapper;
import com.dou.document.models.Department;
import com.dou.document.models.Document;
import com.dou.document.services.IBPODocumentService;
import com.dou.document.services.IStaffInforBranchSipService;
import com.dou.adm.shared.CommonStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BPODocumentImpl implements IBPODocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IStaffInforBranchSipService.class);

    @Autowired
    private BPODocumentMapper BPOdocumentmapper;

    @Override
    public List<Document> getData(Document document) {
        return BPOdocumentmapper.getdata(document);
    }

    @Override
    public List<Department> getDepartment() {
        return BPOdocumentmapper.getdepartment();
    }

    @Override
    public ResponseObject BPOCheckDocument(String documentId) {
        ResponseObject responseObject = new ResponseObject<>();
        try {
            int result;
            int dataIsCheck = selectDataIsCheck(documentId);
            int check = dataIsCheck == 1 ? 0 : 1;

            result =  BPOdocumentmapper.BPOCheckDocument(documentId, check);
            if (result > 0) {
                responseObject.setMessage(CommonStrings.RESP_MSG_UPDATE_SUCCESS);
            }else{
                responseObject.setMessage(CommonStrings.RESP_MSG_UPDATE_FAIL);
            }
        }
        catch (Exception e){
            responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
            LOGGER.error("Error when call Update Information BPO Check in BPO Document Service class: .", e);
        }
        return responseObject;
    }

    @Override
    public int selectDataIsCheck(String documentId) {
        return BPOdocumentmapper.selectDataIsCheck(documentId);
    }
}
