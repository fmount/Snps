package org.osgi.snps.base.common;

public class BaseStation {
	public String bsid;
	public String description;
	public String name;
	public String ip;
	
	public BaseStation(String bsid, String description, String name, String ip) {
		this.bsid = bsid;
		this.description = description;
		this.name = name;
		this.ip = ip;
	}

	public String getBsid() {
		return bsid;
	}

	public void setBsid(String bsid) {
		this.bsid = bsid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString(){
		return this.getBsid()+", "+this.getDescription()+", "+this.getName()+", "+
				this.getIp();
	}
}