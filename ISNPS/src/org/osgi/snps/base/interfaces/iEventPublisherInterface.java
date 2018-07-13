package org.osgi.snps.base.interfaces;

import java.util.List;

public interface iEventPublisherInterface {
	
	public void sendEvent(String id,String type);
	
	public void sendEventt(String userid, List<String> testcmds);

	//void sendDataEvent(String sensorid, String data);

	public void sendDataEvent(String sensorid, String data);

	public void sendDataEventWithAction(String sensorid, String data, String ac);

}
