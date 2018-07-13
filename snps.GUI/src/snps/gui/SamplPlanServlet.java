package snps.gui;

import java.lang.String;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import org.osgi.snps.base.common.SamplingPlan;
import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.*;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class SamplPlanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;

	//Path del file su cui leggere la lista dei sensori disponibili
	private static String path = "./AvailableSensors.txt";
	//file che conterrà la lista dei sensori disponibili
	public File file;
	
	//COSTRUTTORE:	
	public SamplPlanServlet (BundleContext context) {
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
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
			
		//TITOLO PAGINA:
		out.println("<TITLE>Sampling Plan</TITLE>");
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
					out.println("<h2>CONFIGURAZIONE PIANO DI CAMPIONAMENTO:</h2>");
					out.println("<P>Permette di inviare ai sensori selezionati un piano di campionamento opportunamente definito "
							+ "dall'utente.");
					out.println("<BR>Per selezionare più sensori, tenere premuto il tasto \"Shift\" o \"Ctrl\" e cliccare col mouse "
							+ "sui sensori desiderati.<BR>");
						
					//FORM:
					out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/samplPlanServlet\">");
					//Codice per la scelta dell'id del sensore:
					out.println("<P>Scegli i sensori a cui vuoi inviare un piano di campionamento:<BR>");					
					out.println("<select multiple name=\"scelta\">");
					try {
						file = new File(path);
						FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String line;
						Map<String, ABComponent> sensList = service.getSensList();
						while(true) {
							line=br.readLine();
							if(line==null)
								break;
							//Controllo per inserire solo i sensori che hanno stato ON!
							ABComponent comp = sensList.get(line);
							if (comp.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
									SensHybrid sh = (SensHybrid)sensList.get(line);
									if(sh.getState().equalsIgnoreCase("on")){
										out.println("<option value=\""+ line +"\">" + line + "</option>");
									}
									else;
							}
							else {
								Sensor s = (Sensor)sensList.get(line);
								if(s.getState().equalsIgnoreCase("on")) {
									out.println("<option value=\""+ line +"\">" + line + "</option>");
								}
								else;
							}
						}
						br.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					out.println("</select><BR>");
					
					//Codice per a scelta delle impostazioni del piano di campionamento:
					out.println("<P>Scelgi le impostazioni del piano di campionamento:<P>");
					out.println("Soglia Minima = <INPUT TYPE=\"TEXT\" NAME=\"th_min\"><P>");
					out.println("Soglia Massima = <INPUT TYPE=\"TEXT\" NAME=\"th_max\"><P>");
					out.println("Interval (ms)= <INPUT TYPE=\"TEXT\" NAME=\"interval\"><P>");
					out.println("<P><INPUT TYPE=SUBMIT></FORM>");
					//FINE FORM
					
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
		out.println("<TITLE>Sampling Plan</TITLE>");
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
				out.println("<h2>CONFIGURAZIONE PIANO DI CAMPIONAMENTO:</h2>");
				String[] sensor = req.getParameterValues("scelta");
				try {
					int size = sensor.length;
					try {
						int interval = (int) Math.ceil(Double.parseDouble(req.getParameter("interval").replace("," , ".")));
						try {
							double min = Double.parseDouble(req.getParameter("th_min").replace("," , "."));
							double max = Double.parseDouble(req.getParameter("th_max").replace("," , "."));
							if (min>=max) out.println("<P><B>ERRORE</B>: impossibile inviare il piano di campionamento desiderato "
									+ "poichè il valore della Soglia Massima è inferiore a quello della Soglia Minima!");
							else {
								List<String> ids = new ArrayList<String>();
								boolean opres = false;
								try {
									out.println("<P>Stai inviando un piano di campionamento ai seguenti sensori:");
									out.println("<ul>");
									for (int i=0; i<sensor.length; i++) {
										out.println("<li>"+sensor[i]+"</li>");
										ids.add(sensor[i]);
									}
									out.println("</ul>");
									String splanId = org.osgi.snps.base.util.Util.IdGenerator().replace("-", "");
									SamplingPlan sPlan = new SamplingPlan(splanId, ids, min, max, interval);
									opres = Boolean.parseBoolean(service.interprCall("splan", sPlan, null, ""));
								}
								catch (Exception e) {
									out.println("Exception = "+e);
									opres = false;
								}
								if (opres)
									out.println("<BR><P><B>Test Passed --></B> Congratulazioni, il piano di campionamento è stato "
											+ "correttamente inviato ai sensori selezionati!");
								else
									out.println("<P><B>ERRORE</B>: Spiacenti, si è verificato un errore nell'invio del piano di "
											+ "campionamento, riprova!");
							}
						}
						catch (Exception e) {
							if (e.toString().contains("empty String")) {
								List<String> ids = new ArrayList<String>();
								boolean opres = false;
								try {
									out.println("<P>Stai inviando un piano di campionamento ai seguenti sensori:");
									out.println("<ul>");
									for (int i=0; i<sensor.length; i++) {
										out.println("<li>"+sensor[i]+"</li>");
										ids.add(sensor[i]);
									}
									out.println("</ul>");
									String splanId = org.osgi.snps.base.util.Util.IdGenerator().replace("-", "");
									SamplingPlan sPlan = new SamplingPlan(splanId, ids, 45, 75, interval);
									opres = Boolean.parseBoolean(service.interprCall("splan", sPlan, null, ""));
								}
								catch (Exception ex) {
									out.println("Exception = "+ex);
									opres = false;
								}
								if (opres)
									out.println("<BR><P><B>Test Passed --></B> Congratulazioni, il piano di campionamento è stato "
											+ "correttamente inviato ai sensori selezionati!");
								else out.println("<P><B>ERRORE</B>: Spiacenti, si è verificato un errore nell'invio del piano di "
										+ "campionamento, riprova!");
							}
							else out.println("<P><B>ERRORE</B>: Hai inserto dei valori non corretti nei campi relativi alle soglie!");
						}
					}
					catch (Exception e) {
							out.println("<P><B>ERRORE</B>: Impossibile inviare il piano di campionamento poichè non è stato inserito "
									+ "alcun valore nel campo \"Interval\" oppure il valore inserito non è corretto!</P>");
					}
				}		
				catch (Exception e) {
					out.println("<P><B>ERRORE</B>: Impossibile inviare il piano di campionamento poichè non è stato selezionato "
							+ "alcun sensore!");
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
