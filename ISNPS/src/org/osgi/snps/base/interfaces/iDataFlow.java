package org.osgi.snps.base.interfaces;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.osgi.framework.BundleContext;
import org.osgi.snps.base.common.Accumulator;
import org.osgi.snps.base.common.SimpleData;

public interface iDataFlow {
		
	public String sayHello();
	
	//public boolean pushData(SimpleData data);

	public boolean pushData(SimpleData sd, String mode,BundleContext context);

	public Accumulator getAccumulator();

	//void Accumulate(String hid, BlockingQueue<String> bq);

	public void Accumulate(String hid, BlockingQueue<String> bq, BundleContext context, String action);


	//public boolean pushData(SimpleData data, String mode, BundleContext context,Action ac);
}
