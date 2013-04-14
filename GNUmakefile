javafiles  = $(shell find java -name *.java)
classfiles = $(patsubst java/src/%.java,java/class/%.class,$(javafiles))

all: $(classfiles)

java/class/%.class: java/src/%.java
	javac -d java/class -classpath java/src:java/class $<

clean:
	rm -rf java/class
	mkdir java/class

test: all
	./critters 19
