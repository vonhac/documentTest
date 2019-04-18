package com.dou.document.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ResultDrsModel {
	private String idOrPhone;
	private Integer sum;
	private String channelId;
	private int id;
	
	public ResultDrsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdOrPhone() {
		return idOrPhone;
	}
	public void setIdOrPhone(String idOrPhone) {
		this.idOrPhone = idOrPhone;
	}
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
}
