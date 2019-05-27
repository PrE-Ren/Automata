JFLAGS = -g
JC = javac

all: 
	$(JC) *.java

form: classes 
	java Main

CLASSES = Main.java state.java

classes: $(CLASSES:.java=.class)

run: all form
	

clean:
	$(RM) *.class 
