/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.mappers;

import com.dou.document.models.*;
import com.dou.document.shared.DocumentStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ITraInfoMapper {

    List<ChannelCodeModel> loadChannelCodeDefault();

    List<ChannelCodeModel> loadStatusCodeDefault(@Param("acceptStatus") List<DocumentStatus> acceptStatus);

    List<TraInfoModel> searchMatching(DocumentFilterReqObject conditions);

    List<TraInfoModel> searchMatchingByPass(DocumentFilterReqObject conditions);

    List<TraInfoModel> checkDocumentPass(@Param("idNo")String idNo);

    List<TraInfoModel> loadDocuments(@Param("documentCd") String documentCd);

    List<TraInfoModel> checkDocumentByIdNo(@Param("idNo") String idNo);

    List<DMTHistoryStatusModel> getHistoryStatus(@Param("documentNo") String documentNo);

    List<DMTDeferDetailModel> loadDeferHistoryByIdNo (@Param("documentNo") String documentNo);

    boolean addAppID(@Param("documentCd")String  documentCd,@Param("appID")String  appID);

    int addDefersForDocument(List list);

    int updateDefersForDocument(DMTDeferDetailModel list);

    int addDefersStatusHistory (DMTHistoryStatusModel historyStatusModel);

    int addStatusHistory (DMTHistoryStatusModel historyStatusModel);
}