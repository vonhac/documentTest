/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */package com.dou.document.services;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.DMTGroupDeferModel;
import com.dou.document.models.DMTDeferDetailModel;

public interface IDMTGroupDeferService {

  //  Defer Group
  ResponseObject loadGroupDeferDefault();
  ResponseObject addDeferGroup(DMTGroupDeferModel dmtGroupDeferModel);
  ResponseObject editDeferGroup(DMTGroupDeferModel dmtGroupDeferModel);
  ResponseObject delDeferGroup(DMTGroupDeferModel dmtGroupDeferModel);

  // Defer Detail
  ResponseObject loadDeferDetailDefault();

  ResponseObject searchDeferDetail(String GroupCd);
  ResponseObject searchDeferDetailByGroupCd (DMTDeferDetailModel dmtDeferDetailModel);
  ResponseObject addDeferDetail(DMTDeferDetailModel dmtDeferDetailModel);
  ResponseObject editDeferDetail(DMTDeferDetailModel dmtDeferDetailModel);
  ResponseObject delDeferDetail(DMTDeferDetailModel dmtDeferDetailModel);

}