package org.osgi.snps.base.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.snps.base.common.*;
import org.w3c.dom.Document;

public interface iCoreInterface {
	
	public String processorCall(String command, SimpleData sd, String mode,
			String[]options);
	
	public String sayHello();
	
	public Map<String, ABComponent> getSensList();
	
	public boolean addToSensList(String key,ABComponent value);
	
	
	public ArrayList<String> getTestCmd();
	
	public boolean addTestCmd(String key);

	//public String getData(String sId, String mode);


	public String interprCall(String command, SamplingPlan sPlan, List<String> sids,
			String mode);
	
	public String composerCall(String command,List<String> slist, String mode);
	
	public boolean sensorExist(List<String> sId);

	//public String regCall(String command, int opcode, String key,
			//Document description, ABComponent s, String type);

	//public String getData(String sId, String mode);

	public List<String> getActuators();

	public List<String> getHybrids();
	
	public boolean monitorCall(String action, String param);

	public String getData(String sId, String mode, String action);

	public Map<String, String> getActions();
	
	public boolean addToActionList(String key, String value);

	public String regCall(String command, int opcode, String key,
			Document description, ABComponent s, String type,
			List<String> params);

	public String isAlive(String sId);
	
}
