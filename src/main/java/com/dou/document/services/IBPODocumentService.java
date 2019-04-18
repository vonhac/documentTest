package com.dou.document.services;


import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.Department;
import com.dou.document.models.Document;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBPODocumentService {
    List<Document> getData(Document document);

    List<Department> getDepartment();

    ResponseObject BPOCheckDocument(String documentId);

    int selectDataIsCheck(String documentId);
}
