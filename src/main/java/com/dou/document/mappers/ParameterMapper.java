/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.mappers;

import com.dou.document.models.ParameterModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParameterMapper {

    List<ParameterModel> getAllParams   ();

    ParameterModel getParam             (@Param("paramCd") String name);

    int setParam                        (@Param("paramCd") String name, @Param("paramVal")  String value);

}