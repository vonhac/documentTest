package com.dou.document.vo;

import com.dou.document.models.DMTDeferDetailModel;
import com.dou.document.models.DMTGroupDeferModel;
import com.dou.document.models.DMTHistoryStatusModel;

import java.util.List;

public class DocumentHistoryVO {
    private List<DMTHistoryStatusModel> historyStatusModelList;
    private List<DMTDeferDetailModel>   deferDetailModelList;
    private List<DMTGroupDeferModel>    groupDeferModelList;

    public DocumentHistoryVO(List<DMTHistoryStatusModel> historyStatusModelList ,List<DMTDeferDetailModel> deferDetailModelList, List<DMTGroupDeferModel> groupDeferModelList){
        this.deferDetailModelList = deferDetailModelList;
        this.historyStatusModelList = historyStatusModelList;
        this.groupDeferModelList = groupDeferModelList;
    }

    public List<DMTHistoryStatusModel> getHistoryStatusModelList() {
        return historyStatusModelList;
    }

    public void setHistoryStatusModelList(List<DMTHistoryStatusModel> historyStatusModelList) {
        this.historyStatusModelList = historyStatusModelList;
    }

    public List<DMTDeferDetailModel> getDeferDetailModelList() {
        return deferDetailModelList;
    }

    public void setDeferDetailModelList(List<DMTDeferDetailModel> deferDetailModelList) {
        this.deferDetailModelList = deferDetailModelList;
    }

    public List<DMTGroupDeferModel> getGroupDeferModelList() {
        return groupDeferModelList;
    }

    public void setGroupDeferModelList(List<DMTGroupDeferModel> groupDeferModelList) {
        this.groupDeferModelList = groupDeferModelList;
    }
}
