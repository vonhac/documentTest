/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.mappers;

import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTGroupDeferModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IDMTGroupDeferMapper {
    List<DMTGroupDeferModel> loadDataDefault(@Param( "groupCd") String groupCd);
    boolean addDataDeferGroup(DMTGroupDeferModel dmtGroupDeferModel);
    boolean editDataDeferGroup(DMTGroupDeferModel dmtGroupDeferModel);
    boolean delDataDeferGroup(@Param("groupCd") String groupCd);


    List<DMTGroupDeferModel> loadDeferDetailDefault();
    List<DMTDeferDetailModel> searchDeferDetailByGroupCd(DMTDeferDetailModel dmtDeferDetailModel);
    List<DMTDeferDetailModel> searchDeferDetail(@Param("groupCd") String groupCd, @Param("deferCd") String deferCd);

    boolean addDataDeferDetail(DMTDeferDetailModel dmtDeferDetailModel);
    boolean editDataDeferDetail(DMTDeferDetailModel dmtDeferDetailModel);
    boolean delDataDeferDetail(@Param("deferCd")String deferCd, @Param("groupCd") String groupCd);
}