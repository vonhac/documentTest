/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.services;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTHistoryStatusModel;
import com.dou.document.models.DocumentFilterReqObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITranInfoService {

    ResponseObject loadChannelCodeDefault                   ();

    ResponseObject searchMatching                           (DocumentFilterReqObject conditions);
    ResponseObject addAppID                           (DocumentFilterReqObject conditions);

    ResponseObject  loadDocuments                          (String documentCd);


    ResponseObject searchStatusHistory                      (String documentNo);

    ResponseObject retrieveDeferHistoryByDocument           (String documentNo);
    int addDeferDocument                                    (List<DMTDeferDetailModel> listAddDeferDocument);
    ResponseObject updateDeferDocument                                 (DMTDeferDetailModel listUpdateDeferDocument);
    ResponseObject updateDeferDocumentList                                 (List<DMTDeferDetailModel> listUpdateDeferDocument);

    ResponseObject addDefersStatusHistory (DMTHistoryStatusModel historyStatusModel);
    ResponseObject changeStatusDefer (DMTHistoryStatusModel historyStatusModel);

    ResponseObject uploadFileModified(MultipartFile file, String filename);

    ResponseObject viewAttachedFile(String filename);

    ResponseObject changeStatusPass(DMTHistoryStatusModel statusModel);
    ResponseObject changeStatusModified(DMTHistoryStatusModel statusModel);
}