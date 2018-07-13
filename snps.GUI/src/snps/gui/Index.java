package snps.gui;

import java.io.*;
import java.lang.String;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.interfaces.*;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String path = "./AvailableSensors.txt";
	public File file;
	public BundleContext context;
	static iCoreInterface service;
	
//COSTRUTTORE:	
	public Index (BundleContext context) {
		this.context=context;
	}
    
	@Override
	public void init (ServletConfig config) 
			throws ServletException {
		super.init(config);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		//Il blocco try-catch mi crea ogni volta che si avvia la servlet un nuovo file che dopo
		//andrò ad aggiornare con la lista dei sensori disponibili
		try {
			file = new File(path);
			if (file.exists())
				file.delete();
			file.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		resp.setContentType("text/html");
		//resp.getWriter().write("Hello from the cloud!");
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		PrintWriter out = resp.getWriter();
		
	//INIZIO PAGINA HTML:		
		out.println("<HTML><HEAD>");

	//TITOLO PAGINA:		
		out.println("<TITLE>HOME</TITLE>");
		out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-15\"/>");
		out.println("<meta http-equiv=\"content-script-type\" content=\"text/javascript\" />");
		out.println("<meta http-equiv=\"content-style-type\" content=\"text/css\" />");
		out.println("<meta http-equiv=\"imagetoolbar\" content=\"no\" />");
		out.println("<meta name=\"language\" content=\"italian-it\" />");
		//out.println("<link rel=\"stylesheet\" href=\"layout.css\" type=\"text/css\">");

		//DICHIARAZIONE CSS:
		out.println("<style type=\"text/css\">");
		out.println("body {text-align:center;padding: 0;margin: 0;overflow-x: hidden;}");
		//# telaio:
		out.println("#telaio{"
				+ "font-family:\"Arial\";"
				+ "width:100%;"
				+ "margin:0px;"
				+ "border:1px solid gray;"
				+ "line-height:150%;"
				+ "color:black;"
				+ "background-color: rgb(242,244,248);"
				+ "text-align:left;}");
		//#testata:
		out.println("#testata{"
				+ "text-align:center;"
				+ "font-size: 110%;}");
		//#fondo:
		out.println("#fondo{"
				+ "font-size: 85%;"
				+ "text-align: center;}");
		//#testata+fondo:
		out.println("#testata, #fondo {"
				+ "padding:0.5em;"
				+ "color:white;"
				+ "background-color:#3A5896;"
				+ "clear:left;}");
		//#sinistro:
		out.println("#sinistro {"
				+ "text-align:center;"
				+ "float:left;"
				+ "width:190px;"
				+ "padding:5px;"
				+ "border-right:1px solid gray;}");
		//#sinistro ul: 
		out.println("#sinistro ul {"
				+ "margin: 1em 0;"
				+ "padding: 0;"
				+ "list-style: none;}");
		//#sinistro li:
		out.println("#sinistro li {"
				+ "margin: 0.5em 0;"
				+ "padding: 0;}");
		//#sinistro a:link, #sinistro a:visited:
		out.println("#sinistro a:link, #sinistro a:visited {"
				+ "display: block;"
				+ "width: 90%;"
				+ "margin: 0;"
				+ "padding: 0.2em;"
				+ "background: rgb(242,244,248);"
				+ "color: black;"
				+ "text-decoration: none;"
				+ "font-weight: bold;"
				+ "border: 1px solid gray;}");
		//#sinistro a:hover:
		out.println("#sinistro a:hover {"
				+ "background: #3A5896;"
				+ "color: #fff;"
				+ "border: 1px solid gray;"
				+ "text-decoration: none;"
				+ "font-weight: bold;}");
		//#centrale:
		out.println("#centrale {"
				+ "margin-left: 200px;"
				+ "padding:10px;"
				+ "border-left:1px solid gray;}");
		//#sinistro, #centrale:
		out.println("#sinistro, #centrale {"
				+ "min-height:470px;}");

		out.println("</style></HEAD>");
		//FINE DICHIARAZIONE CSS

		//INIZIO BODY HTML:
		out.println("<body>");
		
			//div TELAIO:	
			out.println("<div id=\"telaio\">");
				
				//div TESTATA:
				out.println("<div id=\"testata\"><h1>Sensor Node Plug-in System - SNPS</h1></div>");
				
				//div SINISTRO: --> contiene menu
				out.println("<div id=\"sinistro\"> "
						+ "<ul><li><a href=\"http://localhost:8080/index\">Home</a></li> "
						+ "<li><a href=\"http://localhost:8080/index/regSensorServlet\">Registra un Sensore</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/samplPlanServlet\">Configura un Piano di Campionamento</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/composeServlet\">Componi Sensori</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/getData.html\">Acquisisci Dati</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/sendCommandServlet\">Invia un Comando</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/dao.html\">Data Base</a></li>"
						+ "<li><a href=\"http://localhost:8080/index/PlatformTest.html\">Testa la Piattaforma</a></li>"
						+ "</ul></div>");
				
				//div CENTRALE: contiene dove scrivere
				out.println("<div id=\"centrale\">");
				
					//sayHello():
					out.println("<h2><p align=\"right\"><i>" + service.sayHello() + "</i></p></h2>");
					
					//viewSensor() --> TABELLA CON LA LISTA DEI SENSORI:
					Map<String, ABComponent> mList = service.getSensList();
					
					//INIZIO TABELLA ESTERNA:
					out.println("<table cellpadding=\"0\">");
					//Intestazione Tabella Esterna:
					out.println("<thead align=\"center\"><tr><th><h2>Lista dei Sensori Disponibili:</h2></th></tr></thead>");
					out.println("<tbody>");
					out.println("<tr><td valign=\"middle\">");
					
						//INIZIO TABELLA INTERNA:
						out.println("<table cellpadding=\"10\">");
						//Intestazione Tabella Interna:
						out.println("<thead align=\"center\"><tr>"
								+ "<th align=\"left\"><B>Sensor ID</B></th>"
								+ "<th><B>State</B></th>"
								+ "<th><B>Type</B></th>"
								+ "<th><B>Link<B></th>"
								+ "</tr></thead>");
						out.println("<tbody>");
						for (Entry<String, ABComponent> entry : mList.entrySet()) {
							ABComponent s = entry.getValue();
							//FORM PER IL DETTAGLIO DEI SENSORI:
							out.println("<FORM METHOD=GET ACTION=\"http://localhost:8080/index/sensorDetailServlet\">");
							out.println("<tr><td>"+s.getID()+"</td>");
							out.println("<td align=\"center\">"+s.getState()+"</td>");
							if (s.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
								out.println("<td>Hybrid "+s.getType()+"</td>");
								out.println("<td><INPUT TYPE=\"hidden\" NAME=\"Type\" VALUE=\"Hybrid\">");
								out.println("<INPUT TYPE=\"hidden\" NAME=\"Sid\" VALUE="+s.getID()+">");
								out.println("<INPUT TYPE=\"submit\" VALUE=\"Dettaglio\"></td></tr>");
								out.println("</FORM>");
							}
							else {
								out.println("<td>Simple "+s.getType()+"</td>");
								out.println("<td><INPUT TYPE=\"hidden\" NAME=\"Type\" VALUE=\"Simple\">");
								out.println("<INPUT TYPE=\"hidden\" NAME=\"Sid\" VALUE="+s.getID()+">");
								out.println("<INPUT TYPE=\"submit\" VALUE=\"Dettaglio\"></td></tr>");
								out.println("</FORM>");								
							}
							
						
							//Codice che mi serve aggiornare il file contenente la lista dei sensori disponibili:
							try {
								FileWriter fw = new FileWriter(file,true);
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(s.getID());
								bw.newLine();
								bw.flush();
								bw.close();
							}
							catch(IOException e) {
								e.printStackTrace();
							}
						}
						out.println("</tbody>");
						out.println("</table>");
						//FINE TABELLA INTERNA
						
					out.println("</td></tr></tbody></table>");
					//FINE TABELLA ESTERNA
						
					/*
					//CREAZIONE DELLA TABELLA DEI SENSORI:
					out.println("<div id=\"tabella\">");
						out.println("<table border=\"1\">");
						//DEFINISCO LE INTESTAZIONI DELLA TABELLA --> la prima riga:
						out.println("<tr>"
								+ "<TH>Sensor ID</TH>"
								+ "<TH>Name</TH>"
								+ "<TH>Model</TH>"
								+ "<TH>Type</TH>"
								+ "<TH>Description</TH>"
								+ "<TH>State</TH>"
								+ "<TH>Nature</TH>"
								+ "<TH>Capabilities</TH>"
								+ "<TH>Net Parameters</TH>"
								+ "<TH>Position</TH>"
								+ "<TH>Input List</TH>"
								+ "<TH>Output List</TH>"
								+ "<TH>Refer To Hybrids</TH></tr>");
						//RIEMPIMENTO DELLA TABELLA:
						for (Entry<String, ABComponent> entry : mList.entrySet()) {
							Sensor s = (Sensor) entry.getValue();
							out.println("<tr>"
									+ "<TD>"+ s.getID() +"</TD>"
									+ "<TD>"+ s.getName()+"</TD>"
									+ "<TD>"+ s.getModel()+"</TD>"
									+ "<TD>"+ s.getType()+"</TD>"
									+ "<TD>"+ s.getDescription()+"</TD>"
									+ "<TD>"+ s.getState()+"</TD>"
									+ "<TD>"+ s.getNature()+"</TD>");
							
							//out.println("<TD>"+ s.getCapabilities()+"</TD>");
							out.println("<TD>");
							Map<String, List<String>> capabilities = s.getCapabilities();
							if (capabilities.isEmpty())
								out.println(s.getCapabilities());
							else {
								for (Entry<String, List<String>> capEntry : capabilities.entrySet()) {
									out.println(capEntry.getKey() + " = "+ capEntry.getValue());
								}
							}
							out.println("</TD>");
						
							//out.println("<TD>"+ s.getNetParams()+"</TD>"
							out.println("<TD>");
							Map<String, List<String>> netParam = s.getNetParams();
							if (netParam.isEmpty())
								out.println(s.getNetParams());
							else {
								for (Entry<String, List<String>> netEntry : netParam.entrySet()) {
									out.println(netEntry.getKey() + "=" + netEntry.getValue());
								}
							}
							out.println("</TD>");
							
							//out.println("<TD>"+ s.getPosition()+"</TD>"
							out.println("<TD>");
							Map<String, String> position = s.getPosition();
							if (position.isEmpty())
								out.println(s.getPosition());
							else {
								for (Entry<String, String> posEntry : position.entrySet()) {
									out.println(posEntry.getKey() + "=" + posEntry.getValue());
								}
							}
							out.println("</TD>");
							
							//out.println("<TD>"+ s.getINPUT_LIST()+"</TD>"
							out.println("<TD>");
							Map<String, List<String>> inputList = s.getINPUT_LIST();
							if (inputList.isEmpty())
								out.println(s.getINPUT_LIST());
							else {
								for (Entry<String, List<String>> inputEntry : inputList.entrySet()) {
									out.println(inputEntry.getKey() + "=" + inputEntry.getValue());
								}
							}
							out.println("</TD>");
							
							//out.println("<TD>"+ s.getOUTPUT_LIST()+"</TD>"
							out.println("<TD>");
							Map<String, List<String>> outputList = s.getOUTPUT_LIST();
							if (outputList.isEmpty())
								out.println(s.getOUTPUT_LIST());
							else {
								for (Entry<String, List<String>> outputEntry : outputList.entrySet()) {
									out.println(outputEntry.getKey() + "=" + outputEntry.getValue());
								}
							}
							out.println("</TD>");
							out.println("<TD>"+ s.getReferToHybrid()+"</TD></tr>");
							
							//Codice che mi serve aggiornare il file contenente la lista dei sensori disponibili:
							try {
								FileWriter fw = new FileWriter(file,true);
								BufferedWriter bw = new BufferedWriter(fw);
								bw.write(s.getID());
								bw.newLine();
								bw.flush();
								bw.close();
							}
							catch(IOException e) {
								e.printStackTrace();
							}
						}
					out.println("</table></div>");
					//FINE TABELLA
					*/
					
				out.println("</div>");
				//FINE div CENTRALE

				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");
				//FINE div FONDO				

			out.println("</div>");
			//FINE div TELAIO		
		
		out.println("</body></html>");
		//FINE PAGINA HTML
		
	}
}