  README - 25.10.2013
  
  Doctor-Doc is a web based and a easy-to-use tracking and
  managing system for scientific literature.
  
  Copyright (C) 2005 - 2013  Markus Fischer, Pascal Steiner

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; version 2 of the License.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
  MA  02110-1301, USA.

  Contact: info@doctor-doc.com


There are two mailing lists available for Doctor-Doc:

https://sourceforge.net/projects/doctor-doc/support

We strongly encourage you to get involved!

---  
  
Minimal requirements:

Tomcat 6
Java 1.6
MySQL 5.0

---
Installation / Configuration:

To install Doctor-Doc in a development environment or to build a war 
file, take a look at how-to-checkout.txt  

1. Database
Use master_dump.sql under /db to create the database and
a demo-account

---

2. Configuration
In /source/resources/SystemConfiguration.properties please enter the
appropriate configurations for your system.
-
Doctor-Doc uses UTF-8 encoded URIs. Add the parameter URIEncoding="UTF-8"
to the Connector configuration in Tomcats server.xml:

<Connector port="... URIEncoding="UTF-8" />

-

Change in struts-config.xml the port numbers needed for you environment:

    <plug-in className="org.apache.struts.action.SecurePlugIn">
        <set-property property="httpPort" value="8080"/>
        <set-property property="httpsPort" value="8443"/>
        <set-property property="enable" value="true"/>
        <set-property property="addSession" value="true"/>
    </plug-in>

-

To use a custom icon for your institution replace /img/sp.gif.

-
Doctor-Doc is able to use pooled database connections. We use BoneCP. 
To use pooled connections you have to configure Tomcat appropriately:

Enable pooled connection in /source/resources/SystemConfiguration.properties

Configure Tomcat as indicated below. Checkout http://jolbox.com/ for 
additional configuration details. 


The configuration has to be made in context.xml.
Add in $CATALINA_HOME/conf/context.xml the following configuration:

<Context>
 <!-- Configuration for PooledConnections with BoneCP in Tomcat -->
      <Resource auth="Container"
        driverClass="com.mysql.jdbc.Driver"
        name="jdbc/pooledDS" 
        username="username" 
        password="password" 
        factory="org.apache.naming.factory.BeanFactory" 
        type="com.jolbox.bonecp.BoneCPDataSource" 
        jdbcUrl="jdbc:mysql://127.0.0.1/doctor-doc_com_dbs?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;jdbcCompliantTruncation=false" 
        idleMaxAgeInMinutes="240"
        idleConnectionTestPeriodInMinutes="60"
        partitionCount="3"
        acquireIncrement="5"
        maxConnectionsPerPartition="15"
        minConnectionsPerPartition="3"
        statementsCacheSize="50" />
</Context>

---

3. Build a war-file using the predefined build.xml with Ant.
Deploy the war-file in the webapps-folder of your Tomcat installation.

---
4. Login

http://{name-of-your-server}:{tomcat-portnumber}/{name-of-the-war-file}/login.do

staff@doctor-doc.com / staff as librarian
user@doctor-doc.com / user as patron (with login permission)

You may change the account information completely to use the system in a
productive environment. Please change the default passwords and emails,
or delete the users!

---
5. SSL

To use SSL for the login process, change in the plugin section of
struts-config.xml the port numbers needed for you environment:

    <plug-in className="org.apache.struts.action.SecurePlugIn">
        <set-property property="httpPort" value="80"/>
        <set-property property="httpsPort" value="443"/>
        <set-property property="enable" value="true"/>
        <set-property property="addSession" value="true"/>
    </plug-in>
    
Set also all existing entries in struts-config.xml with

<set-property property="secure" value="false"/> to <set-property property="secure" value="true"/>

Configure Tomcat's server.xml with an additional connector like this:

     <Connector port="443" protocol="HTTP/1.1" SSLEnabled="true"
               maxThreads="150" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS" 
	      	   keystoreFile="/path/to/tomcatkeystore"
	       	   keystorePass="mySecretPassword" URIEncoding="UTF-8" />
	       	   
Make sure to use URIEncoding="UTF-8"! And of course you need a valid keystore...
