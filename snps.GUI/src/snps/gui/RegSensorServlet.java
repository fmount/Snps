package snps.gui;

import java.io.*;
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
import org.osgi.snps.base.common.Sensor;
import org.osgi.snps.base.interfaces.iCoreInterface;
import org.osgi.snps.base.interfaces.iRegistryInterface;
import org.w3c.dom.Document;

import smlparser.Parser;

/**
 * Accepting commands in order to govern core and other bundles..
 * 
 * @author Litterio Barrica
 * 
 */

public class RegSensorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BundleContext context;
	static iCoreInterface service;
	static iRegistryInterface registryservice;
	static ServiceReference reference;
	static Parser pservice;
	
	private static String path = "./AvailableSensors.txt";
	public File file;
	
	public enum choice {
		registration, removal
	}
	
	//COSTRUTTORE:	
	public RegSensorServlet (BundleContext context) {
		this.context=context;
	}
		    
	@Override
	public void init (ServletConfig config)
			throws ServletException {
		super.init(config);
	}
		
	protected void doGet (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
		
		//TITOLO PAGINA:
		out.println("<TITLE>Sensor Registration/Removal</TITLE>");
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
				
				out.println("<h2>REGISTRAZIONE SENSORE:</h2>");
				out.println("<P>Permette la registrazione di una Base Station all'interno della piattaforma.");
				out.println("<BR>Necessita in \"input\" del file XML contenente i dati della BS.</p>");
				out.println("<P>Scegli il file da cui prelevare le informazioni relative ai sensori che vuoi registrare:<BR>");
				out.println("<FORM METHOD=POST ACTION=\"/index/regSensorServlet\">");
					out.println("<INPUT TYPE=\"FILE\" NAME=\"nome\"><P>");
					out.println("<INPUT TYPE=\"hidden\" NAME=\"command\" VALUE=\"registration\">");
					out.println("<INPUT TYPE=SUBMIT>");
				out.println("</FORM>");
				
				out.println("<BR><P><h2>RIMOZIONE SENSORE:</h2></P>");
				out.println("<P>Questa funzione permette di eliminare dal Data Base del sistema un determinato sensore.");
				out.println("<BR><B>NOTA:</B> Puoi eliminare dal Data Base solamente i sensori che hai inserito!</p>");
				out.println("<P>Selezione il sensore che vuoi eliminare:<BR>");
				out.println("<FORM METHOD=POST ACTION=\"/index/regSensorServlet\">");
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
					out.println("<INPUT TYPE=\"hidden\" NAME=\"command\" VALUE=\"removal\">");
					out.println("<P><INPUT TYPE=SUBMIT>");
				out.println("</FORM></div>");
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
		PrintWriter out = resp.getWriter();
		String command, nomeFile, key;
		command = req.getParameter("command");
		nomeFile = req.getParameter("nome");
		key = req.getParameter("key");
		
		//INIZIO PAGINA HTML:
		out.println("<HTML><HEAD>");
		
		//TITOLO PAGINA:
		out.println("<TITLE>Sensor Registration - "+command+"</TITLE>");
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
				
				switch (choice.valueOf(command)) {
				
				case registration:
					out.println("<h2>REGISTRAZIONE SENSORE:</h2>");
					Document description;
					reference = context.getServiceReference(Parser.class.getName());
					ABComponent sens;
					//Controllo sull'estensione del file:
					if (nomeFile.endsWith(".xml")) {
						//out.println("Il file ha la giusta estensione!");
						try {
							if (reference != null) {
								pservice = (Parser) context.getService(reference);
								System.out.println(pservice);
								description = pservice.getDocument(nomeFile);
								sens = pservice.parse(description);
								reference = context.getServiceReference(iCoreInterface.class.getName());
								service = (iCoreInterface) context.getService(reference);
								String ack = service.regCall("persist", 3, sens.getID(),description, sens, sens.getNature(), null);
								if(ack.equals("true")) {
									out.println("<P><B>CONGRATULAZIONI!!!</B>");
									out.println("<BR>Il sensore con ID = "+sens.getID()+" � stato registrato correttamente "
											+ "sia nella Lista dei Sensori Disponibili, sia nel Data Base del sistema!");
								}
								else {
									out.println("<P><B>ATTENZIONE!!!</B>");
									out.println("<BR>Il sensore con ID = "+sens.getID()+" era gi� presente nel Data Base del "
											+ "sistema!<BR>Il sensore � stato quindi inserito nella Lista dei Sensori Disponibili.");
								}
								out.println("<P>Torna alla Home per verificare l'inserimento del nuovo sensore!");
								aggiornaLista();
							}
							else out.println("<B>Error getting Parser service!</B>");
						} catch (Exception e) {
							e.printStackTrace();
							out.println("<B>ERRORE:</B> il file che si sta cercando di caricare non � compatibile!");
						}
					}
					else { 
						out.println("<B>ERRORE:</B> il file che si sta cercando di caricare ha una estensione non compatibile!");
						out.println("<br>Si prega di caricare i dati utilizzando un file con estensione \".XML\"");
					}
					break;
			
				case removal:
					out.println("<h2>RIMOZIONE SENSORE:</h2>");
					reference = context.getServiceReference(iRegistryInterface.class.getName());
					registryservice = (iRegistryInterface) context.getService(reference);
					Map<String, ABComponent> sensList = service.getSensList();
					ABComponent s = sensList.get(key);
					if (s.getClass().getSimpleName().equalsIgnoreCase("senshybrid")) {
						if (String.valueOf(registryservice.removeComponent(3,key)).equals("true"))
								out.println("<P>Congratulazioni, hai eliminato dal Data Base il sensore \"Hybrid\" "+s.getID()+"!");
						else out.println("<P><B>ERRORE:Spiacente, ma l'eliminazione non ha avuto successo!");
					}
					else{
						//Elimino le info ad esso associate..
						registryservice.removeEntry(3, key);
						registryservice.removeBaseStation(3, ((Sensor)s).getNetParams().get("base_station").get(0));
						registryservice.removeZone(3, ((Sensor)s).getNetParams().get("zone").get(0));
						//Rimuovo il componente..
						if(String.valueOf(registryservice.removeComponent(3,key)).equals("true"))					
							out.println("<P>Congratulazioni, hai eliminato dal Data Base il sensore \"Simple\" "+s.getID()+"!");
						else out.println("<P><B>ERRORE:Spiacente, ma l'eliminazione non ha avuto successo!");
					}
					sensList.remove(key);
					aggiornaLista();
					//La stringa nomeFile contiene il Nome del file con l'estensione!
					break;
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