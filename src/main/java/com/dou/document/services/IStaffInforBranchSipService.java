package com.dou.document.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.StaffInforBranchSip;

import java.util.List;

public interface IStaffInforBranchSipService {

  ResponseObject getDataStaffBranchSipByFilterCondition(String staffCd);

  ResponseObject saveAdd(StaffInforBranchSip StaffInforBranchSip);

  ResponseObject updateData(StaffInforBranchSip StaffInforBranchSip);

  ResponseObject deleteBranchSip(List<String> lstBranch);

  List checkDuplicatedBranSip(StaffInforBranchSip StaffInforBranchSip);

  List getBranchSip();

  boolean checkAccountCSR(String userAccount);

}