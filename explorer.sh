export CONFIG_FILE=cores-config.xml
export HTTP_PORT=8080

export JAVA_OPTIONS=-Dconfig=$CONFIG_FILE

export SERVER_OPTIONS=--httpPort=$HTTP_PORT

java $JAVA_OPTIONS -jar explorer-for-apache-solr-0.9.0.jar $SERVER_OPTIONS