/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.controllers;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTHistoryStatusModel;
import com.dou.document.models.DocumentFilterReqObject;
import com.dou.document.models.TraInfoModel;
import com.dou.document.services.IDMTGroupDeferService;
import com.dou.document.services.ITranInfoService;
import com.dou.document.shared.DocumentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@RestController
@RequestMapping("/api/tracking-information")
public class TraInfoController {

    @Autowired
    private ITranInfoService service;

    @Autowired
    private IDMTGroupDeferService serviceDefer;

    @GetMapping("/channels")
    public ResponseObject retrieveChannelAndStatus() {
        return service.loadChannelCodeDefault();
    }

    @GetMapping("/documents")
    public ResponseObject filterDocuments(
            @RequestParam(value = "status_cd",        required = false) DocumentStatus statusCd,
            @RequestParam(value = "department_cd",    required = false) String departmentCd,
            @RequestParam(value = "distributed_date", required = false) Date distributedDate,
            @RequestParam(value = "sending_date",     required = false) Date sendingDate,
            @RequestParam(value = "sale_cd",          required = false) String saleCd,
            @RequestParam(value = "bpo_checking",     required = false) Boolean bpoChecking,
            @RequestParam(value = "document_cd",     required = false)  String documentCd,
            @RequestParam(value = "id_no",     required = false)  String idNo) {

        return service.searchMatching(new DocumentFilterReqObject(
                departmentCd, bpoChecking, statusCd, distributedDate, saleCd, sendingDate, documentCd, idNo
        ));
    }
    @PostMapping("/documents/addappid")
    public ResponseObject addAppID(@RequestBody DocumentFilterReqObject filterReqObject) {
        return service.addAppID(filterReqObject);
    }

    @PostMapping("/documents/{document_cd}/loaddocuments")
    public ResponseObject loadDocuments( @PathVariable("document_cd")  String documentNo) {
        return service.loadDocuments(documentNo);
    }

    @PostMapping("/documents/{document_no}/history-status")
    public ResponseObject retrieveStatusHistory(@PathVariable("document_no") String documentNo){
        return service.searchStatusHistory(documentNo);
    }

    @PostMapping("/documents/{document_no}/defers")
    public ResponseObject retrieveDeferHistory(@PathVariable("document_no") String documentNo) {
        return service.retrieveDeferHistoryByDocument(documentNo);
    }

    @PostMapping("/documents/retrievedefergroups")
    public ResponseObject retrieveDeferHistory() {
        return serviceDefer.loadGroupDeferDefault();
    }

    @PostMapping("/documents/adddefers")
    public int addDeferDocuments(@RequestBody List<DMTDeferDetailModel> listAddDeferDocument) {
        return service.addDeferDocument(listAddDeferDocument);
    }

    @PostMapping("/documents/updatedefers")
    public ResponseObject updateDeferDocuments(@RequestBody DMTDeferDetailModel listUpdateDeferDocument) {
        return service.updateDeferDocument(listUpdateDeferDocument);
    }

    @PostMapping("/documents/updatedeferslist")
    public ResponseObject updateDeferDocumentsList(@RequestBody List<DMTDeferDetailModel> listUpdateDeferDocument) {
        return service.updateDeferDocumentList(listUpdateDeferDocument);
    }
    @PostMapping("/documents/addstatusdeferhistory")
    public ResponseObject addDefersStatusHistory(@RequestBody DMTHistoryStatusModel listAddDeferHistoryDocument) {
        return service.addDefersStatusHistory(listAddDeferHistoryDocument);
    }

    @PostMapping("/documents/changestatusdefer")
    public ResponseObject changeStatusDefer(@RequestBody DMTHistoryStatusModel listAddDeferHistoryDocument) {
        return service.changeStatusDefer(listAddDeferHistoryDocument);
    }

    @PostMapping("/documents/changestatuspass")
    public ResponseObject changeStatusPass(@RequestBody DMTHistoryStatusModel listAddDeferHistoryDocument) {
        return service.changeStatusPass(listAddDeferHistoryDocument);
    }

    @PostMapping("/documents/changestatusmodified")
    public ResponseObject changeStatusModified(@RequestBody DMTHistoryStatusModel listAddDeferHistoryDocument) {
        return service.changeStatusModified(listAddDeferHistoryDocument);
    }

    @PostMapping("/documents/changestatuscancel")
    public ResponseObject changeStatusCancel(@RequestBody DMTHistoryStatusModel listAddDeferHistoryDocument) {
        return service.changeStatusDefer(listAddDeferHistoryDocument);
    }


    @PostMapping(value = "/documents/upload/{filename}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseObject uploadFileModified(@RequestParam (value = "file") MultipartFile file,
                                            @PathVariable ("filename") String filename) {
        return service.uploadFileModified(file, filename);
    } //documents/attachment-file/${filename}

    @GetMapping("/documents/attachment-file/{filename}")
    public ResponseObject viewAttachedFile( @PathVariable ("filename") String filename) {
        return service.viewAttachedFile(filename);
    }
}