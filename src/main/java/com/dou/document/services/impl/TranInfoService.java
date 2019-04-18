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
import com.dou.document.ftp.FTPExecutor;
import com.dou.document.mappers.ITraInfoMapper;
import com.dou.document.mappers.ParameterMapper;
import com.dou.document.models.*;
import com.dou.document.services.ITranInfoService;
import com.dou.document.shared.DMTConfigurationCode;
import com.dou.document.shared.DocumentStatus;
import com.dou.document.shared.VNCharacterUtils;
import com.dou.document.vo.DocumentDetailVO;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TranInfoService implements ITranInfoService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(TranInfoService.class);

    @Autowired
    private ITraInfoMapper mapper;

    @Autowired
    private FTPExecutor executor;

    @Autowired
    private CheckSystemService checkSystemService;

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    @Transactional
    public ResponseObject loadChannelCodeDefault() {
        ResponseObject<DocumentDetailVO> responseObject = new ResponseObject<>();
        try{
            List<DocumentStatus> acceptStatus = new ArrayList<>();
            acceptStatus.add(DocumentStatus.ORIGINAL);
            acceptStatus.add(DocumentStatus.VALID);
            acceptStatus.add(DocumentStatus.INVALID);

            List<ChannelCodeModel> listChannelCd = mapper.loadChannelCodeDefault();
            List<ChannelCodeModel> listStatusCd = mapper.loadStatusCodeDefault(acceptStatus);
            if (listStatusCd.size()> 0 || listChannelCd.size() > 0) {
                DocumentDetailVO documentDetailVO= new DocumentDetailVO(listChannelCd, listStatusCd);
                responseObject.setData(documentDetailVO);
            } else {
                responseObject.setSuccess(false);
                responseObject.setMessage("Không có dữ liệu");
            }
        } catch(Exception e){
            responseObject.setSuccess(false);
            responseObject.setFailMessage("Data Status : Error " );
            LOGGER.error("Error when call listData  in CodeChannelModel class: .", e);
        }
        return responseObject;
    }

    @Override
    public ResponseObject searchMatching(DocumentFilterReqObject conditions) {
        try{
            List<TraInfoModel> listSearch = mapper.searchMatching(conditions);
            if(listSearch.size() == 0){
                return ResponseObject.failResponse("No Found Data");
            }
            return new ResponseObject(listSearch);
        } catch(Exception e) {
            LOGGER.error("Error when call function searchMatching in searchMatching class TraInfoMapper: .", e);
            return ResponseObject.failResponse("Data Status : Error ");
        }
    }

    @Override
    public ResponseObject addAppID(DocumentFilterReqObject conditions) {
        ResponseObject responseObject = new ResponseObject();
        try{
            if(!"".equalsIgnoreCase(conditions.getAddAppID()) && !"".equalsIgnoreCase(conditions.getDocumentCd())){
                boolean result = mapper.addAppID(conditions.getDocumentCd(),conditions.getAddAppID());
                if(result){
                    responseObject.setSuccess(true);
                    responseObject.setMessage("Add AppID Successfully");
                    return responseObject;
                }
            }
        }catch (Exception e){
            LOGGER.error("Error when call function Add AppID in  class TraInfoMapper: .", e);
            return ResponseObject.failResponse("Save AppID Failed");
        }
        return ResponseObject.failResponse("Save AppID Failed");
    }

    @Override
    public ResponseObject loadDocuments(String documentCd) {
        try{
           if(!"".equals(documentCd)&& documentCd.length() > 0){
               List<TraInfoModel> loadDocument = mapper.loadDocuments(documentCd);
               if (loadDocument.size() == 0){
                   return ResponseObject.failResponse("No found data in table DMT_DOCUMENTS");
               }else{
                   return new ResponseObject(loadDocument);
               }
           }

        } catch (Exception e){
            LOGGER.error("Error select ID in table DMT_DOCUMENTS: .", e);
        }
        return ResponseObject.failResponse(CommonStrings.RESP_MSG_NOT_FOUND);
    }

    @Override
    public ResponseObject searchStatusHistory(String documentNo) {
        ResponseObject responseObject = new ResponseObject();
        try{
            if(documentNo != null && documentNo.length() >0){
                List<DMTHistoryStatusModel> dataSearchHistory = mapper.getHistoryStatus(documentNo);
                if(dataSearchHistory.size() == 0){
                    responseObject.setMessage("No found data status history");
                    responseObject.setSuccess(false);
                    return responseObject;
                }
                if(dataSearchHistory.get(0).getStatusCd() == DocumentStatus.DEFER){
                    Date dateDeadline = dataSearchHistory.get(0).getCreatedDate();
                    Calendar time = Calendar.getInstance();
                    time.setTime(dateDeadline);
                    ParameterModel param = parameterMapper.getParam(DMTConfigurationCode.PARAM_DEADLINE);
                    time.add(Calendar.DAY_OF_MONTH, Integer.parseInt(param.getValue()));
                    dataSearchHistory.get(0).setDeadlineDefer(time.getTime());
                }
                responseObject.setData(dataSearchHistory);
            }else{
                responseObject.setMessage("document_cd invalid");
                responseObject.setSuccess(false);
                return responseObject;
            }
        }catch (Exception e){
            LOGGER.error("Error select status history: .", e);
            responseObject.failResponse("Data Status : Error ");
            responseObject.setSuccess(false);
        }
        return responseObject;
    }

    @Override
    public ResponseObject retrieveDeferHistoryByDocument(String documentNo) {
        ResponseObject responseObject = new ResponseObject();
        try{
            if(documentNo != null && documentNo.length() >0){
                List<DMTDeferDetailModel> listDeferHistory = mapper.loadDeferHistoryByIdNo(documentNo);
                if(listDeferHistory.size() ==0){
                    responseObject.setMessage("No found data defer list");
                    responseObject.setSuccess(false);
                    return responseObject;
                }
                return new ResponseObject(listDeferHistory);
            }else{
                responseObject.setMessage("document_cd invalid");
                responseObject.setSuccess(false);
                return responseObject;
            }
        }catch(Exception e){
            LOGGER.error("Error select defer list invalid: .", e);
            responseObject.failResponse("select defer list invalid: Error ");
        }
        return responseObject;
    }

    @Override
     public int addDeferDocument(List<DMTDeferDetailModel> listAddDeferDocument) {
        int result =0;
        try{
            result = mapper.addDefersForDocument(listAddDeferDocument);
        }catch(Exception e){
            LOGGER.error("Error Don't insert Defer Document: .", e);
        }
        return result;
    }

    @Override
    public ResponseObject updateDeferDocument(DMTDeferDetailModel listUpdateDeferDocument) {
        int result =0;
        ResponseObject responseObject = new ResponseObject();
        try{
            result = mapper.updateDefersForDocument(listUpdateDeferDocument);
            responseObject.setData(result);
        }catch(Exception e){
            LOGGER.error("Error Don't update Defer Document: .", e);
            responseObject.failResponse("function update defer list: Error ");
        }
        return responseObject;
    }

    @Override
    public ResponseObject updateDeferDocumentList(List<DMTDeferDetailModel> listUpdateDeferDocument) {
        int result =0;
        ResponseObject responseObject = new ResponseObject();
        try{
            for(int i =0; i< listUpdateDeferDocument.size(); i++){
                listUpdateDeferDocument.get(i).setDeferCheck(true);
                result = mapper.updateDefersForDocument(listUpdateDeferDocument.get(i));
            }
            responseObject.setData(result);
        }catch(Exception e){
            LOGGER.error("Error Don't update Defer Document: .", e);
            responseObject.failResponse("function update defer list: Error " );
        }
        return responseObject;
    }

    @Override
    public ResponseObject addDefersStatusHistory(DMTHistoryStatusModel historyStatusModel) {
        ResponseObject responseObject = new ResponseObject();
        try{
            int historyStatusModelList = mapper.addDefersStatusHistory(historyStatusModel);
            responseObject.setData(historyStatusModelList);
        }catch(Exception e){
            LOGGER.error("Error Don't insert Status Defer Document: .", e);
        }
        return responseObject;
    }

    @Override
    public ResponseObject changeStatusDefer(DMTHistoryStatusModel historyStatusModel) {
        ResponseObject responseObject = new ResponseObject();
        DocumentFilterReqObject documentFilterReqObject = new DocumentFilterReqObject();
        try{
            DocumentStatus statusCurrent = historyStatusModel.getStatusCd();
            if(statusCurrent == DocumentStatus.CANCEL ){
                int rs = mapper.addStatusHistory(historyStatusModel);
                if(rs == 1){
                    responseObject.setData(rs);
                    return responseObject;
                }else{
                    return ResponseObject.failResponse(CommonStrings.RESP_MSG_SAVE_FAIL);
                }
            }else{
                int historyStatusModelList = mapper.addStatusHistory(historyStatusModel);
                responseObject.setData(historyStatusModelList);
                return responseObject;
            }
        }catch(Exception e){
            LOGGER.error("Error Don't insert Status Defer Document: .", e);
        }
        return responseObject;
    }

    @Override
    public ResponseObject uploadFileModified(MultipartFile file, String filename) {

        if(StringUtils.isEmpty(filename)) return ResponseObject.failResponse("Please provide filename ");
        if(file == null || file.isEmpty()) return ResponseObject.failResponse("No found a valid file");

        filename = VNCharacterUtils.removeAccent(filename) + ".pdf";
        try {
            if (executor.push(file.getInputStream(),filename)) {
                return ResponseObject.SUCCESS_WITHOUT_DATA;
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return  ResponseObject.failResponse(CommonStrings.RESP_MSG_UPLOAD_FAIL);
    }

    @Override
    public ResponseObject viewAttachedFile(String filename) {
        if(StringUtils.isEmpty(filename)) return ResponseObject.failResponse("Please provide filename");
        try{
            filename = VNCharacterUtils.removeAccent(filename) + ".pdf";
            byte[] fileArr = executor.pull(filename);
            if(fileArr == null){
                return ResponseObject.failResponse("Can't download file on FTP server");
            }

            return new ResponseObject(fileArr);

        }catch (Exception e){
            LOGGER.error("", e);
        }

        return  ResponseObject.failResponse(CommonStrings.RESP_MSG_DOWNLOAD_FAIL);
    }

    @Override
    public ResponseObject changeStatusPass(DMTHistoryStatusModel statusModel) {
        ResponseObject responseObject = new ResponseObject();
        try{
            if(statusModel.getStatusCd() == DocumentStatus.PASS && statusModel.getIdNo() != null){
                ResultDrsModel drsModel = new ResultDrsModel();
                drsModel.setIdOrPhone(statusModel.getIdNo());
                drsModel.setChannelId(statusModel.getChannelId());
                boolean rs = checkSystemService.checkSystemF1(drsModel);
                if(!rs) {
                    statusModel.setStatusCd(DocumentStatus.CANCEL_STEP_PASS);
                    int rSave = mapper.addStatusHistory(statusModel);
                    if(rSave == 1 ){
                        responseObject.setMessage("Status current is Cancel step Pass");
                        responseObject.setSuccess(true);
                        return responseObject;
                    }else{
                        responseObject.setMessage("Error: Can't save document");
                        responseObject.setSuccess(false);
                        return responseObject;
                    }
                }else if( rs){
                    int rSave = mapper.addStatusHistory(statusModel);
                    if(rSave == 1 ){
                        List<TraInfoModel> checkList = mapper.checkDocumentByIdNo(statusModel.getIdNo());
                        List<TraInfoModel> checkPass = mapper.checkDocumentPass(statusModel.getIdNo());
                        if(checkList.size() > 0){
                            for(int i =0; i< checkList.size(); i++ ){
                                DMTHistoryStatusModel model = new DMTHistoryStatusModel();
                                model.setDocumentCd(String.valueOf(checkList.get(i).getDocumentCd()));
                                model.setStatusCd(DocumentStatus.CANCEL_STEP_PASS);
                                if(checkPass.size() > 0){
                                    model.setDescription("Cancel by Document of " + checkPass.get(i).getSalesChnl());
                                }
                                mapper.addStatusHistory(model);
                            }
                        }
                        responseObject.setMessage("Status current is Pass");
                        responseObject.setSuccess(true);
                        return responseObject;
                    }else{
                        responseObject.setMessage("Error: Can't save document");
                        responseObject.setSuccess(false);
                        return responseObject;
                    }
                }
            }else{
                return ResponseObject.failResponse(CommonStrings.RESP_MSG_SAVE_FAIL);
            }
        }catch(Exception e){
            LOGGER.error("", e);
        }
        return  ResponseObject.failResponse(CommonStrings.RESP_MSG_SAVE_FAIL);
    }

    @Override
    public ResponseObject changeStatusModified(DMTHistoryStatusModel statusModel) {
        ResponseObject responseObject = new ResponseObject();
        DocumentFilterReqObject documentFilterReqObject = new DocumentFilterReqObject();
        try{
            if(statusModel.getStatusCd() == DocumentStatus.MODIFIED){
                String documentCode = statusModel.getDocumentCd();
                documentFilterReqObject.setDocumentCd(documentCode);
                List<TraInfoModel> rs = mapper.searchMatching(documentFilterReqObject);
                String custumerName = VNCharacterUtils.removeAccent(rs.get(0).getCustomerNm());

                String fileName = documentCode + "_" + custumerName;
                statusModel.setFileName(fileName);

                int rs1 = mapper.addStatusHistory(statusModel);
                if(rs1 == 1){
                    responseObject.setData(rs1);
                    return responseObject;
                }else{
                    return ResponseObject.failResponse(CommonStrings.RESP_MSG_SAVE_FAIL);
                }
            }
        }catch(Exception e){
            LOGGER.error("Error can't save change status this document", e);
        }
        return ResponseObject.failResponse(CommonStrings.RESP_MSG_SAVE_FAIL);
    }
}