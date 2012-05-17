netsh firewall add portopening protocol=tcp port=8089 name=webleap-proxy
set classpath=%classpath%;classes;lib;comm.jar
java nodesystem.SearchEngineProxy.ProxyServer -nw

