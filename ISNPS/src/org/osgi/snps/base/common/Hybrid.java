package org.osgi.snps.base.common;

import java.util.List;

import org.osgi.framework.BundleContext;

public interface Hybrid {
		
	public String getData(List<Sensor> sens, Template templ, String mode,
			BundleContext context);
	
	public boolean sendCmd(List<Sensor> sens, String  cmd);
	
	public boolean setSPlan(SamplingPlan sPlan);
}
