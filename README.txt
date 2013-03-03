Installation:
=============

Explorer for apache solr is a http server application. Although most of the functionality is executed within the browser
it uses the http server as a proxy to the different solr servers you connect to (this to avoid the cross-site scripting
limitations when connected directly from the browser to non-origin solr servers).

The application comes in two flavours:

1) A .war file which is a standard Java Web Archive that can be deployed on any standard compliant server container (e.g. Tomcat, Jetty)

2) A standalone server which can be executed directly without the need of 3rd party servlet containers. This is the fastest
   way to start working with the application. That said, the embedded servlet container that is use is not meant to be used
   for production when high request loads are expected. To run this standalone application:

   a) extract the explorer-for-apache-solr-XXX.zip file to your preferred location (here referred as EXPLORER_HOME)
   b) cd to EXPLORER_HOME
   c) execute EXPLORER_HOME/explorer.sh script

   Before running the application, you would probably want to configure the solr cores you whish to connect to in the
   EXPLORER_HOME/cores-config.xml file. You can also change the location of this file and edit the EXPLORER_HOME/explorer.sh
   file to point to its new location.


For more information please visit http://www.searchworkings.org

Have fun!






