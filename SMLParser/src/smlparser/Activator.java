package smlparser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		Activator.context = bundleContext;
		Parser service = new ParserService();
		ServiceRegistration registration = bundleContext.registerService(Parser.class.getName(),service, new Hashtable());
		
		/*ServiceReference reference;
		reference = context
				.getServiceReference(Parser.class.getName());

		service = (Parser) context.getService(reference);
		service.parse("SensorML.xml");*/
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
