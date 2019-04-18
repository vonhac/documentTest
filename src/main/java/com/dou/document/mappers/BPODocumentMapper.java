package com.dou.document.mappers;

import com.dou.document.models.Department;
import com.dou.document.models.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BPODocumentMapper {
    List getdata(Document document);
    List getdepartment();
    int BPOCheckDocument(@Param("documentId") String documentId,@Param("check") int check );
    int selectDataIsCheck(@Param("documentId") String documentId);
}
