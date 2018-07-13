package org.osgi.snps.base.common;

import org.osgi.framework.BundleContext;



public interface Component {
	
	
	//public String getData(BundleContext context,String mode);

	//public String getData(BundleContext context, String mode, String[] options);

	public String getData(BundleContext context, String mode, String[] options,
			String action);
	

	String isAlive(BundleContext context);
	
}
