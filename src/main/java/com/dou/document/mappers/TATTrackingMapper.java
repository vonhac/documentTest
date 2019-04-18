package com.dou.document.mappers;

import com.dou.document.models.DocumentSts;
import com.dou.document.models.TATTracking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TATTrackingMapper {
    List<TATTracking> getDataF1(Map param);
    List<TATTracking> getDataDB2(Map listParams);
    List<TATTracking> getDataHome(Map param);
    List<DocumentSts> getDocumentSts();
}
