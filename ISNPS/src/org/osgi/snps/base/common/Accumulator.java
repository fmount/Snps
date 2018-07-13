package org.osgi.snps.base.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;



public class Accumulator{
	
	public Map<String,BlockingQueue<String>> struct;
	
	public Accumulator(){
		struct = new HashMap<String, BlockingQueue<String>>();
	}
	
	public BlockingQueue<String> getBQ(String hid){
		return struct.get(hid);
	}
	
	public void Accumulate(String hid, BlockingQueue<String> bq){
		struct.put(hid, bq);
	}
	
	public void addElementToBQ(String hid,String data){
		struct.get(hid).add(data);
		System.out.println(struct.get(hid).toString());
	}	

}
