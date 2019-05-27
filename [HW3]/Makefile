JFLAGS = -g
JC = javac

all:
	javac *.java

CLASSES = Main.java

classes: $(CLASSES:.java=.class)

run1: all classes
	java Main TM1.txt

run2: all classes
	java Main TM2.txt

run3: all classes
	java Main TM3.txt
clean:
	$(RM) *.class 
