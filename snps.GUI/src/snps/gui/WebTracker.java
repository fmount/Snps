package snps.gui;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

@SuppressWarnings("rawtypes")
public class WebTracker extends ServiceTracker {
	
	BundleContext context;
	
	//COSTRUTTORE:
	@SuppressWarnings("unchecked")
	public WebTracker (BundleContext context) {
		super(context, HttpService.class.getName(), null);
		this.context = context;
	}
	
	//REGISTRAZIONE DELLE SERVLET DELLA GUI::	
	public Object addingService(ServiceReference reference) {
		HttpService httpService = (HttpService) super.addingService(reference);
		if (httpService == null)
			return null;
		try {
			System.out.println("Registering servlet at /index");
			httpService.registerServlet("/index", new Index(this.context), null, null);
			
			System.out.println("Registering servlet at /index/sensorDetailServlet");
			httpService.registerServlet("/index/sensorDetailServlet", new SensorDetailServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/regSensorServlet");
			httpService.registerServlet("/index/regSensorServlet", new RegSensorServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/samplPlanServlet");
			httpService.registerServlet("/index/samplPlanServlet", new SamplPlanServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/composeServlet");
			httpService.registerServlet("/index/composeServlet", new ComposeServlet(this.context), null, null);
			
			System.out.println("Registering resources at /index/getData.html");
			httpService.registerResources("/index/getData.html", "/getData.html", null);
			
			System.out.println("Registering servlet at /index/getData/oneSensorServlet");
			httpService.registerServlet("/index/getData/oneSensorServlet", new GetDataOneSensorServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/getData/monitorModeServlet");
			httpService.registerServlet("/index/getData/monitorModeServlet", new GetDataMonitorModeServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/getData/sensorHistoryServlet");
			httpService.registerServlet("/index/getData/sensorHistoryServlet", new SensorHistoryServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/sendCommandServlet");
			httpService.registerServlet("/index/sendCommandServlet", new SendCommandServlet(this.context), null, null);
			
			System.out.println("Registering resources at /index/dao.html");
			httpService.registerResources("/index/dao.html", "/dao.html", null);
			
			System.out.println("Registering servlet at /index/daoServlet");
			httpService.registerServlet("/index/daoServlet", new DaoServlet(this.context), null, null);
			
			System.out.println("Registering servlet at /index/daoServlet/detectionsByZoneDetails");
			httpService.registerServlet("/index/daoServlet/detectionsByZoneDetails", new DetecionsDetailsServlet(this.context), null, null);
			
			System.out.println("Registering resources at /index/PlatformTest.html");
			httpService.registerResources("/index/PlatformTest.html", "/PlatformTest.html", null);
			
			System.out.println("Registering servlet at /index/platformTestServlet");
			httpService.registerServlet("/index/platformTestServlet", new PlatformTestServlet(this.context), null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return httpService;
	}

//RIMOZIONE DEL SERVIZIO OFFERTO DALLA SERVLET:	
	public void removedService (ServiceReference reference, Object service) {
		HttpService httpService = (HttpService) service;
		httpService.unregister("/index");
		httpService.unregister("/index/sensorDetailServlet");
		httpService.unregister("/index/regSensorServlet");
		httpService.unregister("/index/samplPlanServlet");
		httpService.unregister("/index/composeServlet");
		httpService.unregister("/index/getData.html");
        httpService.unregister("/index/getData/oneSensorServlet");
        httpService.unregister("/index/getData/monitorModeServlet");
        httpService.unregister("/index/getData/sensorHistoryServlet");
        httpService.unregister("/index/sendCommandServlet");
        httpService.unregister("/index/dao.html");
		httpService.unregister("/index/daoServlet");
		httpService.unregister("/index/daoServlet/detectionsByZoneDetails");
		httpService.unregister("/index/PlatformTest.html");
		httpService.unregister("/index/platformTestServlet");
		super.removedService(reference, service);
  }
}
