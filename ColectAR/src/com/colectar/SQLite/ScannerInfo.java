package com.colectar.SQLite;

public class ScannerInfo {
	  private long id;
	  private String harvester;
	  private String info_readed;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getHarvester() {
		return harvester;
	}
	public void setHarvester(String harvester) {
		this.harvester = harvester;
	}
	public String getInfo_readed() {
		return info_readed;
	}
	public void setInfo_readed(String info_readed) {
		this.info_readed = info_readed;
	}
}
