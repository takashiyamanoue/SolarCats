## compile using jdk 1.2.2

COM1	= COM1
JAVA	= cd .. && CLASSPATH=.. java
JAVAC	= CLASSPATH=../.. javac

all:		Play.class Record.class
clean:		; rm -f *.class
test:	r p
r:		; $(JAVA) ft.industry.Record $(COM1) >industry/skript
p:		; $(JAVA) ft.industry.Play $(COM1)  <industry/skript

.SUFFIXES:
.SUFFIXES:	.class .java
.java.class:	; $(JAVAC) $*.java
