package com.dou.document.services.impl;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.mappers.StaffInforBranchSipMapper;
import com.dou.document.models.BranchSip;
import com.dou.document.models.StaffInforBranchSip;
import com.dou.document.services.IStaffInforBranchSipService;
import com.dou.adm.shared.CommonStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffInforBranchSipServiceImpl implements IStaffInforBranchSipService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IStaffInforBranchSipService.class);

  @Autowired
  private StaffInforBranchSipMapper mapper;

  @Override
  public ResponseObject getDataStaffBranchSipByFilterCondition(String staffCd) {
    List<StaffInforBranchSip> result = mapper.getDataStaffBranchSipByFilterCondition(staffCd);
    return new ResponseObject<>(result);
  }

  @Override
  public ResponseObject<StaffInforBranchSip> saveAdd(StaffInforBranchSip StaffInforBranchSip){
    ResponseObject<StaffInforBranchSip> responseObject = new ResponseObject<>();
    try {
      List listBranchSipExist;
      int result;
      boolean CSRAccount = checkAccountCSR(StaffInforBranchSip.getStaffCd());
      List<BranchSip> allBranchSip = getBranchSip();
      if(CSRAccount){
        listBranchSipExist = mapper.checkDuplicatedBranSip(StaffInforBranchSip.getBranchSipId());
        if(StaffInforBranchSip.getBranchSipId() == null){
            for(int i = 0; i<allBranchSip.size();i++){
              for(int j = 0; j< listBranchSipExist.size();j++){
                if(allBranchSip.get(i).getBranchId().equals(listBranchSipExist.get(j))){
                  allBranchSip.remove(i);
                }
              }
            }
          for(int i = 0; i<allBranchSip.size();i++){
            StaffInforBranchSip.setBranchSipId(allBranchSip.get(i).getBranchId());
            result = mapper.saveAdd(StaffInforBranchSip);
          }
        }
        else {
          if(listBranchSipExist.size() != 0){
            responseObject.setMessage(CommonStrings.RESP_MSG_DUPLICATED);
          }else{
            result = mapper.saveAdd(StaffInforBranchSip);
            if (result > 0) {
              responseObject.setMessage(CommonStrings.RESP_MSG_SAVE_SUCCESS);
            }else{
              responseObject.setFailMessage(CommonStrings.RESP_MSG_SAVE_FAIL);
            }
          }
        }
      }else {
        responseObject.setFailMessage("This account is not belong CSR Group");
      }
    }catch (Exception e){
      responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
      LOGGER.error("Error when call Save Information Branch Sip in Staff Information Branch Sip in Controller class: .", e);
    }
    return responseObject;
  }

  @Override
  public ResponseObject<StaffInforBranchSip> updateData(StaffInforBranchSip StaffInforBranchSip){
    ResponseObject<StaffInforBranchSip> responseObject = new ResponseObject<>();
    try {
      List listBranchSipExist;
      int result;
      boolean CSRAccount = checkAccountCSR(StaffInforBranchSip.getStaffCd());
      List<BranchSip> allBranchSip = getBranchSip();
      if(CSRAccount) {
        result = mapper.updateData(StaffInforBranchSip);
        if (result > 0) {
          responseObject.setMessage(CommonStrings.RESP_MSG_SAVE_SUCCESS);
        } else {
          responseObject.setMessage(CommonStrings.RESP_MSG_SAVE_FAIL);
        }
      }
      else{
        responseObject.setFailMessage("This account is not belong CSR Group");
      }
    }
    catch (Exception e){
      responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
      LOGGER.error("Error when call Update Information Branch Sip in Staff Information Branch Sip Controller class: .", e);
    }
    return responseObject;
  }


  @Override
  public List checkDuplicatedBranSip(StaffInforBranchSip StaffInforBranchSip) {
    List checkDuplicateBranchSip = mapper.checkDuplicatedBranSip(StaffInforBranchSip.getBranchSipId());
    return  checkDuplicateBranchSip;
  }


  @Override
  public ResponseObject deleteBranchSip(List<String> lstBranch) {
    ResponseObject<List<StaffInforBranchSip>> responseObject = new ResponseObject<>();
    try {
      int result  =  mapper.deletebranchsip(lstBranch);
      if(result > 0){
        responseObject.setMessage(CommonStrings.RESP_MSG_DELETE_SUCCESS);
      }else{
        responseObject.setFailMessage(CommonStrings.RESP_MSG_DELETE_FAIL);
      }

    }catch (Exception e){
      responseObject.setFailMessage("Data Status : Error "+ e.getMessage());
      LOGGER.error("Error when call Delete Information Branch Sip in Staff Information Branch Sip Controller class: .", e);
    }
    return responseObject;
  }

  @Override
  public List getBranchSip() {
    List<BranchSip> result = mapper.getBranchSip();
    return result;
  }

  @Override
  public boolean checkAccountCSR(String userAccount) {
    return mapper.checkAccountCSR(userAccount);
  }
}