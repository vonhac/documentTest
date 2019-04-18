package com.dou.document.vo;




import com.dou.document.models.ChannelCodeModel;

import java.util.List;

public class DocumentDetailVO {

    private List<ChannelCodeModel> listChannelCd;
    private List<ChannelCodeModel> listStatusCd;


    public DocumentDetailVO(List<ChannelCodeModel> listChannelCd, List<ChannelCodeModel>listStatusCd){
        this.listChannelCd =listChannelCd;
        this.listStatusCd =listStatusCd;
    }

    public List<ChannelCodeModel> getListChannelCd() {
        return listChannelCd;
    }

    public void setListChannelCd(List<ChannelCodeModel> listChannelCd) {
        this.listChannelCd = listChannelCd;
    }

    public List<ChannelCodeModel> getListStatusCd() {
        return listStatusCd;
    }

    public void setListStatusCd(List<ChannelCodeModel> listStatusCd) {
        this.listStatusCd = listStatusCd;
    }
}
