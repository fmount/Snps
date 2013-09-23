
package org.platform.sns;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.platform.common.SamplingPlan;
import org.platform.common.Sensor;
import org.platform.util.*;

/**
 * This class was generated by Apache CXF 2.1.4
 * Fri Jul 26 11:49:08 CEST 2013
 * Generated source version: 2.1.4
 * @author francesco 
 * 
 */

@SuppressWarnings("unused")
public final class IWebIntegrationInterfacePortType_IWebIntegrationInterfacePort_Client {

    private static final QName SERVICE_NAME = new QName("http://interfaces.base.snps.osgi.org/", "iWebIntegrationInterface");

    private IWebIntegrationInterfacePortType_IWebIntegrationInterfacePort_Client() {
    }
    
    public enum commands {
		getSensor, getSDesc,getAllSensors, sensType, history, allhistory, 
		getSensInfo, getsenslist, getzonelist, inszone, insbs, rmvzone, rmvbs,
		getbsinfo, getzoneinfo, insertentry, rmventry, updateentry, getnodelist,
		detection, detectbydate, detectbytime, detectbydateandtime, detectbyzone,
		getSensorbyzone, getSensorbynode
	}

    public static void main(String args[]) throws Exception {
        //URL wsdlURL = IWebIntegrationInterface.WSDL_LOCATION;
    	URL wsdlURL = new URL(Util.loadConfiguration("config.xml"));
        if (args.length > 0) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
         
        
        /**CLIENT MANAGEMENT**/
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String answer,answ = "";
		String type,initDate,endDate,initTime,endTime,zoneid,bsid,id_detection="";
		boolean result, jump = false;
		IWebIntegrationInterface ss = new IWebIntegrationInterface(wsdlURL, SERVICE_NAME);
        IWebIntegrationInterfacePortType port = ss.getIWebIntegrationInterfacePort();  
		do {
			Util.clientHelp();
			System.out.println("[SNS:Info] -> Select Service:\n");
			String userInput, command = "";
			List<String> options = new ArrayList<String>();
			answer = "";
			
			userInput = stdIn.readLine();
			jump = false;
			try {
				BufferedReader stdin = new BufferedReader(new InputStreamReader(
						System.in));
				switch (Integer.parseInt(userInput)) {
				case 0:
					System.out.println("[SNS:Info]-> Bye");
					System.exit(0);
				
				
				case 1: 
					System.out.println("[SNS:SayHello] Invoking SNPS Middleware...");
			        java.lang.String _sayhello__return = port.sayhello();
			        System.out.println("SNPS Middleware says: " + _sayhello__return);
					break;
				
				
				case 2: 
					
					System.out.println("[SNS:SendCommand] Invoking SNPS Middleware...");
			        System.out.println("What command?");
			        System.out.println("1. Enable\n");
					System.out.println("2. Disable\n");
					String exec="";
					int ex = Integer.parseInt(stdIn.readLine());
					if(ex==1) {
						exec = "enable";
					}
					else if(ex==2){
						exec = "disable";
					}
			        java.lang.String _sendCommand_arg0 = exec;
			        
			        String sensorList = port.discoverySensorsAndMeasurements
			        		("getAllSensors", "","","","","","","", "", "");
			        
			        List<String> s= JSonUtil.JsonToArrayList(sensorList);
			        System.out.println("Available Sensors:");
			        for(int i=0;i<s.size();i++){
			        	Sensor sens = JSonUtil.JsonToSensor(s.get(i));
			        	System.out.println(i+1+")"+sens.getID());
			        }
			        
			        answ = "y";
					org.platform.sns.ArrayOfString _sendCommand_arg1 = new org.platform.sns.ArrayOfString();
			        java.util.List<java.lang.String> _sendCommand_arg1String = new java.util.ArrayList<java.lang.String>();
			        //java.lang.String _sendCommand_arg1StringVal1 = "_sendCommand_arg1StringVal-29817788";
			        
					do {
						try {
							System.out.println("What Sensors? (type ids)");
							String str = stdin.readLine();
							//slist.add(str);
							_sendCommand_arg1String.add(str);
					        _sendCommand_arg1.getString().addAll(_sendCommand_arg1String);
							System.out.println("Another one? (y/n) [Default yes]");
							answ = stdin.readLine();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} while (!answ.equals("n"));
					
					
					System.out.println("Result: "
							+port.sendCommand(exec, _sendCommand_arg1, ""));
			       /*org.platform.sns.ArrayOfString _sendCommand_arg1 = new org.platform.sns.ArrayOfString();
			        java.util.List<java.lang.String> _sendCommand_arg1String = new java.util.ArrayList<java.lang.String>();
			        java.lang.String _sendCommand_arg1StringVal1 = "_sendCommand_arg1StringVal-29817788";
			        _sendCommand_arg1String.add(_sendCommand_arg1StringVal1);
			        _sendCommand_arg1.getString().addAll(_sendCommand_arg1String);
			        
			        java.lang.String _sendCommand_arg2 = "";
			        
			        java.lang.String _sendCommand__return = port.sendCommand(_sendCommand_arg0, _sendCommand_arg1, _sendCommand_arg2);
			        System.out.println("sendCommand.result=" + _sendCommand__return);		*/			
					break;
				
				
				case 3: 
					stdin = new BufferedReader(new InputStreamReader(
							System.in));
					System.out.println("[SNS:SetSamplingPlan] Invoking SNPS Middleware...");
					
					String sList = port.discoverySensorsAndMeasurements
			        		("getAllSensors", "","","","","","","", "", "");
			        
			        List<String> s1= JSonUtil.JsonToArrayList(sList);
			        System.out.println("Available Sensors:");
			        for(int i=0;i<s1.size();i++){
			        	Sensor sens = JSonUtil.JsonToSensor(s1.get(i));
			        	System.out.println(i+1+")"+sens.getID());
			        }
			        
			        answ = "y";
					List<String> slist = new ArrayList<String>();
								        
					do {
						try {
							System.out.println("What Sensors? (type ids)");
							String str = stdin.readLine();
							slist.add(str);
							System.out.println("Another one? (y/n) [Default yes]");
							answ = stdin.readLine();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} while (!answ.equals("n"));
					
					
					SamplingPlan plan = new SamplingPlan("", slist, 30.0, 40.0, 200);
					plan.setSplan_identifier(Util.IdGenerator().replace("-", ""));
					
					
					java.lang.String _setSPlan_arg0 = JSonUtil.SamplingPlanToJSON(plan); 
			        java.lang.String _setSPlan__return = port.setSPlan(_setSPlan_arg0);
			        System.out.println("setSPlan.result=" + _setSPlan__return);
					break;
				
				
				case 4: 
					System.out.println("[SNS:BuildVirtualSensor] -> Invoking SNPS Middleware...");
			        List<String> sss = new ArrayList<String>();
					 String sensList = port.discoverySensorsAndMeasurements
				        		("getAllSensors", "","","","","","","", "", "");
				        
				        List<String> s2= JSonUtil.JsonToArrayList(sensList);
				        System.out.println("Available Sensors:");
				        for(int i=0;i<s2.size();i++){
				        	Sensor sens = JSonUtil.JsonToSensor(s2.get(i));
				        	System.out.println(i+1+")"+sens.getID());
				        }
				        
				        answ = "y";
				        org.platform.sns.ArrayOfString _buildVirtualSensor_arg0 = new org.platform.sns.ArrayOfString();
				        java.util.List<java.lang.String> _buildVirtualSensor_arg0String = new java.util.ArrayList<java.lang.String>();
			        
			        do {
						try {
							System.out.println("What Sensors? (type ids)");
							String str = stdin.readLine();
							_buildVirtualSensor_arg0String.add(str);
							sss.add(str);
							System.out.println("Another one? (y/n) [Default yes]");
							answ = stdin.readLine();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					} while (!answ.equals("n"));
					
			        if(sss.size()<2){
			        	System.out.println("Building Service must have at least 2 simple sensors!");
			        	break;
			        }
			        _buildVirtualSensor_arg0.getString().addAll(_buildVirtualSensor_arg0String);
			        java.lang.String _buildVirtualSensor_arg1 = "";
			        java.lang.String _buildVirtualSensor__return = port.buildVirtualSensor(_buildVirtualSensor_arg0, _buildVirtualSensor_arg1);
			        System.out.println("New Virtual Sensor identifier: " + _buildVirtualSensor__return);
					break;
				
				case 5: 
					System.out.println("[SNS:DiscoverySensorsAndMeasures] -> Invoking SNPS Middleware...");
					java.lang.String _discoverySensorsAndMeasurements__return = "";
					String sid="";
					Util.discoveryHelp();
					try {
						System.out.println("What Query? (type ids)");
						command = stdin.readLine();
					
					switch (commands.valueOf(command)) {
					
					case getSensor:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = 
						port.discoverySensorsAndMeasurements(
								command,sid, "","","","","","","","");
				        System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						
				        break;
					case getSDesc:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = 
								port.discoverySensorsAndMeasurements(
										command,sid, "","","","","","","","");
						        System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						        break;
					case getAllSensors:			
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","","","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
						
					case sensType:
						System.out.println("Type the Sensor Type (Phenomenon Nature): ");
						type = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "",type,"","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					
					
					case getSensInfo:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case getbsinfo:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case getzoneinfo:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case getSensorbyzone:
						System.out.println("Type the Zone Id: ");
						zoneid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","","","","","","", zoneid, "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case getSensorbynode:
						System.out.println("Type the Base Station Id: ");
						bsid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","","","","","","", "", bsid);
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case history:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","","","","","", "", "");
						System.out.println("Query Result = " + _discoverySensorsAndMeasurements__return);
						break;
					case allhistory:						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","","","","","","", "", "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					case detection:						
						System.out.println("What detection Id ?");
						id_detection = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","",id_detection,"","","","", "", "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					case detectbydate:
						
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						
						System.out.println("What init date? (FORMAT: dd-mm-yyy)");
						initDate = stdin.readLine();
						
						System.out.println("What end date? (FORMAT: dd-mm-yyy)");
						endDate = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","",initDate,endDate,"","", "", "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					case detectbytime:
						System.out.println("Type the Sensor Id: ");
						sid = stdin.readLine();
						
						
						System.out.println("What date? (FORMAT: dd-mm-yyy)");
						initDate = stdin.readLine();
						
						System.out.println("What init time? (FORMAT: xx:yy:zz)");
						initTime = stdin.readLine();
						
						System.out.println("What end time? (FORMAT: xx:yy:zz)");
						endTime = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, sid,"","",initDate,"",initTime,endTime, "", "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					case detectbydateandtime:
						
						System.out.println("What init date? (FORMAT: dd-mm-yyy)");
						initDate = stdin.readLine();
						
						System.out.println("What end date? (FORMAT: dd-mm-yyy)");
						endDate = stdin.readLine();
						
						System.out.println("What init time? (FORMAT: xx:yy:zz)");
						initTime = stdin.readLine();
						
						System.out.println("What end time? (FORMAT: xx:yy:zz)");
						endTime = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
								(command, sid,"","",initDate,endDate,initTime,endTime, "", "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					case detectbyzone:
						System.out.println("Type the Zone Id: ");
						zoneid = stdin.readLine();
						
						_discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements
		        		(command, "","","","","","","", zoneid, "");
						System.out.println("Query Rresult = " + _discoverySensorsAndMeasurements__return);
						break;
					default:
						System.out.println("Command not found!");
						break;
					}
					
					}catch(Exception e){
						e.printStackTrace();
					}					
					
					break;
				
				case 6: 
					System.out.println("[SNS:GetDataFromSensor] -> Invoking SNPS Middleware...");
					String senssList = port.discoverySensorsAndMeasurements
			        		("getAllSensors", "","","","","","","", "", "");
			        
			        List<String> s3= JSonUtil.JsonToArrayList(senssList);
			        System.out.println("Available Sensors:");
			        for(int i=0;i<s3.size();i++){
			        	Sensor sens = JSonUtil.JsonToSensor(s3.get(i));
			        	System.out.println(i+1+")"+sens.getID());
			        }      
		        
			        System.out.println("What Sensors? (type ids)");
					String str = stdin.readLine();
					java.lang.String _getData_arg0 = str;
					
					System.out.println("Mode:");
					System.out.println("1. Sync\n");
					System.out.println("2. Async\n");
					String mode="";
					ex = Integer.parseInt(stdIn.readLine());
					if(ex==1) {
						mode = "sync";
					}
					else if(ex==2){
						mode = "async";
					}
					else{
						System.out.println("Error selecting mode!! ");
						break;
					}
				    java.lang.String _getData_arg1 = mode;
				    java.lang.String _getData_arg2 = "none";
				    java.lang.String _getData__return = port.getData(_getData_arg0, _getData_arg1, _getData_arg2);
				    System.out.println("GetData Result=" + _getData__return);
					break;
				
				default: 
					System.out.println("[SNS:Alert] -> Service not found!!\n");
					break;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("[SNS:Alert] -> Service not found!!\n");
			}
			if (!jump) {
				System.out
						.println("Do you want to test another service? [Y/N default=Y]\n");
				answer = stdIn.readLine();
			}
		} while (!answer.equals("n"));
			System.out.println("[SNS:Info] -> Stop Client");
			System.out.println("[SNS:Info] -> Bye");
			System.exit(0);

			
			
			
			
			
			
			
			
			
			

        {
        System.out.println("Invoking buildVirtualSensor...");
        org.platform.sns.ArrayOfString _buildVirtualSensor_arg0 = new org.platform.sns.ArrayOfString();
        java.util.List<java.lang.String> _buildVirtualSensor_arg0String = new java.util.ArrayList<java.lang.String>();
        java.lang.String _buildVirtualSensor_arg0StringVal1 = "_buildVirtualSensor_arg0StringVal-2018635232";
        _buildVirtualSensor_arg0String.add(_buildVirtualSensor_arg0StringVal1);
        _buildVirtualSensor_arg0.getString().addAll(_buildVirtualSensor_arg0String);
        java.lang.String _buildVirtualSensor_arg1 = "_buildVirtualSensor_arg1-291706736";
        java.lang.String _buildVirtualSensor__return = port.buildVirtualSensor(_buildVirtualSensor_arg0, _buildVirtualSensor_arg1);
        System.out.println("buildVirtualSensor.result=" + _buildVirtualSensor__return);


        }
        
        {
        System.out.println("Invoking discoverySensorsAndMeasurements...");
        java.lang.String _discoverySensorsAndMeasurements_arg0 = "_discoverySensorsAndMeasurements_arg0-1876815754";
        java.lang.String _discoverySensorsAndMeasurements_arg1 = "_discoverySensorsAndMeasurements_arg1-703702973";
        java.lang.String _discoverySensorsAndMeasurements_arg2 = "_discoverySensorsAndMeasurements_arg2381336866";
        java.lang.String _discoverySensorsAndMeasurements_arg3 = "_discoverySensorsAndMeasurements_arg32139406483";
        java.lang.String _discoverySensorsAndMeasurements_arg4 = "_discoverySensorsAndMeasurements_arg4-1538342533";
        java.lang.String _discoverySensorsAndMeasurements_arg5 = "_discoverySensorsAndMeasurements_arg5-2143495963";
        java.lang.String _discoverySensorsAndMeasurements_arg6 = "_discoverySensorsAndMeasurements_arg6-1357553961";
        java.lang.String _discoverySensorsAndMeasurements_arg7 = "_discoverySensorsAndMeasurements_arg71846906922";
        java.lang.String _discoverySensorsAndMeasurements_arg8 = "_discoverySensorsAndMeasurements_arg8-392000696";
        java.lang.String _discoverySensorsAndMeasurements_arg9 = "_discoverySensorsAndMeasurements_arg91080770568";
        java.lang.String _discoverySensorsAndMeasurements__return = port.discoverySensorsAndMeasurements(_discoverySensorsAndMeasurements_arg0, _discoverySensorsAndMeasurements_arg1, _discoverySensorsAndMeasurements_arg2, _discoverySensorsAndMeasurements_arg3, _discoverySensorsAndMeasurements_arg4, _discoverySensorsAndMeasurements_arg5, _discoverySensorsAndMeasurements_arg6, _discoverySensorsAndMeasurements_arg7, _discoverySensorsAndMeasurements_arg8, _discoverySensorsAndMeasurements_arg9);
        System.out.println("discoverySensorsAndMeasurements.result=" + _discoverySensorsAndMeasurements__return);


        }
        {
        System.out.println("Invoking getData...");
        java.lang.String _getData_arg0 = "_getData_arg0-1624834680";
        java.lang.String _getData_arg1 = "_getData_arg1-1679320948";
        java.lang.String _getData_arg2 = "_getData_arg21127783771";
        java.lang.String _getData__return = port.getData(_getData_arg0, _getData_arg1, _getData_arg2);
        System.out.println("getData.result=" + _getData__return);


        }
       
        {
        System.out.println("Invoking setSPlan...");
        java.lang.String _setSPlan_arg0 = "_setSPlan_arg01360673859";
        java.lang.String _setSPlan__return = port.setSPlan(_setSPlan_arg0);
        System.out.println("setSPlan.result=" + _setSPlan__return);


        }
        {
        System.out.println("Invoking stopSPlan...");
        java.lang.String _stopSPlan_arg0 = "_stopSPlan_arg0-1973918038";
        java.lang.String _stopSPlan__return = port.stopSPlan(_stopSPlan_arg0);
        System.out.println("stopSPlan.result=" + _stopSPlan__return);


        }

        System.exit(0);
    }
    
    

}
