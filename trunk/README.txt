  README - 31.10.2010
  
  Doctor-Doc is a webbased and a easy-to-use tracking and
  managing system for scientific literature
  
  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; version 2 of the License.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

  Contact: info@doctor-doc.com


There are two mailing lists available for Doctor-Doc:

https://sourceforge.net/projects/doctor-doc/support

We strongly encourage you to get involved!
  
  
Requirements:

---
Webserver Apache 1.3 or 2.0
Tomcat 5.0 or higher
Java 1.6
MySQL 5.0 or higher


---
Installation / Configuration:

1. Database
Use master_dump.sql under /db to create the database and
a demo-account

---

2. Configuration
In the source files in /source/resources/SystemConfiguration.properties please enter 
the appropriate configurations for your system. Otherwise the system won't work!
-
You may also may want to use a custom icon, e.g. from your institution, for your installation:
replace the following two images /img/sp.gif and /war/img/sp.gif with your own image but with the same name.
The image should be quadratic to avoid distortion. 
-
Doctor-Doc is able to use pooled Connections to MySQL. We use c3p0, since with c3p0 we can
directly include the necessary libraries. But you still have to confiugre Tomcat appropriately:

This configuration is easy but the configuration depends on the Tomcat-Version in use. Therefore
this option is disabled by default in /source/resources/SystemConfiguration.properties

If you decide to benefit from an enormous PERFORMANCE BOOST with enabled pooled Connections, you have 
to configure Tomcat as indicated below.

Doctor-Doc works also without pooled Connection. Be careful: enabling the option in SystemConfiguration.properties and not 
using the configuration below will generate a lot of error messages by email and in the log-file and it will seriously 
REDUCE SYSTEM PERFORMANCE!

Checkout http://www.mchange.com/projects/c3p0/index.html for all configuration details. 

Tomcat 5.0
Place in your $CATALINA_HOME/conf/server.xml of your Tomcat-Installation the following
code between <Context path=...> and </Context>

<Resource name="jdbc/pooledDS" auth="Container" type="com.mchange.v2.c3p0.ComboPooledDataSource" />
  <!-- Tomcat 5.0 -->
  <ResourceParams name="jdbc/pooledDS">
     <parameter><name>factory</name><value>org.apache.naming.factory.BeanFactory</value></parameter>  
     <parameter><name>driverClass</name><value>com.mysql.jdbc.Driver</value></parameter>  
     <parameter><name>jdbcUrl</name><value>jdbc:mysql://127.0.0.1/doctor-doc_com_dbs?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;jdbcCompliantTruncation=false</value></parameter>  
     <parameter><name>user</name><value>username</value></parameter>  
     <parameter><name>password</name><value>password</value></parameter>  
     <parameter><name>minPoolSize</name><value>0</value></parameter>  
     <parameter><name>maxPoolSize</name><value>30</value></parameter>
     <parameter><name>acquireIncrement</name><value>5</value></parameter>
     <parameter><name>maxIdleTimeExcessConnections</name><value>1800</value></parameter>
     <parameter><name>automaticTestTable</name><value>c3p0TestTable</value></parameter>
     <parameter><name>testConnectionOnCheckin</name><value>true</value></parameter>
     <parameter><name>idleConnectionTestPeriod</name><value>240</value></parameter>
</ResourceParams>

Tomcat 5.5
Works different under 5.5! The configuration has to be made in the context.xml and NOT the server.xml! There are also
slight differences in the syntax.
Place in your $CATALINA_HOME/conf/context.xml of your Tomcat-Installation the following
code:

<Context>
    <!-- Tomcat 5.5 -->
    <Resource auth="Container" 
  description="DB Connection" 
  driverClass="com.mysql.jdbc.Driver" 
  maxPoolSize="30" 
  minPoolSize="0" 
  acquireIncrement="5" 
  maxIdleTimeExcessConnections="1800"
  automaticTestTable="c3p0TestTable"
  testConnectionOnCheckin="true"
  idleConnectionTestPeriod="240"
  name="jdbc/pooledDS" 
  user="username" 
  password="password" 
  factory="org.apache.naming.factory.BeanFactory" 
  type="com.mchange.v2.c3p0.ComboPooledDataSource" 
  jdbcUrl="jdbc:mysql://127.0.0.1/doctor-doc_com_dbs?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;jdbcCompliantTruncation=false" />
</Context>

---

3. Build a war-file using e.g. the predefined build.xml with Ant.
Deploy the war-file in the webapps-folder of your Tomcat-installation.

---
4. Login

http://{name-of-your-server}:{tomcat-portnumber}/{name-of-the-war-file}/login.do

staff@doctor-doc.com / staff as librarian
user@doctor-doc.com / user as patron (with login permission)

For a productive system, please change the passwords and emails, or delete the users!

You may change the account information completely to use the system in a productive environement.

---
