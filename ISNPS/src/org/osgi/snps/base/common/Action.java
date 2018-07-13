package org.osgi.snps.base.common;

public class Action {

	String component;
	String cmd;
	double restriction;
	
	public enum restrictions{
		none,parameter
	}
	
	public Action(){}
	
	public Action(String component,String cmd,double restriction){
		this.component = component;
		this.cmd = cmd;
		this.restriction = restriction;
		System.out.println("New Action "+cmd+" required for "+component+" " +
				"with restriction: "+restriction);
	}

	public double getRestriction() {
		return restriction;
	}

	public void setRestriction(double restriction) {
		this.restriction = restriction;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public String toString(){
		return "Exec Action "+cmd+" required for "+component+
				"with restriction: "+restriction;
	}
	
	
}
