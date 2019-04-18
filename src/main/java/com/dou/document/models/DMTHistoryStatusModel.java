/*
 * @Author: Nhac.Vo
 * @Email: nhac.vo@dounets.com
 * @Date: 2019-02-19 09:00:00
 * @Last Modified by:
 * @Last Modified time:
 */
package com.dou.document.models;

import com.dou.document.shared.DocumentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class DMTHistoryStatusModel  extends MasterModel {
    private String id;
    private String documentCd;
    private String description;
    private DocumentStatus statusCd;
    private String fileName;
    private String channelId;
    private String idNo;
    private Date deadlineDefer;

    public DMTHistoryStatusModel(){}

    public Date getDeadlineDefer() {
        return deadlineDefer;
    }

    public void setDeadlineDefer(Date deadlineDefer) {
        this.deadlineDefer = deadlineDefer;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentCd() {
        return documentCd;
    }

    public void setDocumentCd(String documentCd) {
        this.documentCd = documentCd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public DocumentStatus getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(DocumentStatus statusCd) {
        this.statusCd = statusCd;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
