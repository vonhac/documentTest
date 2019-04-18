package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DMTGroupDeferModel extends MasterModel {
    private String id;
    private String groupCd;
    private String groupNm;
    private String description;

    public DMTGroupDeferModel(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupCd() {
        return groupCd;
    }

    public void setGroupCd(String groupCd) {
        this.groupCd = groupCd;
    }

    public String getGroupNm() {
        return groupNm;
    }

    public void setGroupNm(String groupNm) {
        this.groupNm = groupNm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
