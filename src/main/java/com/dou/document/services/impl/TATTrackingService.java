package com.dou.document.services.impl;

import com.dou.document.mappers.ForwarderMapper;
import com.dou.document.mappers.TATTrackingMapper;
import com.dou.document.models.DocumentSts;
import com.dou.document.models.TATTracking;
import com.dou.document.models.TATTrackingPeriod;
import com.dou.document.services.ITATTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TATTrackingService implements ITATTrackingService {
    @Autowired
    private TATTrackingMapper mapper;

    @Autowired
    private ForwarderMapper forwarder;

    @Override
    public Map getDataDB2(Map listParam) {
        List<TATTrackingPeriod> listDataPeriod = new ArrayList<>();

        List<TATTracking> getListParamDB2= new ArrayList<>();
        List<TATTracking> getListParamF1 = new ArrayList<>();
        List<TATTracking> dataFromHome  = new ArrayList<>();
        List<TATTracking> dataFromF1 = new ArrayList<>();
        List<TATTracking> resultDB2 = new ArrayList<>();
        Map<String,List> result = new HashMap<>();
        Map param = new HashMap<>();

        long nDataentrySale = 0;
        long nDataentryCSR = 0;
        long nDataentryModified = 0;
        long nUND = 0;
        long nPDOC = 0;
        long nDisbursement = 0;
        long nDaysfromSale = 0;
        long nDaysfromCSR = 0;
        long nDaysfromModified = 0;

        int lenghtListDB2 = 0;
        int lenghtListF1 = 0;
        int lenghtListHome = 0;

        String dtFrom = listParam.get("fromDt").toString();
        String dtTo= listParam.get("toDt").toString();

        String baseOnStepData = listParam.get("baseOnStepData").toString();
        String documentSts = listParam.get("documentSts").toString();

        listParam.put("dtFromDB2", dtFrom);
        listParam.put("dtToDB2", dtTo);
        listParam.put("documentSts", documentSts);

        resultDB2 = mapper.getDataDB2(listParam);
        for(TATTracking models : resultDB2){
            TATTracking modelDB2 = new TATTracking();
            if(models.getApp_id_BD2() != null){
                modelDB2.setApp_id_BD2(models.getApp_id_BD2());
                getListParamDB2.add(modelDB2);
            }
        }
        param.put("listdata",getListParamDB2);
        param.put("baseOnStep",baseOnStepData);
        param.put("fromDt",dtFrom);
        param.put("toDt",dtTo);

        lenghtListDB2 = resultDB2.size();
        boolean isAppId = false;
        for (TATTracking appId : resultDB2){
            if(appId.getApp_id_BD2() != null){
                isAppId = true;
            }
        }

        if(lenghtListDB2 != 0){
            if(isAppId == true){
               dataFromF1 = this.forwarder.getDataF1(param);

                param.put("listParamF1",getListParamDB2);
                param.put("fromDtHome",dtFrom);
                param.put("toDtHome",dtTo);

                dataFromHome = getDataHome(param);
                lenghtListHome = dataFromHome.size();
            }
        }

        //Merge Data
        for(TATTracking modelDB2 : resultDB2){
            if(lenghtListF1 != 0){
                for(TATTracking modelF1 : dataFromF1){
                    if(modelDB2.getIdNo().equals(modelF1.getIdNo())){
                        modelDB2.setAgreeId(modelF1.getAgreeId());
                        modelDB2.setDataEntry(modelF1.getDataEntry());
                        modelDB2.setPdocDt(modelF1.getPdocDt());
                        modelDB2.setFinishDt(modelF1.getFinishDt());
                        modelDB2.setF1Sts(modelF1.getF1Sts());
                    }
                }
            }
            if(lenghtListHome != 0) {
                for (TATTracking modelHome : dataFromHome) {
                    if (modelDB2.getIdNo().equals(modelHome.getIdNo())) {
                        modelDB2.setPendingDt(modelHome.getPendingDt());
                        modelDB2.setUndDt(modelHome.getUndDt());
                    }
                }
            }
        }
        //End Merge Data

        //Caculator how many day
        for(TATTracking dataPeriod : resultDB2){
            TATTrackingPeriod tatTrackingPeriod = new TATTrackingPeriod();
            long DataentrySale = 0;
            if(dataPeriod.getPendingDt() == null || dataPeriod.getReceivedDt() == null){
                DataentrySale = 0;
            }else {
                DataentrySale = dataPeriod.getPendingDt().getTime() - dataPeriod.getReceivedDt().getTime();
            }

            long DataentryCSR = 0;
            if(dataPeriod.getPendingDt() == null || dataPeriod.getDistributedDt() == null){
                DataentryCSR = 0;
            }else {
                DataentryCSR  = dataPeriod.getPendingDt().getTime() -  dataPeriod.getDistributedDt().getTime();
            }

            long DataentryModified = 0;
            if(dataPeriod.getPendingDt() == null || dataPeriod.getLastModified() == null){
                DataentryModified = 0;
            }else {
                DataentryModified  = dataPeriod.getPendingDt().getTime() -  dataPeriod.getLastModified().getTime();
            }

            long UND = 0;
            if(dataPeriod.getUndDt() == null || dataPeriod.getPendingDt() == null){
                UND = 0;
            }else {
                UND  = dataPeriod.getUndDt().getTime() -  dataPeriod.getPendingDt().getTime();
            }

            long PDOC = 0;
            if(dataPeriod.getPdocDt() == null || dataPeriod.getUndDt() == null){
                PDOC = 0;
            }else {
                PDOC  = dataPeriod.getPdocDt().getTime() -  dataPeriod.getUndDt().getTime();
            }

            long Disbursement = 0;
            if(dataPeriod.getFinishDt() == null || dataPeriod.getPdocDt() == null){
                Disbursement = 0;
            }else {
                Disbursement  = dataPeriod.getFinishDt().getTime() -  dataPeriod.getPdocDt().getTime();
            }

            long DaysfromSale = 0;
            if(dataPeriod.getPdocDt() == null || dataPeriod.getUndDt() == null){
                DaysfromSale = 0;
            }else {
                DaysfromSale  = DataentrySale + dataPeriod.getPdocDt().getTime() +  dataPeriod.getUndDt().getTime() + Disbursement;
            }

            long DaysfromCSR = 0;
            if(dataPeriod.getPdocDt() == null || dataPeriod.getUndDt() == null){
                DaysfromCSR = 0;
            }else {
                DaysfromCSR  = DataentryCSR + dataPeriod.getPdocDt().getTime() +  dataPeriod.getUndDt().getTime() + Disbursement;
            }

            long DaysfromModified = 0;
            if(dataPeriod.getPdocDt() == null || dataPeriod.getUndDt() == null){
                DaysfromModified = 0;
            }else {
                DaysfromModified  = DataentryModified + dataPeriod.getPdocDt().getTime() +  dataPeriod.getUndDt().getTime() + Disbursement;
            }

            nDataentrySale = TimeUnit.MILLISECONDS.toDays(DataentrySale);
            nDataentryCSR = TimeUnit.MILLISECONDS.toDays(DataentryCSR);
            nDataentryModified = TimeUnit.MILLISECONDS.toDays(DataentryModified);
            nUND = TimeUnit.MILLISECONDS.toDays(UND);
            nPDOC = TimeUnit.MILLISECONDS.toDays(PDOC);
            nDisbursement = TimeUnit.MILLISECONDS.toDays(Disbursement);
            nDaysfromSale = TimeUnit.MILLISECONDS.toDays(DaysfromSale);
            nDaysfromCSR = TimeUnit.MILLISECONDS.toDays(DaysfromCSR);
            nDaysfromModified = TimeUnit.MILLISECONDS.toDays(DaysfromModified);

            tatTrackingPeriod.setIdNo(dataPeriod.getIdNo());
            tatTrackingPeriod.setCustomerNm(dataPeriod.getCustomerNm());
            tatTrackingPeriod.setProductNm(dataPeriod.getProductNm());
            tatTrackingPeriod.setSalesCd(dataPeriod.getSalesCd());
            tatTrackingPeriod.setBranchSip(dataPeriod.getBranchSip());
            tatTrackingPeriod.setAgreeId(dataPeriod.getAgreeId());
            tatTrackingPeriod.setDataEntry(dataPeriod.getDataEntry());
            tatTrackingPeriod.setDocumentSts(dataPeriod.getDocumentSts());
            tatTrackingPeriod.setF1Sts(dataPeriod.getF1Sts());
            tatTrackingPeriod.setDataEntrySale(nDataentrySale);
            tatTrackingPeriod.setDataEntryCsr(nDataentryCSR);
            tatTrackingPeriod.setDaysFromModified(nDataentryModified);
            tatTrackingPeriod.setUndDay(nUND);
            tatTrackingPeriod.setPdocDay(nPDOC);
            tatTrackingPeriod.setDisbursementDay(nDisbursement);
            tatTrackingPeriod.setDaysFromSale(nDaysfromSale);
            tatTrackingPeriod.setDaysFromCsr(nDaysfromCSR);
            tatTrackingPeriod.setDaysFromModified(nDaysfromModified);

            listDataPeriod.add(tatTrackingPeriod);
        }
        //End caculator

        result.put("listTATDate",resultDB2);
        result.put("listTATPeriod",listDataPeriod);
        return result;
    }
    public List<TATTracking> getDataHome(Map param) {
        List<TATTracking> resultHome= mapper.getDataHome(param);
        return resultHome;
    }

    @Override
    public List<DocumentSts> getDocumentSts() {
        List<DocumentSts> resultHome= mapper.getDocumentSts();
        return resultHome;
    }
}
