package snps.registry;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.snps.base.interfaces.iRegistryInterface;




public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	
	/**
	 * Metodo che esporta sul Contex OSGI locale i servizi che sono stati definiti nell'interfaccia relativa
	 * al Registry, in modo da renderli disponibili per l'invocazione (sia locale che remota).
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void start(BundleContext bundleContext) throws Exception {
		
		//System.out.println(bundleContext.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " starting...");
		
		//ESPORTAZIONE DEL SERVIZIO
		Activator.context = bundleContext;
		iRegistryInterface service = new RegisterService(context);
		System.out.println("[IREGISTRY:Info] -> State: ACTIVE");
		ServiceRegistration registration = bundleContext.registerService(iRegistryInterface.class.getName(),service, new Hashtable());
		//System.out.println("Service registered: "+ registration.getReference());
		
	}

	
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println(bundleContext.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " stopping...");
		Activator.context = null;
	}

}
