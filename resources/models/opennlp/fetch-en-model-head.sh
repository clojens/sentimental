#!/usr/bin/env bash

tmp=$(mktemp)

arr=(http://opennlp.sourceforge.net/models-1.5/en-token.bin
     http://opennlp.sourceforge.net/models-1.5/en-sent.bin
     http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin
     http://opennlp.sourceforge.net/models-1.5/en-pos-perceptron.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-date.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-location.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-money.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-organization.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-percentage.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin
     http://opennlp.sourceforge.net/models-1.5/en-ner-time.bin
     http://opennlp.sourceforge.net/models-1.5/en-chunker.bin
     http://opennlp.sourceforge.net/models-1.5/en-parser-chunking.bin)

# Simulate for now by just showing the file size.
for uri in ${arr[@]}
  do

  # First show intended command echoed
    echo wget -nc --spider \"$uri\"
    # Next subshell (delay) and redirect stdout to temp file (overwrite)
    (
      wget -nc --spider "$uri" 2> $tmp &&\
        awk '/Length:/{a=1}/\[/{print;a=0}a' $tmp | cut -d ' ' -f3
    )
    # Finally we could awk and cut it up to show just human readible length
done

