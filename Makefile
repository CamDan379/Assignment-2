# Makefile for Assignment 2
# Cameron Rebelo RBLCAM001
# 29 August 2021

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= WordRecord.class WordPanel.class WordDictionary.class Score.class WordApp.class
         
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

run: $(CLASS_FILES)
	java  -cp $(BINDIR) 