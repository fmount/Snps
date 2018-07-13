package org.osgi.snps.base.common;

import org.osgi.snps.base.util.Util;


public class SimpleData {

	protected String sid="";
	protected String _id_meas="";
	protected String data="";
	protected String ref="";
	protected String date="";
	protected String time="";
	
	public SimpleData(){}
	
	public SimpleData(String sid , String data, String ref,String date,String time){
		this.sid = sid;
		this.data = data;
		this.ref = ref;
		this.date = date;
		this.time = time;
		this._id_meas = Util.IdGenerator().replace("-", "");
		
	}

	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
	@Override
	public String toString(){
		return "sid:"+this.getSid()+",data:"+this.getData()+",type:"+
				this.getRef()+",date:"+this.getDate()+","+this.getTime();
	}

	public String get_id_meas() {
		return _id_meas;
	}

	public void set_id_meas(String _id_meas) {
		this._id_meas = _id_meas;
	}
}
