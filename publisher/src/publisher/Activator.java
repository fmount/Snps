package publisher;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;
import org.osgi.snps.base.interfaces.iEventPublisherInterface;

public class Activator implements BundleActivator {

	private static BundleContext context;

	final static String[] topics = { "osgi/snps/log" };
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ServiceReference ref = context.getServiceReference(EventAdmin.class.getName());
		EventAdmin eventAdmin =	(EventAdmin) context.getService(ref);
		iEventPublisherInterface service = new EventPublisher(eventAdmin,context,"osgi/testEvent");
		ServiceRegistration registration = bundleContext.registerService(iEventPublisherInterface.class.getName(),service, new Hashtable());
		System.out.println("Service registered: "+ registration.getReference());		
		System.out.println("[PUBLISHER SERVICE:Info] -> State:ACTIVE");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
