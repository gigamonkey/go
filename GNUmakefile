javafiles  = $(shell find java -name *.java)
#classfiles = $(patsubst java/src/%,java/class/%,$(patsubst %.java,%.class,$(javafiles)))
classfiles = $(patsubst java/src/%.java,java/class/%.class,$(javafiles))

all: $(classfiles)
	@echo $(classfiles)

$(classfiles): java/class/%.class: java/src/%.java
	javac -d java/class -classpath java/src:java/class $<

clean:
	rm -rf java/class
	mkdir java/class

test: all
	java -cp java/class com.gigamonkeys.go.GUI
