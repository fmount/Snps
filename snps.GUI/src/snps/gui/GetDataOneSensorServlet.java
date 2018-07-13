package snps.gui;

import java.io.*;
import java.lang.String;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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

public class GetDataOneSensorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;

	//Path del file su cui leggere la lista dei sensori disponibili
	private static String path = "./AvailableSensors.txt";
	//Path del file contenente le misurazioni
	private static String dataPath = "./data.txt";
	//file che conterrà la lista dei sensori disponibili
	public File file;
	public int count;
	
	//COSTRUTTORE:	
		public GetDataOneSensorServlet (BundleContext context) {
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
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		PrintWriter out = resp.getWriter();
		
		//INIZIO PAGINA HTML:
			out.println("<HTML><HEAD>");
			
			//TITOLO PAGINA:
			out.println("<TITLE>Sensor Acquisition</TITLE>");
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
					out.println("<h2>ACQUISIZIONE DATI - SENSOR ACQUISITION:</h2>");
						
						//FORM:
						out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/getData/oneSensorServlet\">");
						//Codice per la scelta dell'id del sensore:
						out.println("<p>Scegli il sensore da cui vuoi ricevere un dato*:<BR>");					
						out.println("<select name=\"scelta\">");
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
						out.println("<BR><INPUT TYPE=SUBMIT></FORM>");
						//FINE FORM
						
						out.println("<BR><BR><BR><P><B>*NOTA:</B> Se non è presente alcun sensore vuol dire che:");
						out.println("<ol type=\"a\">");
						out.println("<li>La Lista dei Sensori Disponibili è vuota</li>");
						out.println("<li>Tutti i sensori disponibili sono distattivati (si trovano nello stato \"OFF\")</li></ol>");
						
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
		
		String idSensore = req.getParameter("scelta");
		//String mod = req.getParameter("modalita");
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
		
		//TITOLO PAGINA:
		out.println("<TITLE>Sensor Acquisition</TITLE>");
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
				
					//ACQUISIZIONE:
					out.println("<h2>ACQUISIZIONE DATI - SENSOR ACQUISITION:</h2>");
					try{
					Map<String, ABComponent> sensList = service.getSensList();
					ABComponent comp = sensList.get(idSensore);

					//Se è un sensore "Hybrid":
					if (comp.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
						out.println("<P>Acquisizione dal sensore \"Hybrid\" con ID = " + idSensore +"</P>");
						out.println("<P>");
						service.getData(idSensore, "async","none");
						try {
							File file = new File(dataPath);
							FileReader fr = new FileReader(file);
							BufferedReader br = new BufferedReader(fr);
							String line;
							count=0;
							while(true) {
								line=br.readLine();
								if(line==null)
									break;
								else count++;
							}
							br.close();
						}
						catch(IOException e) {
							System.out.println(e);
						}
						try {
							int from = count-7;
							int to = count-1;
							File file = new File(dataPath);
							FileReader fr = new FileReader(file);
							BufferedReader br = new BufferedReader(fr);
							String line;
							for (int i=0; i<count; i++) {
								line=br.readLine();
								if(line==null)
									break;
								else {
									if (i>=from&&i<to){
										out.println(line+"<BR>");
									}
									else;
								}
							}
							br.close();
						}
						catch(IOException e) {
							System.out.println(e);
						}
						out.println("<BR><BR><P><B>*NOTA:</B> La misurazione ricavata dal sensore ibrido "+idSensore+""
								+ " è ottenuta effettuando una media aritmetica tra i dati acquisiti dai sensori che lo"
								+ " compongono, cioè dai sensori con ID:");
						out.println("<ul><li>");
						out.println(idSensore.replace("_", "</li><li>"));
						out.println("</li><ul>");
					}
					//Se è un sensore "Simple":
					else {
						out.println("<P>Acquisizione dal sensore \"Simple\" con ID = " + idSensore +"</P>");
						String originalData = service.getData(idSensore, "sync","none");
						//CODICE PER LA MINIPOLAZIONE DELLA STRINGA PER VISUALIZZARE MEGLIO I DATI!
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
						out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
						out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);
						out.println("</P>");
					}
				}
				catch (Exception e) {
						out.println("<P><B>ERRORE:</B>Non è stato selezionato alcun sensore!");
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