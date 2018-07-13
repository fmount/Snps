package snps.gui;

import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
import org.osgi.snps.base.common.SensHybrid;
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.*;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class ComposeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;
	//Path del file su cui leggere la lista dei sensori disponibili
	private static String path = "./AvailableSensors.txt";
	//file che conterrà la lista dei sensori disponibili
	public File file;
	
	//COSTRUTTORE:	
	public ComposeServlet (BundleContext context) {
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
		out.println("<TITLE>Sensor Composition</TITLE>");
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
				out.println("<h2>COMPOSIZIONE DI SENSORI:</h2>");
				out.println("<P>La piattaforma SNPS permette di comporre tra di loro più sensori ottenendo alla fine "
						+ "un sensore \"Hybrid\" che darà in output una misura in funzione del \"Template\" scelto.");
				out.println("<BR>Di default è stato scelto come \"Template\" la media aritmetica!");
						
					//FORM:
					out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/composeServlet\">");
					//Codice per la scelta dell'id del sensore:
					out.println("<P>Scegli i sensori che vuoi comporre (minimo 2 sensori):<BR>");					
					out.println("<select multiple name=\"scelta\">");
					try {
						ServiceReference reference;
						reference = context.getServiceReference(iCoreInterface.class.getName());
						service = (iCoreInterface) context.getService(reference);
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
					
					out.println("<P>Scegli il template:<BR>");
					out.println("<select name=\"template\">");
						out.println("<option value=\"media\">Media</option>");
					out.println("</select><BR>");
					out.println("<P><INPUT TYPE=SUBMIT></FORM>");
					//FINE FORM
					
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");
			//FINE div TELAIO
			
		out.println("</body></html>");
		//FINE pag HTML
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
		out.println("<TITLE>Sensor Composition</TITLE>");
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
					out.println("<h2>COMPOSIZIONE DI SENSORI:</h2>");
					try {
						String[] sensor = req.getParameterValues("scelta");
						String command = req.getParameter("template");
						if (sensor.length<2) out.println("<B>ERRORE:</B> Inserisci almeno 2 sensori!");
						else{
							List<String> tocompose = new ArrayList<String>();
							for (int i=0; i<sensor.length; i++) {
								tocompose.add(sensor[i]);
							}
							out.println("<p><B>CONGRATULAZIONI!!!</B>");
							String SenHyId = service.composerCall("compose", tocompose, "");
							out.println("<p>Ho creato il sensore \"Hybrid\" con ID = "+SenHyId+" componendo i sensori con ID:");
							out.println("<ul><li>");
							out.println(SenHyId.replace("_", "</li><li>"));
							out.println("</li></ul>");
							out.println("<P>Questo sensore restituirà la media aritmetica dei dati acquisiti dai sensori che lo compongono!");
							tocompose.clear();
						}
					}
					catch(Exception e) {
						out.println("<P><B>ERRORE:</B> Non è stato selezionato alcun sensore. Torna indietro e riprova!");
					}
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");				
			//FINE div TELAIO
				
		out.println("</body></html>");
		//FINE PAGINA HTML
		
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
