package snps.gui;

import java.io.*;
import java.lang.String;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.ABComponent;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.*;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class SendCommandServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;

	//Path del file su cui leggere la lista dei sensori disponibili
	private static String path = "./AvailableSensors.txt";
	//file che conterrà la lista dei sensori disponibili
	public File file;
	
	//COSTRUTTORE:	
	public SendCommandServlet (BundleContext context) {
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
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
			
		//TITOLO PAGINA:
		out.println("<TITLE>Sending Command</TITLE>");
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
					out.println("<h2>INVIA UN COMANDO:</h2>");
					out.println("<P>Permette di inviare ai sensori selezionati i comandi \"enable\" e \"disable\" rispettivamente "
							+ "per l'attivazione e la disattivazione degli stessi.");
					out.println("<BR>Per selezionare più sensori, tenere premuto il tasto \"Shift\" o \"Ctrl\" e cliccare col mouse "
							+ "sui sensori desiderati.<BR>");
						
					//FORM:
					out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/sendCommandServlet\">");
					//Codice per la scelta dell'id del sensore:
					out.println("<P>Scegli il/i sensore/i a cui vuoi inviare un comando:<BR>");					
					out.println("<select multiple name=\"scelta\">");
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
						
					out.println("<P>Scegli il comando da inviare:<BR>");
					out.println("<select name=\"command\">");
						out.println("<option value=\"enable\">Enable</option>");
						out.println("<option value=\"disable\">Disable</option>");
					out.println("</select><BR>");
					out.println("<P><INPUT TYPE=SUBMIT></FORM>");
					//FINE FORM
					
					out.println("<BR><BR><BR><P><B>NOTA:</B>");
					out.println("<BR>- L'invio del comando \"enable\" ad un sensore \"Hybrid\" comporta l'attivazione "
							+ "dei sensori \"Simple\" che lo compongono");
					out.println("<BR>- L'invio del comando \"disable\" ad un sensore \"Simple\" comporta la disattivazione "
							+ "dei sensori \"Hybrid\" che lo contengono");
						
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");
			//FINE div TELAIO
				
		out.println("</body></html>");
		//FINE PAGINA HTML
	}

	
	//METODO POST
	protected void doPost (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		PrintWriter out = resp.getWriter();
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
		
		//TITOLO PAGINA:
		out.println("<TITLE>Sending Command</TITLE>");
		//METADATI:
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
				out.println("<h2>INVIA UN COMANDO:</h2>");
				String[] sensor = req.getParameterValues("scelta");
				try {
					int size = sensor.length;
					String command = req.getParameter("command");
					List<String> Ids = new ArrayList<String>();
					out.println("<P>Hai inviato il comando \""+command+"\" ai seguenti sensori:<ul>");
					for (int i=0; i<size; i++) {
						out.println("<li>"+sensor[i]+"</li>");
					}
					out.println("</ul><br>");
					if (command.equals("enable")){
						for (int i=0; i<sensor.length; i++) {
							Ids.add(sensor[i]);
							String risposta = service.interprCall(command, null, Ids, "sync");
							out.println("<P><B>Risposta del sensore \""+sensor[i]+"\":</B>");
							if(risposta.equals("true"))
								out.println("<BR><i>Sono passato allo stato \"enable\"!</i>");
							else out.println("<BR><B><i>ERRORE:</B> Mi trovo già nello stato \"enable\"!</i>");
							Ids.clear();
						}
					}
					else if (command.equals("disable")) {
						for (int i=0; i<sensor.length; i++) {
							Ids.add(sensor[i]);
							String risposta = service.interprCall(command, null, Ids, "sync");
							out.println("<P><B>Risposta del sensore \""+sensor[i]+"\":</B>");
							if(risposta.equals("true"))
								out.println("<BR><i>Sono passato allo stato \"disable\"!</i>");
							else out.println("<BR><B><i>ERRORE:</B> Mi trovo già nello stato \"disable\"!</i>");
							Ids.clear();

							//Questo codice serve per evitare che, nel caso stia disattivando un sensore "Hybrid", 
							//vengano disattivati anche i sensori che lo compongono!
							if(sensor[i].contains("_")) {
								//Mi creo un vettore costituito da Sensori "Simple":
								String [] hSens = sensor[i].split("_");
								for (int j=0; j<hSens.length; j++) {
									Map<String, ABComponent> sensList = service.getSensList();
									Sensor simpleSensor = (Sensor)sensList.get(hSens[j]);
									simpleSensor.setState("on");
								}
							}
							
							//Questo codice serve ne caso in cui stia disattivando un sensore "Simple" e quindi bisogna
							//disattivare pure tutti i sensori "Hybrid" che lo contengono!
							else {
								try {
									file = new File(path);
									FileReader fr = new FileReader(file);
									BufferedReader br = new BufferedReader(fr);
									String line;
									while(true) {
										line=br.readLine();
										if(line==null)
											break;
										else if (line.contains(sensor[i])&&line.contains("_")) {
											Map<String, ABComponent> sensList = service.getSensList();
											ABComponent comp = sensList.get(line);
											comp.setState("off");
										}
									}
									br.close();
								}
								catch(IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				catch (Exception e) {
					out.println("<BR><B><i>ERRORE:</B> Non hai selezionato nessun sensore, riprova!</i>");
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
}