package com.dou.document.controllers;

import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.StaffInforBranchSip;
import com.dou.document.services.IStaffInforBranchSipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branch-sips")
public class StaffInforBranchSipController {

  @Autowired
  private IStaffInforBranchSipService service;

  @GetMapping
  public ResponseObject getDataStaffBranchSipByFilterCondition(@RequestParam (value = "staff_cd", required = false) String staffCd) {
    staffCd = staffCd == ""? null:staffCd;
    return service.getDataStaffBranchSipByFilterCondition(staffCd);
  }

  @PostMapping
  public ResponseObject saveBranchSip(@RequestBody StaffInforBranchSip StaffInforBranchSip) {
    return service.saveAdd(StaffInforBranchSip);
  }

  @PutMapping("/updateDataBranchSip")
  public ResponseObject updateBranchSip(@RequestBody StaffInforBranchSip StaffInforBranchSip) {
    return service.updateData(StaffInforBranchSip);
  }

  @PutMapping("/branch")
  public ResponseObject removeBranch(@RequestBody List<String> branchs) {
    return service.deleteBranchSip(branchs);
  }

  @GetMapping("/getAllbranchSip")
  public ResponseObject getBranchSip(){
    ResponseObject responseObject = new ResponseObject<>();
    responseObject.setData(service.getBranchSip());
    return responseObject;
  }
}