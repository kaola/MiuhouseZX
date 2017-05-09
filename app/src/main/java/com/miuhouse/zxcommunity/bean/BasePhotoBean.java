package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;

public class BasePhotoBean implements Serializable{
	
	private   String phone_type="3";
	private String imei;
	private String version_code;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVersion_code() {
		return version_code;
	}
	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}
	
}
