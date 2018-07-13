package snps.gui;

import java.io.*;
import java.lang.String;
import java.io.IOException;
import java.io.PrintWriter;
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

public class SensorDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public File file;
	public BundleContext context;
	static iCoreInterface service;
	
//COSTRUTTORE:	
	public SensorDetailServlet (BundleContext context) {
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
		
		/*
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
		*/
		
		resp.setContentType("text/html");
		//resp.getWriter().write("Hello from the cloud!");
		ServiceReference reference;
		reference = context.getServiceReference(iCoreInterface.class.getName());
		service = (iCoreInterface) context.getService(reference);
		PrintWriter out = resp.getWriter();
		String Sid = req.getParameter("Sid");
		String Type = req.getParameter("Type");
		
	//INIZIO PAGINA HTML:		
		out.println("<HTML><HEAD>");

	//TITOLO PAGINA:		
		out.println("<TITLE>Sensor Detail</TITLE>");
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
				out.println("Dettaglio del sensore con ID = "+Sid);
				Map<String, ABComponent> sensList = service.getSensList();
				//Se è un sensore "Ibrido":
				if (Type.equalsIgnoreCase("Hybrid")) {
					SensHybrid sh = (SensHybrid)sensList.get(Sid);
					out.println("<ul>"
							+ "<li><b>Sensor ID</b> = "+ Sid +"</li>"
							+ "<li><b>Name</b> = "+ sh.getName() +"</li>"
							+ "<li><b>Model</b> = "+ sh.getModel() +"</li>"
							+ "<li><b>Type</b> = "+ sh.getType() +"</li>"
							+ "<li><b>Description</b> = "+ sh.getDescription() +"</li>"
							+ "<li><b>State</b> = "+ sh.getState() +"</li>"
							+ "<li><b>Nature</b> = "+ sh.getNature() +"</li>");
				}
				//Se è un sensore "Simple":
				else if (Type.equalsIgnoreCase("Simple")){
					Sensor s = (Sensor)sensList.get(Sid);

					out.println("<ul>"
							+ "<li><b>Sensor ID</b> = "+ Sid +"</li>"
							+ "<li><b>Name</b> = "+ s.getName() +"</li>"
							+ "<li><b>Model</b> = "+ s.getModel() +"</li>"
							+ "<li><b>Type</b> = "+ s.getType() +"</li>"
							+ "<li><b>Description</b> = "+ s.getDescription() +"</li>"
							+ "<li><b>State</b> = "+ s.getState() +"</li>"
							+ "<li><b>Nature</b> = "+ s.getNature() +"</li>");
					
					out.println("<li><b>Capabilitiese:</b><ul>");
					Map<String, List<String>> capabilities = s.getCapabilities();
					if (capabilities.isEmpty())
						out.println(s.getCapabilities());
					else {
						for (Entry<String, List<String>> capEntry : capabilities.entrySet()) {
							out.println("<li>"+capEntry.getKey()+" = "+capEntry.getValue()+"</li>");
						}
					}
					out.println("</ul></li>");
					
					out.println("<li><b>Net Parameters:</b><ul>");
					Map<String, List<String>> netParam = s.getNetParams();
					if (netParam.isEmpty())
						out.println(s.getNetParams());
					else {
						for (Entry<String, List<String>> netEntry : netParam.entrySet()) {
							out.println("<li>"+netEntry.getKey()+ " = "+netEntry.getValue()+"</li>");
						}
					}
					out.println("</ul></li>");
					
					out.println("<li><b>Position:</b><ul>");
					Map<String, String> position = s.getPosition();
					if (position.isEmpty())
						out.println(s.getPosition());
					else {
						for (Entry<String, String> posEntry : position.entrySet()) {
							out.println("<li>"+posEntry.getKey()+" = "+posEntry.getValue()+"</li>");
						}
					}
					out.println("</ul></li>");
				
					out.println("<li><b>Input List:</b><ul>");
					Map<String, List<String>> inputList = s.getINPUT_LIST();
					if (inputList.isEmpty())
						out.println(s.getINPUT_LIST());
					else {
						for (Entry<String, List<String>> inputEntry : inputList.entrySet()) {
							out.println("<li>"+inputEntry.getKey()+" = "+inputEntry.getValue()+"</li>");
						}
					}
					out.println("</ul></li>");
				
					out.println("<li><b>Output List:</b><ul>");
					Map<String, List<String>> outputList = s.getOUTPUT_LIST();
					if (outputList.isEmpty())
						out.println(s.getOUTPUT_LIST());
					else {
						for (Entry<String, List<String>> outputEntry : outputList.entrySet()) {
							out.println("<li>"+outputEntry.getKey()+" = "+outputEntry.getValue()+"</li>");
						}
					}
					out.println("</ul></li>");
					out.println("<li><b>Refer to Hybrid</b> = "+ s.getReferToHybrid()+"</li></ul>");
				}
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