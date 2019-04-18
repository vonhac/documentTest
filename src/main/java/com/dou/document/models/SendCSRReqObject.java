package com.dou.document.models;

import java.util.List;

public class SendCSRReqObject {

    private String sendToPlace;

    private List<TraInfoModel> models;

    public String getSendToPlace() {
        return sendToPlace;
    }

    public void setSendToPlace(String sendToPlace) {
        this.sendToPlace = sendToPlace;
    }

    public List<TraInfoModel> getModels() {
        return models;
    }

    public void setModels(List<TraInfoModel> models) {
        this.models = models;
    }
}
