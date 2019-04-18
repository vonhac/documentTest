/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.controllers;


import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTGroupDeferModel;
import com.dou.document.services.IDMTGroupDeferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/defer-rule")
public class GroupDeferController {

    @Autowired
    private IDMTGroupDeferService service;
    /*
    * Group defer
    * function load Data : loadGroupDefault()
    * add: addDeferGroup()
    * edit: editDeferGroup()
    * delete: delDeferGroup()
    * */
    @GetMapping("/groupdefer-load")
    public ResponseObject loadGroupDeferDefault() {
        return service.loadGroupDeferDefault();
    }

    @PostMapping("/groupdefer-add")
    public ResponseObject addDeferGroup(@RequestBody DMTGroupDeferModel dmtGroupDeferModel) {
        return service.addDeferGroup(dmtGroupDeferModel);
    }

    @PostMapping("/groupdefer-edit")
    public ResponseObject editDeferGroup(@RequestBody DMTGroupDeferModel dmtGroupDeferModel) {
        return service.editDeferGroup(dmtGroupDeferModel);
    }

    @PostMapping("/groupdefer-del")
    public ResponseObject delDeferGroup(@RequestBody DMTGroupDeferModel dmtGroupDeferModel) {
        return service.delDeferGroup(dmtGroupDeferModel);
    }



    /*
     * Defer Detail
     * function load Data : loadDeferDetailDefault()
     * add: addDeferDetail()
     * edit: editDeferDetail()
     * delete: delDeferDetail()
     * search: searchDeferDetailByGroupCdDeferNm() , searchDeferDetailByGroupCd()
     * */
    @GetMapping("/infodeferdetail")
    public ResponseObject loadDeferDetailDefault() {
        return service.loadDeferDetailDefault();
    }

    @PostMapping("/deferdetail-add")
    public ResponseObject addDeferDetail(@RequestBody DMTDeferDetailModel dmtDeferDetailModel) {
        return service.addDeferDetail(dmtDeferDetailModel);
    }

    @PostMapping("/deferdetail-edit")
    public ResponseObject editDeferDetail(@RequestBody DMTDeferDetailModel dmtDeferDetailModel) {
        return service.editDeferDetail(dmtDeferDetailModel);
    }

    @PostMapping("/deferdetail-searchid-nm")
    public ResponseObject searchDeferDetailByGroupCdDeferNm(@RequestBody DMTDeferDetailModel dmtDeferDetailModel) {
        return service.searchDeferDetailByGroupCd(dmtDeferDetailModel);
    }

    @PostMapping("/{group_cd}/deferdetail-searchid")
    public ResponseObject searchDeferDetailByGroupCd(@PathVariable("group_cd") String groupCd  ) {
        return service.searchDeferDetail(groupCd);
    }

    @PostMapping("/deferdetail-del")
    public ResponseObject delDeferDetail(@RequestBody DMTDeferDetailModel dmtDeferDetailModel) {
        return service.delDeferDetail(dmtDeferDetailModel);
    }
}