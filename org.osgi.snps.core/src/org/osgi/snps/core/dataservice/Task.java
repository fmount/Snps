package org.osgi.snps.core.dataservice;

import java.util.TimerTask;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.interfaces.iCoreInterface;

class Task extends TimerTask{
	
	
	//Sensor s;
	ABComponent s;
	BundleContext context;
	String mode;
	String[] options;
	String action;
	iCoreInterface service;
	
	public Task(ABComponent sens, BundleContext context,String mode, String[] options,String action){
		this.s = sens;
		this.context = context;
		this.mode = mode;
		this.options = options;
		
		this.action = action;
		
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void run(){
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class
				.getName());

		service = (iCoreInterface) context.getService(reference);
		if(s==null){
			for(String key : service.getSensList().keySet()){
				service.getData(service.getSensList().get(key).getID(), mode, action);
			}
		}else{
			service.getData(s.getID(), mode, action);
		}
	}
	
}