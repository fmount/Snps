package snps.wii;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.snps.base.interfaces.iWebIntegrationInterface;
import org.osgi.snps.base.util.Util;

public class Activator implements BundleActivator {
    @SuppressWarnings("rawtypes")
	private ServiceRegistration registration;

    @Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(BundleContext bc) throws Exception {
        Dictionary props = new Hashtable();

        props.put("service.exported.interfaces", "*");
        //props.put("org.apache.cxf.ws.address", "http://192.168.0.8:9090/wii");
        String url = Util.loadConfiguration("config.xml");
        if(url.equalsIgnoreCase("")){
        	url = "http://localhost:9090/wii";
        }
        props.put("org.apache.cxf.ws.address", url);
        props.put("service.exported.configs", "org.apache.cxf.ws");
        
        registration = bc.registerService(iWebIntegrationInterface.class.getName(),
                                          new iWebService(bc), props);
        System.out.println("Service Exposed as WS");
    }

    @Override
	public void stop(BundleContext bc) throws Exception {
        registration.unregister();
    }
}
