package com.dou.document.controllers;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.DocumentSts;
import com.dou.document.services.ITATTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/TAT-Tracking")
public class TATTrackingController {
    @Autowired
    private ITATTrackingService service;

    @GetMapping
    public ResponseObject getDataF1(@RequestParam (value = "id_no", required = false) String idNo,
                                    @RequestParam (value = "document_sts", required = false) String documentSts,
                                    @RequestParam (value = "base_on_step_data", required = false) String baseOnStepData,
                                    @RequestParam (value = "from_dt", required = false) String fromDt,
                                    @RequestParam (value = "to_dt", required = false) String toDt) {
        ResponseObject response = new ResponseObject();
        Map listParam = new HashMap<>();

        listParam.put("idNo",  idNo);
        listParam.put("documentSts",documentSts);
        listParam.put("baseOnStepData",  baseOnStepData);
        listParam.put("fromDt",fromDt = fromDt == null ? "" : fromDt);
        listParam.put("toDt",toDt = toDt == null ? "" : toDt);

        Map data = service.getDataDB2(listParam);

        response.setData(data);
        return response;
    }
    @GetMapping ("/Document-Sts")
    public ResponseObject getDocumentSts(){
        ResponseObject<List<DocumentSts>> response = new ResponseObject<List<DocumentSts>>();
        List<DocumentSts> listDocumentSts = service.getDocumentSts();
        response.setData(listDocumentSts);
        return response;
    }

}
