package org.osgi.remote.wsn;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * Dummy application for starting the client.
 * 
 * @author Cristiano Longo
 *
 */
public class ClientApplication implements IApplication {

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
