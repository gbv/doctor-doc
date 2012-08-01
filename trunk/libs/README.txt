The libraries in this folder are not necessary for running a productive system.

You may add them to the build path, but make sure servlet-api.jar does not get exported, if using another build process than build.xml.
Servlet-api.jar is already included in the Tomcat libraries and may cause problems if exported additionally. However, it is needed for 
building a war file by build.xml.