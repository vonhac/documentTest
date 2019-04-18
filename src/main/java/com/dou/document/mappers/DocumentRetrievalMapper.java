package com.dou.document.mappers;

import java.util.List;

import com.dou.document.models.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface DocumentRetrievalMapper {
	List<TraInfoModel> searchRetrievalDocument(@Param("status")String status, @Param("distributeDate")String distributeDate, @Param("distributeCsr")String distributeCsr);
	List<CsrModel> getCsrList();
	public List<DocumentStatusModel> getStatusModel();
}
