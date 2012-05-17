## compile using jdk 1.2.2, don't use jdk 1.3.0

CLASSPATH = ..
JAVA	= CLASSPATH=$(CLASSPATH) java
JAVAC	= CLASSPATH=$(CLASSPATH) javac
JAVADOC	= CLASSPATH=$(CLASSPATH) javadoc

classes	= Controller.class \
	  Stepper.class StepMotor.class \
	  Diagnose.class EdgeButton.class EdgeView.class TouchView.class \
	  MotorButton.class MotorView.class \
	  ScaledView.class AnalogView.class \
	  Interface.class Constants.class

all:		$(classes)
clean:		; rm -f *.class
doc:		; rm -rf docs
		  mkdir docs
		  $(JAVADOC) -package -d docs ft ft.comm ft.industry ft.mobile
test::	i d
i:	Interface.class	; $(JAVA) ft.Interface COM1
d:	Diagnose.class	; $(JAVA) ft.Diagnose COM1
	
.SUFFIXES:
.SUFFIXES:	.class .java
.java.class:	; $(JAVAC) $*.java
