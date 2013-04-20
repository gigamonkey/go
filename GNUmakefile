srcfiles	:= $(shell find java/src -name '*.java')
testfiles	:= $(shell find java/tests -name '*.java')
srcclasses	:= $(patsubst java/src/%.java,java/class/%.class,$(srcfiles))
testclasses	:= $(patsubst java/tests/%.java,java/class/%.class,$(testfiles))

all: $(srcclasses) $(testclasses)

$(srcclasses) : java/class/%.class: java/src/%.java | java/class
	javac -d java/class -classpath java/src:java/class $<

$(testclasses): java/class/%.class: java/tests/%.java | java/class
	javac -d java/class -classpath java/src:java/class $<

java/class:
	mkdir -p $@

clean:
	rm -rf java/class

tidy:
	find . -name '*~' -exec rm {} \;

test: all
	./critters 19
