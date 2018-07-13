package snps.interpreter;


import java.util.Hashtable;
import ch.ethz.iks.r_osgi.RemoteOSGiService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/*import org.osgi.framework.ServiceReference;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import it.remote.interfaces.iWsnInterface;*/
import org.osgi.snps.base.interfaces.iGWInterface;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(final BundleContext context) throws Exception {
	      
		  final Hashtable properties = new Hashtable();
	      properties.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);
	      context.registerService(iGWInterface.class.getName(), new MDWExportService(context), properties);
	      System.out.println("[INTERPRETER:Info] -> State: ACTIVE");
	     // new Worker(context);
	  }

	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
