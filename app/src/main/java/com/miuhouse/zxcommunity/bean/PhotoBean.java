package com.miuhouse.zxcommunity.bean;


import java.util.List;

public class PhotoBean extends BasePhotoBean{
	public String folder;
	public List<Photos>images;
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public List<Photos> getImage() {
		return images;
	}
	public void setImage(List<Photos> image) {
		this.images = image;
	}
	

}
