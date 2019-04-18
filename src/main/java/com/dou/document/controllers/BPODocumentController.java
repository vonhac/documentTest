package com.dou.document.controllers;

import com.dou.adm.security.JwtAuthFilter;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.Department;
import com.dou.document.models.Document;
import com.dou.document.services.IBPODocumentService;
import com.dou.document.services.IStaffInforBranchSipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/BPO-document")
public class BPODocumentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IStaffInforBranchSipService.class);

    @Autowired
    private IBPODocumentService service;

    @GetMapping
    public ResponseObject getAllDataBPOCheckDocument(@RequestParam (value = "sale_channel", required = false) String saleChannel,
                                                     @RequestParam (value = "sale_cd", required = false) String saleCd,
                                                     @RequestParam (value = "distribute_dt", required = false) Date distributeDt,
                                                     @RequestParam (value = "bpo_check", required = false) String bpoCheck,
                                                     @RequestAttribute (value = JwtAuthFilter.JWT_USER) JwtUser user) {
        ResponseObject<List<Document>> responseObject = new ResponseObject<>();
        try {
            saleChannel = saleChannel == "All"? null: saleChannel;
            saleCd = saleCd == ""? null:saleCd;
            bpoCheck = bpoCheck == "All" ? null: bpoCheck;
            if("Yes".equals(bpoCheck)){
                bpoCheck = "1";
            }
            if("No".equals(bpoCheck)){
                bpoCheck = "0";
            }

            if("All".equals(bpoCheck)){
                bpoCheck = null;
            }
            Document document = new Document();
            document.setSaleChannel(saleChannel = saleChannel == "" ? null : saleChannel);
            document.setSaleCd(saleCd = saleCd == "" ? null : saleCd);
            document.setBpoCheck(bpoCheck = bpoCheck == "" ? null : bpoCheck);
            document.setDistributeDt(distributeDt);
            document.setDistributedCsr(user.getUsername());
            document.setRetrivalCsr(user.getUsername());
            List<Document> data = service.getData(document);
            responseObject.setData(data);
        }catch (Exception e){
            responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
            LOGGER.error("Error when call Get Data Information BPO Check in BPO Document Controller class: .", e);
        }
        return responseObject;
    }
    @GetMapping("/department-list")
    public ResponseObject getDepartmentList() {
        ResponseObject<List<Department>> responseObject = new ResponseObject<List<Department>>();
        try {
            List<Department> data = service.getDepartment();
            responseObject.setData(data);
        }catch (Exception e){
            responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
            LOGGER.error("Error when call Get Data Sale Channel Controller class: .", e);
        }
        return responseObject;
    }

    @PutMapping("/BPOCheckDocument")
    public ResponseObject BPOCheckDocument(@RequestBody String documentId) {
        return service.BPOCheckDocument(documentId);
    }
}
