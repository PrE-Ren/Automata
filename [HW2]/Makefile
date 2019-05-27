JFLAGS = -g
JC = javac

all:
	javac *.java

CLASSES = Main.java

classes: $(CLASSES:.java=.class)

run: all classes
	java Main

clean:
	$(RM) *.class 
