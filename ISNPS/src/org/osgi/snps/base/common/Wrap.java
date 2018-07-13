package org.osgi.snps.base.common;

import org.w3c.dom.Document;

public class Wrap {

	String str;
	boolean opresult;
	Document description;
	Sensor sensor;
	
	public Wrap(){}
	
	public Wrap(String str,boolean opresult, Document description, Sensor sensor){
		this.str=str;
		this.opresult=opresult;
		this.description=description;
		this.sensor=sensor;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public boolean isOpresult() {
		return opresult;
	}

	public void setOpresult(boolean opresult) {
		this.opresult = opresult;
	}

	public Document getDescription() {
		return description;
	}

	public void setDescription(Document description) {
		this.description = description;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	};
	
}
