package com.dou.document.mappers;

import java.util.List;

import com.dou.document.models.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface DocumentDistributeMapper {

	List<TraInfoModel> searchDistributeDocument(@Param("status")String status, @Param("dateParam")String dateParam, @Param("id")String id, @Param("checkDuplicate")boolean checkDuplicate, @Param("branch")String branch);
	
	List<CsrModel> getCsrList(@Param("branch")String branch,@Param("table") String table);

}
