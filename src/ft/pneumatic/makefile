## compile using jdk 1.2.2

JAVA	= cd .. && CLASSPATH=.. java
JAVAC	= CLASSPATH=../.. javac
COM1	= COM1

all:		Sorter.class
clean:		; rm -f *.class
test:		; $(JAVA) ft.Controller $(COM1) ft.pneumatic.Sorter
	
.SUFFIXES:
.SUFFIXES:	.class .java
.java.class:	; $(JAVAC) $*.java
