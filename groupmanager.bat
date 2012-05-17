netsh firewall add portopening protocol=tcp port=31415 name=dsr-gm
set classpath=%classpath%;classes;lib;comm.jar
java nodesystem.groupmanager.GroupManager
exit
