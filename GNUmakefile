srcfiles	:= $(shell find java/src -name '*.java')
testfiles	:= $(shell find java/tests -name '*.java')
srcclasses	:= $(patsubst java/src/%.java,java/class/%.class,$(srcfiles))
testclasses	:= $(patsubst java/tests/%.java,java/class/%.class,$(testfiles))

JAVACOPTS := -Xlint:unchecked

all: $(srcclasses) $(testclasses)

$(srcclasses) : java/class/%.class: java/src/%.java | java/class
	javac $(JAVACOPTS) -d java/class -classpath java/src:java/class $<

$(testclasses): java/class/%.class: java/tests/%.java | java/class
	javac $(JAVACOPTS) -d java/class -classpath java/src:java/class $<

java/class:
	mkdir -p $@

clean:
	rm -rf java/class

tidy:
	find . -name '*~' -exec rm {} \;
	rm -f java/src/com/gigamonkeys/go/VM.java.bak

vm:
	./vm.pl java/src/com/gigamonkeys/go/VM.java

test: all
	./critters 19
