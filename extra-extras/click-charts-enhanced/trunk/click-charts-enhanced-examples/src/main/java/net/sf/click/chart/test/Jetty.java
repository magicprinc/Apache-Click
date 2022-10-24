package net.sf.click.chart.test;

import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class Jetty {
	private static final Server SERVER = new Server();
	public static void main(String[] args) {
		String webapp = "./target/";
		if (args.length > 0) {
			webapp = args[0];
		}

		WebAppContext app = new WebAppContext();
		app.setContextPath("/click-charts-enhanced-examples");
		app.setWar(webapp);
		// Avoid the taglib configuration because its a PITA if you don't have a net connection
		app.setConfigurationClasses(new String[] { WebInfConfiguration.class.getName(), WebXmlConfiguration.class.getName() });
		app.setParentLoaderPriority(true);

		// We explicitly use the SocketConnector because the SelectChannelConnector locks files
		Connector connector = new SocketConnector();
		connector.setPort(Integer.parseInt(System.getProperty("jetty.port", "8080")));
		connector.setMaxIdleTime(60000);

		Jetty.SERVER.setConnectors(new Connector[] { connector });
		Jetty.SERVER.setHandler( app );
		Jetty.SERVER.setAttribute("org.mortbay.jetty.server.Request.maxFormContentSize", new Integer(0) );
		Jetty.SERVER.setStopAtShutdown(true);

		try {
			Jetty.SERVER.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
