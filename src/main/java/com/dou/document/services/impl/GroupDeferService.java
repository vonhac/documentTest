/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */

package com.dou.document.services.impl;

import com.dou.adm.shared.CommonStrings;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.IDMTGroupDeferMapper;
import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTGroupDeferModel;
import com.dou.document.services.IDMTGroupDeferService;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class GroupDeferService implements IDMTGroupDeferService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GroupDeferService.class);

  @Autowired
  private IDMTGroupDeferMapper mapper;


  @Override
  @Transactional
  public ResponseObject loadGroupDeferDefault() {
    ResponseObject<List<DMTGroupDeferModel>> responseObject = new ResponseObject<>();
    try{
      List<DMTGroupDeferModel> loadDataDefault = mapper.loadDataDefault(null);
      responseObject.setData(loadDataDefault);
      responseObject.setMessage(CommonStrings.RESP_MSG_SUCCESS);
    }catch(Exception e){
      LOGGER.error("Error when call listData  in DMTGroupDeferModel class: .", e);
      responseObject.setMessage(CommonStrings.RESP_MSG_SERVER_ERROR);
    }
    return responseObject;
  }


  @Override
  @Transactional
  public ResponseObject addDeferGroup(DMTGroupDeferModel dmtGroupDeferModel) {

    ResponseObject responseObject = new ResponseObject();
    try{
      if (StringUtils.isEmpty(dmtGroupDeferModel.getGroupCd())){
        return ResponseObject.failResponse("Please input valid sale code!");
      }
      List<DMTGroupDeferModel> list = mapper.loadDataDefault(dmtGroupDeferModel.getGroupCd());
      if(list.size() > 0){
        return ResponseObject.failResponse("Duplicate Group Code");
      }
      boolean result = mapper.addDataDeferGroup(dmtGroupDeferModel);
      if(result){
        responseObject.setData(result);
      }else{
        return ResponseObject.failResponse("Add Group Code Error");
      }

    }catch(Exception e){
      responseObject.setSuccess(false);
      responseObject.setFailMessage("Error Add Data GroupCode");
      LOGGER.error("Error when call add Defer Group function  in DMTGroupDeferModel class: .", e);
    }
    return responseObject;
  }

  @Override
  @Transactional
  public ResponseObject  editDeferGroup(DMTGroupDeferModel dmtGroupDeferModel) {
    ResponseObject responseObject = new ResponseObject();
    try {
        List<DMTGroupDeferModel> checkList = mapper.loadDataDefault(dmtGroupDeferModel.getGroupCd());
        if(checkList.size() > 0){
          for(int i =0; i< checkList.size(); i++){
            if(checkList.get(i).getGroupCd() == dmtGroupDeferModel.getGroupCd()){
              return ResponseObject.failResponse("Duplicate Group code");
            }
          }
        }
        boolean result = mapper.editDataDeferGroup(dmtGroupDeferModel);
        if(result){
          responseObject.setMessage("Update Successful");
          return responseObject;
        }
    }catch(Exception e){
      LOGGER.error("Error when call edit Defer Group function  in DMTGroupDeferModel class: .", e);
      return ResponseObject.failResponse("Update Failed");
    }
    return ResponseObject.failResponse("Update Failed");
  }

  @Override
  @Transactional
  public ResponseObject delDeferGroup(DMTGroupDeferModel dmtGroupDeferModel) {
    ResponseObject responseObject = new ResponseObject();
    try{
      boolean delGroup = mapper.delDataDeferGroup(dmtGroupDeferModel.getGroupCd());
      boolean delDefer = mapper.delDataDeferDetail(null, dmtGroupDeferModel.getGroupCd());
       if(delGroup  && delDefer ){
         responseObject.setMessage("Deleted Successful");
         return responseObject;
       }
    }catch(Exception e){
      LOGGER.error("Error when call deleted data in Group Defer: .", e);
      return ResponseObject.failResponse("Deleted Failed");
    }
    return ResponseObject.failResponse("Deleted Failed");
  }



  ///////////// Defer Detail//////////////////////////

  @Override
  @Transactional
  public ResponseObject loadDeferDetailDefault() {
    ResponseObject<List<DMTGroupDeferModel>> responseObject = new ResponseObject<>();
    try{
      List<DMTGroupDeferModel> data = mapper.loadDeferDetailDefault();
      if(data.size() > 0){
        responseObject.setData(data);
      }else{
        responseObject.setSuccess(false);
      }
    }catch(Exception e)
    {
      LOGGER.error("Error when call loadDeferDetailDefault  in DMTDeferDetailModel class: .", e);
    }
    return responseObject;
  }

  @Override
  public ResponseObject searchDeferDetail(String groupCd) {
    ResponseObject responseObject = new ResponseObject();
    try{
      List<DMTDeferDetailModel> listDeferHistory = mapper.searchDeferDetail(groupCd,null);
      if(listDeferHistory.size() > 0){
        responseObject.setData(listDeferHistory);
        return responseObject;
      }
    }catch(Exception e){
      LOGGER.error("Error when call function search  in DMTHistoryStatusModel class: .", e);
      return ResponseObject.failResponse("Error found data");
    }
    return ResponseObject.failResponse("Error found data");

  }

  @Override
  @Transactional
  public ResponseObject<List<DMTDeferDetailModel>> searchDeferDetailByGroupCd(DMTDeferDetailModel dmtDeferDetailModel) {
    ResponseObject responseObject = new ResponseObject<>();
    try{
      List<DMTDeferDetailModel> data = mapper.searchDeferDetailByGroupCd(dmtDeferDetailModel);
      if(data.size() > 0){
        responseObject.setData(data);
      }
    }catch(Exception e){
      LOGGER.error("Error when call search data defer detail  in DMTDeferDetailModel class: .", e);
      return ResponseObject.failResponse("Error found data");
    }
    return responseObject;
  }


  @Override
  public ResponseObject addDeferDetail(DMTDeferDetailModel dmtDeferDetailModel) {
    ResponseObject responseObject = new ResponseObject();
    try {
      List<DMTDeferDetailModel> detailModels = mapper.searchDeferDetail(dmtDeferDetailModel.getGroupCd(), dmtDeferDetailModel.getDeferCd());
      if(detailModels.size() > 0){
        return ResponseObject.failResponse("DeferCode Invalid");
      }
      boolean result = mapper.addDataDeferDetail(dmtDeferDetailModel);
      if(result){
        responseObject.setMessage("Save Successful");
        responseObject.setSuccess(true);
        return responseObject;
      }
    }catch(Exception e){
      LOGGER.error("Error when call add data defer detail  in DMTDeferDetailModel class: .", e);
      return ResponseObject.failResponse("Save Failed");
    }
    return ResponseObject.failResponse("Save Failed");
  }

  @Override
  public ResponseObject editDeferDetail(DMTDeferDetailModel dmtDeferDetailModel) {
    ResponseObject responseObject = new ResponseObject();
    try {
      List<DMTDeferDetailModel> checkList= mapper.searchDeferDetail(dmtDeferDetailModel.getGroupCd(),dmtDeferDetailModel.getDeferCd());
      if(checkList.size()> 0){
        return ResponseObject.failResponse("Edit Failed");
      }
      boolean result = mapper.editDataDeferDetail(dmtDeferDetailModel);
    }catch(Exception e){
      LOGGER.error("Error when call edit data defer detail in DMTDeferDetailModel class: .", e);
      return ResponseObject.failResponse("Edit Failed");
    }
    return responseObject;
  }

  @Override
  public ResponseObject delDeferDetail(DMTDeferDetailModel dmtDeferDetailModel) {
    ResponseObject responseObject = new ResponseObject();
    boolean result;
    try {
      result = mapper.delDataDeferDetail(dmtDeferDetailModel.getDeferCd(),null);
      if(result){
        responseObject.setMessage("Deleted Successful");
        return responseObject;
      }
    }catch(Exception e){
      LOGGER.error("Error when call delete data defer detail  in DMTDeferDetailModel class: .", e);
      return ResponseObject.failResponse("Deleted Failed");
    }
    return ResponseObject.failResponse("Deleted Failed");
  }
}