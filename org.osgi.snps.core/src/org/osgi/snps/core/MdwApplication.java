package org.osgi.snps.core;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * Dummy application for starting the client.
 * 
 * @author f3anceSco
 *
 */
public class MdwApplication implements IApplication {

	//@Override
	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Client Started");
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}