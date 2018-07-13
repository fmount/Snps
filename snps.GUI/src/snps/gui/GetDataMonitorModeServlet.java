package snps.gui;

import java.lang.String;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class GetDataMonitorModeServlet extends HttpServlet {
	private static final long serialVersionUID = 3L;
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
		public GetDataMonitorModeServlet (BundleContext context) {
			this.context=context;
		}
	    
		@Override
		public void init (ServletConfig config) 
				throws ServletException {
			super.init(config);
		}
	
	//Metodo Get:
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
		out.println("<TITLE>Sensor Monitoring</TITLE>");
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
				out.println("<h2>ACQUISIZIONE DATI - SENSOR MONITORING:</h2>");
				
					//FORM:
					out.println("<FORM METHOD=POST ACTION=\"http://localhost:8080/index/getData/monitorModeServlet\">");
					//Scelta del sensore:
					out.println("<P>Scegli il sensore che vuoi monitorare*:<BR>");					
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
					//Scelta delle impostazioni:
					out.println("<P>Scegli il \"rate\" del monitoraggio (ms) = <INPUT TYPE=\"TEXT\" NAME=\"rate\"></P>");
					out.println("<P>Scegli la \"durata\" del monitoraggio (ms) = <INPUT TYPE=\"TEXT\" NAME=\"finish\"></P>");
					out.println("<P><INPUT TYPE=SUBMIT></FORM></P>");
					
					out.println("<P><B>NB: IL TEMPO DI ATTESA PER LA RISPOSTA DEL SISTEMA E' PARI ALLA \"durata\" DEL "
							+ "MONITORAGGIO; SI CONSIGLIA DUNQUE DI IMPOSTARE COME DURATA AL MASSIMO 10000ms PARI A 10sec!</B></P>");
					
					out.println("<BR><BR><BR><P><B>*NOTA:</B> Se non è presente alcun sensore vuol dire che:");
					out.println("<ol type=\"a\">");
					out.println("<li>La Lista dei Sensori Disponibili è vuota</li>");
					out.println("<li>Tutti i sensori disponibili sono distattivati (si trovano nello stato \"OFF\")</li>");
					
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");
			//FINE div TELAIO
			
		out.println("</body></html>");
		//FINE PAGINA HTML
	}
	
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
		out.println("<TITLE>Sensor Monitoring</TITLE>");
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
				out.println("<h2>ACQUISIZIONE DATI - SENSOR MONITORING:</h2>");
					String idSensore = req.getParameter("scelta");
					try {
						if (idSensore.equals("")) out.println("<P><B>ERRORE:</B> Non hai selezionato nessun sensore!");
						else {
							int rate,finish,sample,k,j=0;
							try {
								rate = Integer.parseInt(req.getParameter("rate"));
								finish = Integer.parseInt(req.getParameter("finish"));
								sample = (int) Math.ceil((double)finish/rate);
								Map<String, ABComponent> sensList = service.getSensList();
								ABComponent comp = sensList.get(idSensore);
					
								//Se è un sensore "Hybrid":
								if (comp.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
									out.println("<P>Monitoring del sensore \"Hybrid\" con ID = " + idSensore +"</P>");
									File dataFile = new File(dataPath);
									for (int i = 0; i<sample; i++) {
										out.println("<P>");
										service.getData(idSensore, "async","none");
										try {
											Thread.sleep(rate);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										try {
											FileReader fr = new FileReader(dataFile);
											BufferedReader br = new BufferedReader(fr);
											String dataLine;
											count=0;
											while(true) {
												dataLine=br.readLine();
												if(dataLine==null)
													break;
												else count++;
											}
											br.close();
										}
										catch(IOException e) {
											out.println(e);
										}
										try {
											int from = count-7;
											int to = count-1;
											FileReader fr = new FileReader(dataFile);
											BufferedReader br = new BufferedReader(fr);
											String dataLine;
											for (int n=0; n<count; n++) {
												dataLine=br.readLine();
												if(dataLine==null)
													break;
												else {
													if (n>=from&&n<to){
														out.println(dataLine+"<BR>");
													}
													else;
												}	
											}
											br.close();
										}
										catch(IOException e) {
											out.println(e);
										}
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
									out.println("<P>Monitoring del sensore \"Simple\" con ID = " + idSensore +"</P>");
									for (int i = 0; i<sample; i++) {
										String key = service.getSensList().get(idSensore).getID();
										String originalData = service.getData(key, "sync","none");
										//CODICE PER LA MINIPOLAZIONE DELLA STRINGA PER VISUALIZZARE MEGLIO I DATI!
										String newData = originalData.replace("\":\"", "\",\"");
										newData = newData.replace("{\"", "");
										newData = newData.replace("\"}", "");
										String[] array = newData.split("\",\"");
										String[] newArray = new String[array.length/2];
										for (k=1; k<array.length; j++){
											newArray[j]=array[k];
											k=k+2;
										}
										k=j=0;
										out.println("<P><strong>Sensor ID</strong> = "+newArray[j]); j++;
										out.println("<BR><strong>Data</strong> = "+newArray[j]); j++;
										out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
										out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
										out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
										out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);j=0;
										out.println("</P>");
										try {
											Thread.sleep(rate);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}
							catch (Exception e) {
								out.println("<P><B>ERRORE</B>: Non è stato inserito nessun valore nel campo \"rate\" o \"durata\", oppure "
										+ "i valori inseriti non sono corretti!");
							}
						}
					}
					catch (Exception e) {
						out.println("<P><B>ERRORE</B>: Non è stato selezionato nessun sensore!");
					}
				out.println("</div>");
				//FINE div CENTRALE

				//div FONDO:
				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");

			out.println("</div>");
			
		out.println("</body></html>");
		//FINE PAGINA HTML
	}
}