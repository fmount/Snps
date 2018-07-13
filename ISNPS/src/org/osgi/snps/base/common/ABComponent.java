package org.osgi.snps.base.common;

import java.io.Serializable;

import org.osgi.framework.BundleContext;


public abstract class ABComponent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String ID;
	private String name;
	private String model;
	private String type;
	private String description;
	private String nature; //Type of sensor/actuator
	private String state; //Inactive,Active
	
	public ABComponent(String id,String name, String model,String type,String state,
			String description, String nature){
		ID = id;
		this.name = name;
		this.model = model;
		this.type = type;
		this.description = description;
		this.state=state;
		this.nature = nature;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	@Override
	public String toString(){
		
		String str="{_id: "+getID()+",name: "+getName()+",model: "+getModel()+
				",type: "+getType()+",description: "+getDescription()+
				",state: "+getState()+",nature: "+getNature();		
		return str;		
	}

	public String isAlive(BundleContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
