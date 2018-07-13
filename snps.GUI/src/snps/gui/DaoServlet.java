package snps.gui;

import java.lang.String;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
import org.w3c.dom.Document;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class DaoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;
	static iRegistryInterface registryservice;
	static ServiceReference serviceRef;
	
	public String title, command, testa; 
	
	//Path del file su cui leggere la lista dei sensori disponibili
	private static String path = "./AvailableSensors.txt";
	//file che conterrà la lista dei sensori disponibili
	public File file;
	
	public enum choice {
		sensorsbyzone, sensorsbynode, detection, detectbydate, 
		detectbytime, detectbyzone, 
		//detectbydateandtime, removeComponent
	}
	
	public enum registrycommands {
		getSensorbyzone, getSensorbynode, detection, detectbydate, 
		detectbytime, detectbyzone, 
		//detectbydateandtime, removeComponent
		//persist, image, check, updateComponent, getSensor, getSDesc,
		//getAllSensors, sensType, history, allhistory, getSensInfo, getsenslist, 
		//getzonelist, inszone, insbs, rmvzone, rmvbs, getbsinfo, getzoneinfo, 
		//insertentry, rmventry, updateentry, getnodelist
	}
	
	//COSTRUTTORE:	
	public DaoServlet (BundleContext context) {
		this.context=context;
	}
	    
	@Override
	public void init (ServletConfig config) 
			throws ServletException {
		super.init(config);
	}
		
	//METODO GET:
	protected void doGet (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String scelta = req.getParameter("scelta");
		
		switch (choice.valueOf(scelta)) {
		
		case sensorsbyzone:
			title = "Get Sensors by Zone";
			testa = "Ricerca sensori per zona";
			command = "getSensorbyzone";
			break;
		case sensorsbynode:
			title = "Get Sensors by Node";
			testa = "Ricerca sensori per Base Station";
			command = "getSensorbynode";
			break;
		case detection:
			title = "Get Detection";
			testa = "Ricerca una misurazione";
			command ="detection";
			break;
		case detectbydate:
			title = "Get Detections by Date";
			testa = "Ricerca misurazioni per data";
			command = "detectbydate";
			break;
		case detectbytime:
			title = "Get Detections by Time";
			testa = "Ricerca misurazioni per intervallo di tempo";
			command = "detectbytime";
			break;
		case detectbyzone:
			title = "Get Detections by Zone";
			testa = "Ricerca misurazioni per zona";
			command = "detectbyzone";
			break;
			/*
		case detectbydateandtime:
			title = "Detect by Date & Time";
			command = "detectbydateandtime";
			break;
			
		case removeComponent:
			title = "Remove Component";
			command = "removeComponent";
			break;
			*/
		}
	
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
			
		//TITOLO PAGINA:
		out.println("<TITLE>DAO - "+title+"</TITLE>");
		//METADATI:
		out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-15\"/>");
		out.println("<meta http-equiv=\"content-script-type\" content=\"text/javascript\" />");
		out.println("<meta http-equiv=\"content-style-type\" content=\"text/css\" />");
		out.println("<meta http-equiv=\"imagetoolbar\" content=\"no\" />");
		out.println("<meta name=\"language\" content=\"italian-it\" />");
		
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
		
		//INIZIO BODY:
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
					out.println("<h2>DATA BASE - "+testa+":</h2>");
					//out.println(scelta);
					
					switch (choice.valueOf(scelta)) {
					
					case sensorsbyzone:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione permette di conoscere tutti i sensori registrati all'interno del Data Base del sistema in funzione "
								+ "della Zona di appartenenza, specificata tramite il suo ID.");
						
						out.println("<P>Inserisci l'ID della Zona:<BR>");
						out.println("Zone ID = <INPUT TYPE=\"TEXT\" NAME=\"zoneid\">");
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
						
					case sensorsbynode:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione permette di conoscere tutti i sensori registrati all'interno del Data Base del sistema in funzione "
								+ "della Base Station di appartenenza, specificata tramite il suo ID.");
						
						out.println("<P>Inserisci l'ID della Base Station:<BR>");
						out.println("Base Station ID = <INPUT TYPE=\"TEXT\" NAME=\"bsid\">");
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
						
					case detection:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione permette di risalire ad una misurazione registrata all'interno del Data Base del sistema in funzione "
								+ "del suo ID.");
						
						out.println("<P>Inserisci l'ID della misura:<BR>");
						out.println("Measure ID = <INPUT TYPE=\"TEXT\" NAME=\"id_meas\"><P>");
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
						
					case detectbydate:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione richiede al Data Base del sistema tutte le misure effettuate da un determinato "
								+ "sensore (selezionabile tramite il suo ID) in un determinato periodo.");
						
						out.println("<P>Inserisci l'ID del sensore:<BR>");
						out.println("Sensor ID = <INPUT TYPE=\"TEXT\" NAME=\"key\">");
						
						out.println("<P>Inserisci il periodo:<BR>");
						out.println("Data Inizio:<BR>");
						out.println("<TABLE CELLSPACING=\"5\"><TR>");
						out.println("<TD>Giorno: <SELECT NAME=\"g1\">");
						for (int i=1; i<32; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Mese: <SELECT NAME=\"m1\">");
						for (int i=1; i<13; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Anno: <INPUT TYPE=\"TEXT\" NAME=\"a1\" MAXLENGTH=\"4\" SIZE=\"4\"></TD>");
						out.println("</TR></TABLE>");
						out.println("Data Fine:<BR>");
						out.println("<TABLE CELLSPACING=\"5\"><TR>");
						out.println("<TD>Giorno: <SELECT NAME=\"g2\">");
						for (int i=1; i<32; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Mese: <SELECT NAME=\"m2\">");
						for (int i=1; i<13; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Anno: <INPUT TYPE=\"TEXT\" NAME=\"a2\" MAXLENGTH=\"4\" SIZE=\"4\"></TD>");
						out.println("</TR></TABLE>");
						/*
						//ALTRIMENTI (Funziona solo con Google Chrome):
						out.println("<LABEL>Data Inizio = <INPUT TYPE=\"date\" NAME=\"date1\"></LABEL><BR>");
						out.println("<LABEL>Data Fine = <INPUT TYPE=\"date\" NAME=\"date2\"></LABEL><BR>");
						*/
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
						
					case detectbytime:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione richiede al Data Base del sistema tutte le misure effettuate da un determinato sensore "
								+ "(selezionabile tramite il suo ID) in un determinato giorno ed in un determinato intervallo di tempo.");
						
						out.println("<P>Inserisci l'ID del sensore:<BR>");
						out.println("Sensor ID = <INPUT TYPE=\"TEXT\" NAME=\"key\">");
						out.println("<P>Inserisci la data d'interesse:<BR>");
						out.println("<TABLE CELLSPACING=\"5\"><TR>");
						out.println("<TD>Giorno: <SELECT NAME=\"g1\">");
						for (int i=1; i<32; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Mese: <SELECT NAME=\"m1\">");
						for (int i=1; i<13; i++) {
							out.println("<OPTION VALUE=\""+i+"\">"+i+"</OPTION>");
						}
						out.println("</SELECT></TD>");
						out.println("<TD>Anno: <INPUT TYPE=\"TEXT\" NAME=\"a1\" MAXLENGTH=\"4\" SIZE=\"4\"></TD>");
						out.println("</TR></TABLE>");
						/*
						//ALTRIMENTI (Funziona solo con Google Chrome):
						out.println("<LABEL>Data = <INPUT TYPE=\"date\" NAME=\"date\"></LABEL><BR>");
						*/
						out.println("<P>Inserisci l'orario:<BR>");
						out.println("<LABEL>Ora inizio (hh:mm) = <INPUT TYPE=\"TEXT\" NAME=\"time1\" MAXLENGTH=\"5\" SIZE=\"5\"></LABEL><BR>");
						out.println("<LABEL>Ora fine (hh:mm) = <INPUT TYPE=\"TEXT\" NAME=\"time2\" MAXLENGTH=\"5\" SIZE=\"5\"></LABEL><BR>");
						/*
						//ALTRIMENTI (Funziona solo con Google Chrome): 
						out.println("<LABEL>Ora inizio = <INPUT TYPE=\"time\" NAME=\"time1\"></LABEL><BR>");
						out.println("<LABEL>Ora fine = <INPUT TYPE=\"time\" NAME=\"time2\"></LABEL><BR>");
						*/
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
						
					case detectbyzone:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \""+testa+"\":");
						out.println("<P>Questa funzione richiede al Data Base del sistema tutte le misure effettuate in una determinata "
								+ "Zona selezionabile tramite il suo ID.");
						
						out.println("<P>Inserisci l'ID della zona:<BR>");
						out.println("Zone ID = <INPUT TYPE=\"TEXT\" NAME=\"zoneId\">");
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
					/*
					case detectbydateandtime:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \"Detect by Date & Time\":");
						out.println("<P>Descrizione...");
						out.println("<P>Inserisce l'ID del sensore:<BR>");
						out.println("Sensor ID = <INPUT TYPE=\"TEXT\" NAME=\"key\">");
						out.println("<P>Inserisce il periodo d'interesse:<BR>");
						out.println("<LABEL>Data Inizio = <INPUT TYPE=\"date\" NAME=\"date1\"></LABEL><BR>");
						out.println("<LABEL>Data Fine = <INPUT TYPE=\"date\" NAME=\"date2\"></LABEL><BR>");
						out.println("<P>Inserisce l'orario:<BR>");
						out.println("<LABEL>Ora inizio = <INPUT TYPE=\"time\" NAME=\"time1\"></LABEL><BR>");
						out.println("<LABEL>Ora fine = <INPUT TYPE=\"time\" NAME=\"time2\"></LABEL><BR>");
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						break;
				
					case removeComponent:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet\">");
						out.println("<P>Hai selezionato la funzione \"Remove Component\":");
						out.println("<P>Questa funzione permette di eliminare dal Data Base del sistema un determinato sensore.*");
						out.println("<P>Selezione il sensore che vuoi eliminare:<BR>");
						out.println("<select name=\"key\">");
						try {
							file = new File(path);
							FileReader fr = new FileReader(file);
							BufferedReader br = new BufferedReader(fr);
							String line;
							while(true) {
								line=br.readLine();
								if(line==null)
									break;
								out.println("<option value=\""+ line +"\">" + line + "</option>");
							}
							br.close();
						}
						catch(IOException e) {
							e.printStackTrace();
						}
						out.println("</select><BR>");
						
						out.println("<P><INPUT TYPE=SUBMIT></FORM>");
						out.println("<BR><BR><BR><P><B>*NOTA:</B> Puoi eliminare dal Data Base solamente i sensori che hai inserito!");
						break;
					*/
					}

				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");
			//FINE div TELAIO
				
		out.println("</body></html>");
		//FINE PAGINA HTML
	}

	//METODO POST:
	protected void doPost (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
		
		//TITOLO PAGINA:
		out.println("<TITLE>DAO - "+title+"</TITLE>");
		//METADATI:
		out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-15\"/>");
		out.println("<meta http-equiv=\"content-script-type\" content=\"text/javascript\" />");
		out.println("<meta http-equiv=\"content-style-type\" content=\"text/css\" />");
		out.println("<meta http-equiv=\"imagetoolbar\" content=\"no\" />");
		out.println("<meta name=\"language\" content=\"italian-it\" />");
				
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
		
		//INIZIO BODY:
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
					
					out.println("<h2>DATA BASE - "+testa+":</h2>");
					String zoneid, bsid, id_meas, key, initDate, endDate, date, initTime, endTime,
							date1, date2, initTime1, endTime2, zoneId;
					List<String> params = new ArrayList<String>();
					
					switch (registrycommands.valueOf(command)) {
					
					case getSensorbyzone:
						zoneid = req.getParameter("zoneid");
						params.add(zoneid);
						regCall(command, 3, "", null, null, "", params, out);
						break;
						
					case getSensorbynode:
						bsid = req.getParameter("bsid");
						params.add(bsid);
						regCall(command, 3, "", null, null, "", params, out);
						break;
												
					case detection:
						id_meas = req.getParameter("id_meas");
						params.add(id_meas);
						regCall(command, 3, "", null, null, "", params, out);
						break;
						
					case detectbydate:
						key = req.getParameter("key");
						initDate = req.getParameter("g1")+"-"+req.getParameter("m1")+"-"+req.getParameter("a1");
						endDate = req.getParameter("g2")+"-"+req.getParameter("m2")+"-"+req.getParameter("a2");
						/*
						//ALTRIMENTI:
						initDate = req.getParameter("date1"); 						//Data in formato aaaa-mm-gg
						String [] vect1 = date1.split("-");
						initDate = vect1[2]+"-"+vect1[1]+"-"+vect1[0];				//Data in formato gg-mm-aaaa
						endDate = req.getParameter("date2"); 						//Data in formato aaaa-mm-gg
						String [] vect2 = date2.split("-");
						endDate = vect2[2]+"-"+vect2[1]+"-"+vect2[0];				//Data in formato gg-mm-aaaa
						 */
						params.add(initDate);
						params.add(endDate);
						regCall(command, 3, key, null, null, "", params, out);
						break;
						
					case detectbytime:
						key = req.getParameter("key");
						date = req.getParameter("g1")+"-"+req.getParameter("m1")+"-"+req.getParameter("a1");
						initTime = req.getParameter("time1");
						endTime = req.getParameter("time2");
						params.add(date);
						params.add(initTime);
						params.add(endTime);
						regCall(command, 3, key, null, null, "", params, out);
						break;
						
					case detectbyzone:
						zoneId = req.getParameter("zoneId");
						params.add(zoneId);
						regCall(command, 3, "", null, null, "", params, out);
						break;
					/*
					case detectbydateandtime:
						key = req.getParameter("key");
						date1 = req.getParameter("date1");
						//vect1 = date1.split("-");
						//date1 = vect1[2]+"-"+vect1[1]+"-"+vect1[0];
						date2 = req.getParameter("date2");
						//vect2 = date2.split("-");
						//date2 = vect2[2]+"-"+vect2[1]+"-"+vect2[0];
						initTime1 = req.getParameter("time1");
						endTime2 = req.getParameter("time2");
						out.println(key+date1+date2+initTime1+endTime2);
						params.add(date1);
						params.add(date2);
						params.add(initTime1);
						params.add(endTime2);
						regCall(command, 3, key, null, null, "", params, out);
						break;
					
					case removeComponent:
						key = req.getParameter("key");
						ServiceReference reference;
						reference = context.getServiceReference(iCoreInterface.class.getName());
						service = (iCoreInterface) context.getService(reference);
						Map<String, ABComponent> sensList = service.getSensList();
						ABComponent s = sensList.get(key);
						regCall(command, 3, key, null, s, "", params, out);
						sensList.remove(key);
						aggiornaLista();
						break;
					*/
					}
					
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");				
			//FINE div TELAIO
			
		out.println("</body></html>");
		//FINE PAGINA HTML
	}
	
	
	public void regCall (String command, int opcode, String key, Document description, 
			ABComponent s, String type, List<String> params, PrintWriter out) {
		try {
			serviceRef = context.getServiceReference(iRegistryInterface.class.getName());
			registryservice = (iRegistryInterface) context.getService(serviceRef);

			switch (registrycommands.valueOf(command)) {
			
			case getSensorbyzone:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String zoneid = params.get(0);
				List<String> sbz = registryservice.getSensorBYZone(opcode, zoneid);
				if (sbz.isEmpty()) out.println("<P>Il Data Base non contiene alcun sensore appartenente alla zona "+zoneid+"!");
				else {
					out.println("<P>Il Data Base contiene i seguente sensori appartenenti alla zona "+zoneid+":");
					out.println("<ul>");
					for (int i=0; i<sbz.size(); i++) {
						String original = sbz.get(i).replace("{\"sid\":\"", "");
						String[] array = original.split("\",\"");
						out.println("<li>"+array[0]+"</li>");
					}
					out.println("</ul>");
				}
				break;
				
			case getSensorbynode:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String bsid = params.get(0);
				List<String> sbn = registryservice.getSensorBYNode(opcode, bsid);
				if (sbn.isEmpty()) out.println("<P>Il Data Base non contiene alcun sensore appartenente alla BS "+bsid+"!");
				else {
					out.println("<P>Il Data Base contiene i seguente sensori appartenenti alla BS "+bsid+":");
					out.println("<ul>");
					for (int i=0; i<sbn.size(); i++) {
						String original = sbn.get(i).replace("{\"sid\":\"", "");
						String[] array = original.split("\",\"");
						out.println("<li>"+array[0]+"</li>");
					}
					out.println("</ul>");
				}
				break;
				
			case detection:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String id_meas = params.get(0);
				String originalData = registryservice.getSingleDetect(opcode, id_meas);
				String newData = originalData.replace("\":\"", "\",\"");
				newData = newData.replace("{\"", "");
				newData = newData.replace("\"}", "");
				String[] array = newData.split("\",\"");
				String[] newArray = new String[array.length/2];
				int i,j=0;
				
				for (i=1; i<=array.length; j++){
					newArray[j]=array[i];
					i=i+2;
					}
				j=0;
				out.println("<P><strong>Sensor ID</strong> = "+newArray[j]); j++;
				out.println("<BR><strong>Data</strong> = "+newArray[j]); j++;
				//out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
				out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
				out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
				out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);
				out.println("</P>");
				break;
				
			case detectbydate:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String initDate = params.get(0);
				String endDate = params.get(1);
				//out.println(initDate+endDate);
				List<String> dbd = registryservice.getDetectionByDate(opcode, key, initDate, endDate);
				if (dbd.isEmpty()) out.println("<P>Spiacente, nessuna misurazione è stata effettuata dal sensore "+key+" tra il "+initDate+" ed il "+endDate+"!");
				else {
					out.println("<P>Tra il "+initDate+" ed il "+endDate+" il sensore "+key+" ha effettuato le seguenti misurazioni:");
					for (int k=0; k<dbd.size(); k++) {
						originalData = dbd.get(k);
						newData = originalData.replace("\":\"", "\",\"");
						newData = newData.replace("{\"", "");
						newData = newData.replace("\"}", "");
						array = newData.split("\",\"");
						newArray = new String[array.length/2];
						j=0;
						for (i=1; i<=array.length; j++){
							newArray[j]=array[i];
							i=i+2;
							}
						j=0;
						out.println("<P><strong>Sensor ID</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Data</strong> = "+newArray[j]); j++;
						//out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);
						out.println("</P>");
					}
				}
				break;
			
			case detectbytime:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String date = params.get(0);
				String initTime = params.get(1);
				String endTime = params.get(2);
				List<String> dbt = registryservice.getDetectionByTime(opcode, key, date, initTime, endTime);
				if (dbt.isEmpty()) out.println("<P>Spiacente, nessuna misurazione è stata effettuata dal sensore "+key+" in data "+date+""
						+ " tra le "+initTime+" e le "+endTime+"!");
				else {
					out.println("<P>Il sensore "+key+" in data "+date+" tra le ore "+initTime+" e le ore "+endTime+" ha effettuato le seguenti misurazioni:");
					for (int k=0; k<dbt.size(); k++) {
						originalData = dbt.get(k);
						newData = originalData.replace("\":\"", "\",\"");
						newData = newData.replace("{\"", "");
						newData = newData.replace("\"}", "");
						array = newData.split("\",\"");
						newArray = new String[array.length/2];
						j=0;
						for (i=1; i<=array.length; j++){
							newArray[j]=array[i];
							i=i+2;
							}
						j=0;
						out.println("<P><strong>Sensor ID</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Data</strong> = "+newArray[j]); j++;
						//out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);
						out.println("</P>");
					}
				}
				break;
				
			case detectbyzone:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String zoneId = params.get(0);
				String originalDBZ = registryservice.getDetectionByZone(opcode, zoneId);
				if (originalDBZ.equals("[]")) out.println("<P>Spiacente, non è stata effettuate nessuna misurazione nella zona "+zoneId+"!");
				else {
					out.println("<P>Nella zona "+zoneId+" sono state effettuate misurazioni dai seguenti sensori:");
					String newDBZ = originalDBZ.replace("\":\"", "!");
					newDBZ = newDBZ.replace("\",\"", "!");
					newDBZ = newDBZ.replace("\\", "");
					newDBZ = newDBZ.replace("\"", "");
					newDBZ = newDBZ.replace("[{", "[");
					newDBZ = newDBZ.replace("},{", "],[");
					newDBZ = newDBZ.replace("}]", "]");
					newDBZ = newDBZ.replace("{", "");
					newDBZ = newDBZ.replace("}", "");
					String [] dbz = newDBZ.split("!");
					out.println("<ul>");
					for (int i1=0; i1<dbz.length; i1++){
						String Sid = dbz[i1];
						i1++;
						if (dbz[i1].equals("[]"));
						else {
							String detections = dbz[i1];
							out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/daoServlet/detectionsByZoneDetails\">");
							out.println("<li>"+Sid);
							out.println("<INPUT TYPE=\"hidden\" NAME=\"Sid\" VALUE="+Sid+">");
							out.println("<INPUT TYPE=\"hidden\" NAME=\"detections\" VALUE="+detections+">");
							out.println("<INPUT TYPE=\"submit\" VALUE=\"Dettaglio\"></li>");
							out.println("</FORM>");
						}
					}
				}
				break;
			/*	
			case detectbydateandtime:
				if(params==null){
					out.println("<B>Error processing dates</B>!");
					break;
				}
				String date1 = params.get(0);
				String date2 = params.get(1);
				String initTime1 = params.get(2);
				String endTime2 = params.get(3);
				List<String> dbdt = registryservice.getDetectionByDateAndTime(opcode, key,date1, date2, initTime1, endTime2);
				break;
			
			case removeComponent:
				if (s.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
					if (String.valueOf(registryservice.removeComponent(opcode,key)).equals("true"))
							out.println("<P>Congratulazioni, hai eliminato dal Data Base il sensore \"Hybrid\" "+s.getID()+"!");
					else out.println("<P><B>ERRORE:Spiacente, ma l'eliminazione non ha avuto successo!");
				}
				else{
					//Elimino le info ad esso associate..
					registryservice.removeEntry(opcode, key);
					registryservice.removeBaseStation(opcode, ((Sensor)s).getNetParams().get("base_station").get(0));
					registryservice.removeZone(opcode, ((Sensor)s).getNetParams().get("zone").get(0));
					//Rimuovo il componente..
					if(String.valueOf(registryservice.removeComponent(opcode,key)).equals("true"))					
						out.println("<P>Congratulazioni, hai eliminato dal Data Base il sensore \"Simple\" "+s.getID()+"!");
					else out.println("<P><B>ERRORE:Spiacente, ma l'eliminazione non ha avuto successo!");
				}
				break;
			*/
			}
		} catch (Exception e) {
			out.println("<P><B>Alert -> Command or Parameters not found!</B>");
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	
	public void aggiornaLista() {
		//AGGIORNO LISTA SENSORI:
		try {
			file = new File(path);
			if (file.exists())
				file.delete();
			file.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, ABComponent> mList = service.getSensList();
		for (Entry<String, ABComponent> entry : mList.entrySet()) {
			ABComponent comp = entry.getValue();
			try {
				file = new File(path);
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(comp.getID());
				bw.newLine();
				bw.flush();
				bw.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
