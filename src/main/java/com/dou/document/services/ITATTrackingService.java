package com.dou.document.services;

import com.dou.document.models.DocumentSts;
import com.dou.document.models.TATTracking;

import java.util.List;
import java.util.Map;

public interface ITATTrackingService {
    Map getDataDB2(Map listParam);
    List<DocumentSts> getDocumentSts();

}
