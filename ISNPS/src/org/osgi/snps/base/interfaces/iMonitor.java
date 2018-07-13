package org.osgi.snps.base.interfaces;

import org.osgi.snps.base.common.Action;

public interface iMonitor {
	
	public void monitor(String sid, Action act);
	
	public boolean stop(String param);

}
