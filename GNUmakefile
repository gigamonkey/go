srcfiles	:= $(shell find java/src -name '*.java')
testfiles	:= $(shell find java/tests -name '*.java')
srcclasses	:= $(patsubst java/src/%.java,java/class/%.class,$(srcfiles))
testclasses	:= $(patsubst java/tests/%.java,java/class/%.class,$(testfiles))

all: $(srcclasses) $(testclasses)

$(srcclasses) : java/class/%.class: java/src/%.java
	javac -d java/class -classpath java/src:java/class $<

$(testclasses): java/class/%.class: java/tests/%.java
	javac -d java/class -classpath java/src:java/class $<


clean:
	rm -rf java/class
	mkdir java/class

test: all
	./critters 19
