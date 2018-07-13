package org.osgi.remote.wsnclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.SimpleData;
import org.osgi.snps.base.interfaces.iWsnInterface;
import org.osgi.snps.base.util.JSonUtil;

public class WSNClient implements Runnable {

	BundleContext context;
	iWsnInterface wsnservice;

	public WSNClient(BundleContext context) {
		this.context = context;
	}

	public enum commands {
		sendData, regSensor, quit
	}

	public void invokeCommands() {
		System.out.println("Start wsn client \n");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer = "";
		ServiceReference reference;
		reference = context.getServiceReference(iWsnInterface.class.getName());
		wsnservice = (iWsnInterface) context.getService(reference);
		List<String> params = new ArrayList<String>();

		do {
			menu();
			System.out.println("\nInsert Command: \n");
			String input = null, command = "";
			boolean jump = false;
			try {
				input = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (input != null) {
				StringTokenizer str = new StringTokenizer(input, " ");
				command = str.nextToken();
				while (str.hasMoreTokens()) {
					params.add(str.nextToken());
				}

				switch (commands.valueOf(command)) {

				case sendData:
					if (params.get(0).equals("--sId")) {
						String id = params.get(1);
						System.out.println(params.toString());
						SimpleData data = new SimpleData();
						data.setSid(id);
						System.out.println("Sent Data to MW " + id + "\n");
						System.out.println("Data sended: " + wsnservice.sendDataToMiddleware(JSonUtil
								.SimpleDataToJSON(data)));
						params.clear();
					} else {
						System.out.println("Check command syntax");
					}
					break;
				case regSensor:
					System.out.println("Registration new Sensor: " + wsnservice.registerSensor());
					break;
				case quit:
					answer = "n";
					jump = true;
					break;
				default:
					System.out.println("Command not found \n");
				}

				if (!jump) {
					System.out
							.println("Do you want to try another command? [y/n default y] \n");
					try {
						answer = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} while (!answer.equals("n"));

		System.out.println("Stopping wsn client \n");

	}

	public void menu() {
		for (Enum command : commands.class.getEnumConstants()) {
			if (command.toString().equals("sendData")) {
				System.out.println("[Command] " + command
						+ " --sId <SensorId> \n");
			} else if (command.toString().equals("regSensor")) {
				System.out.println("[Command] " + command
						+ " <SensorML.xml> \n");
			}else{
				System.out.println("[Command]" + command + "\n");
			}
		}
	}

	@Override
	public void run() {
		invokeCommands();
	}

}
