##BUILD YOUR DEVELOPMENT ENVIRONMENT

1. Remove "This Plugin is a singleton"
2. Rename "plugin.xml" => "plugin.xml.tobuild"
3. Remove from dependencies:
	a. org.apache.felix.gogo.runtime;bundle-version="0.8.0",
	b. org.eclipse.equinox.app;bundle-version="1.3.100",
	c. org.eclipse.core.runtime;bundle-version="3.7.0"

N.B. In order to export SNPS Middleware, Manifest.ml MUST be as follow:
	Manifest-Version: 1.0
	Bundle-ManifestVersion: 2
	Bundle-Name: Core
	Bundle-SymbolicName: org.osgi.snps.core
	Bundle-Version: 1.0.0.qualifier
	Bundle-Activator: org.osgi.snps.core.Activator
	Export-Package: org.osgi.snps.core
	Require-Bundle: org.apache.felix.gogo.command;bundle-version="0.8.0",
	 org.apache.felix.gogo.runtime;bundle-version="0.8.0",
	 org.apache.felix.gogo.shell;bundle-version="0.8.0",
	 org.eclipse.equinox.console;bundle-version="1.0.0",
	 org.eclipse.osgi;bundle-version="3.7.0",
	 org.eclipse.equinox.app;bundle-version="1.3.100",
	 ISNPS;bundle-version="1.0.0",
	 publisher;bundle-version="1.0.0",
	 SMLParser;bundle-version="1.0.0",
	 snps.registry;bundle-version="1.0.0",
	 ch.ethz.iks.r_osgi.remote;bundle-version="1.0.0",
	 org.eclipse.equinox.event;bundle-version="1.3.0",
	 org.eclipse.core.runtime;bundle-version="3.7.0"
	Import-Package: org.osgi.service.event
	Bundle-ActivationPolicy: lazy
	Bundle-RequiredExecutionEnvironment: JavaSE-1.6

4. Goto Core Activator and Decomment The piece of code about "Debug Mode" (CommandLine)
5. Re-Configure launch configuration:
	a. Deselect all and then select only middleware bundles
	
6. In order to show online WSDL, on VM you can add this iptables rule:
	+ iptables -t nat -A PREROUTING -p tcp --dport 9090 -j DNAT --to-destination 192.168.0.8:9090
	(where 192.168.0.8 is the local ip address)
	
7. In order to grant access to mysqldb, goto mysql and add this:
   + GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON SensorDB.* TO 'root'@'192.168.0.8' IDENTIFIED BY '<PASSWORD>';
   (you can customize dbname, username, host (ifconfig before), password)

8. add a config.xml on SNPSMDW folder containing:
		<client>
			<config>
				<ip>192.168.0.8</ip>
				<port>9090</port>
			</config>
		</client>
 
