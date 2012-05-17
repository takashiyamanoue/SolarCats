netsh firewall add portopening protocol=tcp port=2785 name=dsr-nd1
netsh firewall add portopening protocol=tcp port=10008 name=dsr-nd2
netsh firewall add portopening protocol=udp port=10007 name=dsr-nd3
rem set classpath=%classpath%;classes;lib
set classpath=classes;lib;lib\jmf.jar;lib\comm.jar
java -Xmx256m nodesystem.CommunicationNode
exit
