package publisher;

import java.util.Dictionary;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.snps.base.interfaces.iEventPublisherInterface;

public class EventPublisher implements iEventPublisherInterface {
	
	private final EventAdmin admin;
	BundleContext context;
	static String topic; 
	
	
	public enum commands{registration, alert, data, }
	
	@SuppressWarnings("static-access")
	public EventPublisher(EventAdmin admin,BundleContext context,String topic){
		this.admin = admin;
		this.context=context;
		EventPublisher.topic =  topic;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendEvent(String id, String type) {
			
			Dictionary payload = new Properties();		
			payload.put(type, id);
			Event event =	new Event(topic,payload);
			System.out.println("Sending event on topic!: "+event.getTopic());
			//ASYNC
			admin.postEvent(event);
			//System.out.println("Sent: "+id+ " - "+type);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendDataEvent(String sensorid, String data){
			Dictionary payload = new Properties();
			payload.put("sid", sensorid);						
			payload.put("data", data);
			//Event event =	new Event("osgi/dataEvent/"+sensorid,payload);
			Event event =	new Event("osgi/dataEvent",payload);
			System.out.println("Sending event " +event.toString()+" on topic!: "+event.getTopic());
			//ASYNC
			admin.postEvent(event);
			System.out.println("Sent");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendDataEventWithAction(String sensorid, String data,String ac){
			Dictionary payload = new Properties();
			payload.put("sid", sensorid);						
			payload.put("data", data);
			payload.put("action", ac);
			//Event event =	new Event("osgi/dataEvent/"+sensorid,payload);
			Event event =	new Event("osgi/dataEvent",payload);
			System.out.println("Sending event " +event.toString()+" on topic!: "+event.getTopic());
			//ASYNC
			admin.postEvent(event);
			System.out.println("Sent");
	}
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendEventt(String userid, List<String> testcmds) {
		System.out.println("AA" +userid +" - "+testcmds);
		Dictionary payload = new Properties();
		
		if(testcmds.isEmpty()){
			Random random = new Random();
			payload.put("sid", "test"+random.nextInt(100));
			System.out.println("test"+random.nextInt(100));
						
			System.out.println("I CAN SEND..");
			payload.put("data", (Math.random()*10));
			payload.put("timestamp",System.currentTimeMillis());
			Event event =	new Event("osgi/Event",payload);
			System.out.println("Sending event " +event.toString()+" on topic!: "+event.getTopic());
			//ASYNC
			admin.postEvent(event);
			System.out.println("Sent");
			return;
		}
		
		if(testcmds.size()==1){
			payload.put("sid", testcmds.get(0));
			System.out.println("PUB: "+testcmds.get(0));
			payload.put("data", (Math.random()*10));
			payload.put("timestamp",System.currentTimeMillis());
			Event event =	new Event("osgi/Event",payload);
			System.out.println("Sending event on topic!: "+event.getTopic());
			//ASYNC
			admin.postEvent(event);
			System.out.println("Sent: "+userid+ " - "+testcmds.toString());
			return;
		}
	}
}
