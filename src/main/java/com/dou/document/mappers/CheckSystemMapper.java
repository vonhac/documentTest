package com.dou.document.mappers;

import com.dou.document.models.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckSystemMapper {
    void getResultFromExternalSystem(ResultDrsModel customerStatus);
}
