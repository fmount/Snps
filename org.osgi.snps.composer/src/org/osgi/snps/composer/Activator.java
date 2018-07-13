package org.osgi.snps.composer;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.snps.base.interfaces.iCompose;

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
		ServiceRegistration registration;
		iCompose service = new ComposerService(context);
		registration = bundleContext.registerService(
				iCompose.class.getName(),service, new Hashtable());
		System.out.println("[COMPOSER: Info] -> State: ACTIVE");
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
