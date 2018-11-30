# Project-IRONS
A research project for a novelty paradigm of corpus representation. (Project IRONS: Information Retrieval on Ontology Network of Sentences)
> **Caution! This is a technique preview version of IRONS for reproduction purpose. The code and all materials that mentioned in this repository are for academic research purpose only, Do not use them
in any commercial scenario!** 
### What is IRONS
IRONS is the first experimental implementation for corpus level representation. A document contains multiple semantic meanings , and each individual sentence of the document is a minimum semantic meaning carrier. A word is a minimum element of a language. But a word itself has meaning, only if it is in a particular sentence. Certain sentences contained in different documents describe a particular topic that is shatted throughout the corpus. A topic cannot be completely described by a document or several documents, since a topic is an abstract concept that only exists on the corpus level. Furthermore, a topic is a hierarchical concept, which means a topic may contain multiple sub-topics. Thus, we implemented Project IRONS as our first attempt for corpus representation. Its final goal is fundamentally solve the problems of document clustering, topic modeling and topic summarization.

### How to use IRONS
  1. Download the repository then load it with any IDE that supports Maven framework
  2. Download all dependent libraries and data then put them in /lib folder
  3. Modify the /config/irons.properties file for your needs
  4. Check /src/test/java/edu.udel.irl/irons/IronsTest.java for an example
### Dependent library and data
The rights of dependent libraries, toolkits and data are belong to their developers or organizations. 
##### JavaPlex
- [Persistent Homology and Topological Data Analysis Library](https://github.com/appliedtopology/javaplex/files/2196392/javaplex-processing-lib-4.3.4.zip)
##### BabelNet
- [BabelNet Java Online and Offline API 3.7.1](http://babelnet.org/data/3.7/BabelNet-API-3.7.1.zip)
  - Use the code below to install BabelNet 3.7.1 to your local Maven repository
  
```shell
#!/bin/bash

mvn install:install-file -Dfile=lib/babelfy-commons-1.0.jar -DgroupId=it.uniroma1.lcl.babelfy.commmons -DartifactId=babelfy-commons -Dversion=1.0 -Dpackaging=jar
unzip -p babelnet-api-3.7.1.jar META-INF/maven/it.uniroma1.lcl.babelnet/babelnet-api/pom.xml | grep -vP '<(scope|systemPath)>' >babelnet-api-3.7.1.pom
mvn install:install-file -Dfile=babelnet-api-3.7.1.jar -DpomFile=babelnet-api-3.7.1.pom
```

- [BabelNet Offline Indices](https://babelnet.org/guide#HowcanIdownloadtheBabelNetindices?)
##### ADW
- [ADW WordNet Word Similarity Matrix 1.0](http://lcl.uniroma1.it/adw/jar/adw.v1.0.tar.gz)
- [ADW Signature Files](http://lcl.uniroma1.it/adw/ppvs.30g.1k.tar.gz)
##### Babelfy
- [Babelfy RESTful Java API 1.0](http://babelfy.org/data/BabelfyAPI-1.0.zip)
##### NASARI
- [NASARI Embed Vector Representations for BabelNet synsets](http://lcl.uniroma1.it/nasari/files/NASARIembed+UMBC_w2v.zip)
- [NASARI Lexical Vector Representations for BabelNet synsets](http://lcl.uniroma1.it/nasari/files/NASARI_lexical_english.zip)
- [NASARI Unified Vector Representations for BabelNet synsets](http://lcl.uniroma1.it/nasari/files/NASARI_unified_english.zip)
### Examples
