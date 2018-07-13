package snps.gui;

import java.lang.String;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.snps.base.common.*;
import org.osgi.snps.base.interfaces.*;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class PlatformTestServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	public BundleContext context;
	static iCoreInterface service;
	public int l,t;
	private static String path = "./AvailableSensors.txt";
	public File file;

	//COSTRUTTORE:	
	public PlatformTestServlet (BundleContext context) {
		this.context=context;
	}
	    
	@Override
	public void init (ServletConfig config) 
			throws ServletException {
		super.init(config);
	}
	
	//Metodo POST:
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
		out.println("<TITLE>Platform Test</TITLE>");
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
						+ "<center><h2 center>MENU</h2></center> "
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
				out.println("<h2>TEST DELLA PIATTAFORMA:</h2>");
				try {	
					l = Integer.parseInt(req.getParameter("limit"));
					t = Integer.parseInt(req.getParameter("time"));
				
					//BuildSensor(100):
					for (int i = 0; i < 100; i++) {
						String key = "test" + i;
						Sensor value = new Sensor(key, "test", "test", "test", "on",
								"test", "temperature", new HashMap<String, List<String>>(),
								new HashMap<String, List<String>>(),
								new HashMap<String, List<String>>(),
								new HashMap<String, List<String>>(),
								new HashMap<String, String>());
						service.addToSensList(key, value);
					}
					if(l>100) {
						out.println("<b>ERRORE</B>:Hai inserito un valore di \"limit\" non corretto!!<BR>");
						out.println("Torna indietro ed inserisci un valore compreso tra 1 e 100!!");
					}
					else {
						out.println("Stai testando la piattaforma acquisendo dati da "+l+" sensori<BR>");
						Random random = new Random();
						int size = l-1;
						int k,j=0;
						for (int i=0; i<=size; i++) {
							String key = service.getSensList().get("test" + random.nextInt(l)).getID();
							String originalData = service.getData(key, "sync", "none");
							
							//CODICE PER LA MINIPOLAZIONE DELLA STRINGA PER VISUALIZZARE MEGLIO I DATI!
							//out.println("La stringa originale è:<BR>" +originalData);
							String newData = originalData.replace("\":\"", "\",\"");
							newData = newData.replace("{\"", "");
							newData = newData.replace("\"}", "");
							String[] array = newData.split("\",\"");
							String[] newArray = new String[array.length/2];
							for (k=1; k<=array.length; j++){
								newArray[j]=array[k];
								k=k+2;
							}
							j=0;
							
							//CODICE PER LA STAMPA DEI DATI:
							//out.println("La nuova stringa formattata è:\n");
							out.println("<BR><strong>Sensor ID</strong> = "+newArray[j]); j++;
							out.println("<BR><strong>Data</strong> = "+newArray[j]); j++;
							out.println("<BR><strong>Date Ref</strong> = "+newArray[j]); j++;
							out.println("<BR><strong>Date Received</strong> = "+newArray[j]); j++;
							out.println("<BR><strong>Time</strong> = "+newArray[j]); j++;
							out.println("<BR><strong>ID_MEAS</strong> = "+newArray[j]);
							out.println("<BR>");
							j=0;
							try {
								Thread.sleep(t);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					
				}
				catch (Exception e) {
					out.println("<P><B>ERRORE</B>: Non è stato inserito nessun valore nel campo \"Rate\" o \"Limit\", oppure "
								+ "i valori inseriti non sono corretti!");
				}
				aggiornaLista();
				out.println("<div>");
				//FINE div CENTRALE

				out.println("<div id=\"fondo\"><p>&copy Litterio Barrica</p></div>");
				//FINE div FONDO:

			out.println("</div>");
			//FINE div TELAIO
		
		out.println("</body></html>");
		//FINE PAGINA HTML
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
