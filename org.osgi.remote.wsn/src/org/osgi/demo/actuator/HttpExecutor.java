package org.osgi.demo.actuator;

/*import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;*/


public class HttpExecutor {

	private static final String SEPARATOR = ":";
	private static final String ENABLE = "enable";
	private static final String DISABEL = "disable";

	/**
	 * @param args
	 */
	//public static void main(String[] args) {
	public static void exec(String cmd){
		//while(true){
		String url = "http://10.31.127.57/";
		HttpRequestHandler handler = new HttpRequestHandler(url);
		String command = "forms.htm?";
		
		/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter command:");*/
		String port = null;
		String led = null;
		String action = null;
		
		try {
			//sostituire il source della stringa con formattazione command:port
			//command= Enable, disable
			//port = [0,8]
			//String[] commandSplitted = br.readLine().split(SEPARATOR);
			String[] commandSplitted = cmd.split(SEPARATOR);
			if(commandSplitted[0].equalsIgnoreCase(ENABLE))
			{
				action = "1";
			}
			else if(commandSplitted[0].equalsIgnoreCase(DISABEL))
			{
				action = "0";
			}
						
				if(Integer.parseInt(commandSplitted[1])>=0 && Integer.parseInt(commandSplitted[1])<=8)
				port = commandSplitted[1];		
			led = "led" + port;
					
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		if(command != null && led != null && action !=null)
		handler.execute(command,led,action);
		else
			System.out.println("ERROR: malformed command");
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	//	}
	}
}
