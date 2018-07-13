# SNPS - Sensor Node Plugin System

## What is 

Primary goal of this middleware, is to bring any physical sensor (actuator) on an abstraction 
level that allows for easier and standardized management tasks (switch on/off, sampling), 
in a way that is independent of the proprietary sensor's specification. By the time a sensor 
is ``plugged'' into the middleware, it will constitute a resource/service capable of interacting 
with other resources (be them other sensors plugged into the middleware or third party services)
in order to compose high-value services to be accessed in SOA-fashion. 
The middleware also offers a set of complimentary services and tools to support the management of
the entire life cycle of sensors and to sustain the overall QoS provided by them.

The SNPS middleware is organized into several components, and each component was implemented as a
software module (or ``bundle'') within the OSGi equinox framework. 
The figure depicts the architecture of the middleware and its main components. 

![architecture](https://raw.githubusercontent.com/fmount/snps/master/docs/images/snps_architecture.jpg)


The overall architecture can be broken down into three macro-blocks:

1. Sensor Layer Integration
2. Core and related Components
3. Web Service Integration


## Deep dive into the architecture

1. **Core**: It is where the business logic of the Middleware resides. The Core acts as an orchestrator who coordinates the middleware's activities.
             Data and commands flowing forth and back from the web service layer to the sensor layer are dispatched by the Core to the appropriate component.

2. **Registry**: It is the component where all information about sensors, middleware's components and provided services are stored and indexed for search purpose.
                 As for the sensors, data regarding the geographic position and the topology of the managed wireless sensor networks are stored in the Registry.
                 Also, each working component needs to signal its presence and functionality to the Registry, which will have to make this information public and
                 available so that it can be discovered by any other component/service in the middleware.

3. **Processor**: It is component responsible for the manipulation of the data flow coming from the sensors. In particular, it provides a service to set and enforce
                  a sampling plan on a single sensor or on an aggregate of sensors. Also, this component can be instructed to process data according to specific processing
                  templates.

4. **Composer**: It represents the component which implements the sensors' composition service. Physical sensors can be *virtualized* and are given a uniform representation
                 which allows for *aggregating* multiple virtualized sensors into one sensor that will eventually be exposed to applications.

5. **Event Manager**: It is one of the most important components of the middleware. It provides a publish/subscribe mechanism which can be exploited by every middleware's
                      component to implement asynchronous communication. Components can either be producers (publishers) or consumers (subscribers) of every kind of information
                      that is managed by the middleware. This way, data flows, alerts, commands are wrapped into *events* that are organized into topics and are dispatched to any
                      entity which has expressed interest in them.

6. **DAO**:  It represents the persistence layer of the middleware. It exposes APIs that allow service requests to be easily mapped onto storage or search calls to the database.



## BUILD YOUR DEVELOPMENT ENVIRONMENT (Based on ECLIPSE)

1. Remove "This Plugin is a singleton"
2. Rename "plugin.xml" => "plugin.xml.tobuild"
3. Remove from dependencies:
    * org.apache.felix.gogo.runtime;bundle-version="0.8.0",
    * org.eclipse.equinox.app;bundle-version="1.3.100",
    * org.eclipse.core.runtime;bundle-version="3.7.0"

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
    * Deselect all and then select only middleware bundles

6. In order to show online WSDL, on VM you can add this iptables rule:
    * iptables -t nat -A PREROUTING -p tcp --dport <PORT> -j DNAT --to-destination <IP>:<PORT>

7. In order to grant access to mysqldb, goto mysql and add this:
   * GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON SensorDB.* TO 'root'@'%' IDENTIFIED BY '\<PASSWORD\>';


8. add a config.xml on SNPSMDW folder containing:

    ```
    \<client\>
        \<config\>
            \<ip\>192.168.0.8\<\/ip\>
            \<port\>9090\<\/port\>
        \<\/config\>
    \<\/client\>
    ```
