package org.osgi.snps.base.common;

public class Zone {
	
	public String zoneid;
	public String name;
	public String description;
	public String edificio;
	public String piano;
	
	
	
	
	public Zone(String zoneid, String name, String description,
			String edificio, String piano) {
		super();
		this.zoneid = zoneid;
		this.name = name;
		this.description = description;
		this.edificio = edificio;
		this.piano = piano;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEdificio() {
		return edificio;
	}
	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
	}
	
	
	
}
