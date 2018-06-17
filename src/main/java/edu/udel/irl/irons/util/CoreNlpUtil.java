package edu.udel.irl.irons.util;


import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import edu.stanford.nlp.util.CoreMap;
import edu.udel.irl.irons.IronsConfiguration;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

/**
 * Created by mike on 4/13/18.
 */
public class CoreNlpUtil {

    private static final File STOPWORDLIST = new File(IronsConfiguration.getInstance().getStopwordList());
    private static boolean ignoreCase = IronsConfiguration.getInstance().getIsIgnoreStopwordCase();
    private static CoreNlpUtil instance = null;

    private StanfordCoreNLP pipeline;

    private Document document;
    private TIntObjectHashMap<String> sentList;

    private CoreNlpUtil(){
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, stopword, depparse");
        props.setProperty("customAnnotatorClass.stopword",  "edu.udel.irl.irons.util.StopwordAnnotator");
        props.setProperty(StopwordAnnotator.IGNORE_STOPWORD_CASE, String.valueOf(ignoreCase));
        if(STOPWORDLIST.exists()) {
            try {
                props.setProperty(StopwordAnnotator.STOPWORDS_LIST, loadStopWordList(STOPWORDLIST));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.pipeline = new StanfordCoreNLP(props);
    }

    public CoreNlpUtil(String origText){
        this.document = new Document(origText);
        this.sentList = new TIntObjectHashMap<>();

        for (Sentence sent: this.document.sentences()){
            this.sentList.put(sent.sentenceIndex(), sent.text());
        }
    }

    private static String loadStopWordList(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));

    }

    //this method only for test purpose.
    public SemanticGraph parseOneSentenceDoc(String origSent){
        Annotation sentence = new Annotation(origSent);
        this.pipeline.annotate(sentence);
        System.out.println(sentence.get(SentencesAnnotation.class).get(0).get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class));
        return sentence.get(SentencesAnnotation.class).get(0).get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
    }

    public TIntObjectHashMap<SemanticGraph> getSentList(String origText){
        TIntObjectHashMap<SemanticGraph> sentList = new TIntObjectHashMap<>();

        //used new data stucture from CoreNlp 3.9.0
        //for now I cannot use this new data stucture since the method for getting dependency parse tree only support
        // enhanced plus plus mode which will deduct loop in the graph which will caused infinity loop.
        //I think I will fix this later maybe.
//        CoreDocument document = new CoreDocument(origText);
        Annotation document = new Annotation(origText);
        this.pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(int i = 0; i < sentences.size(); i ++){
            sentList.put(i+1, sentences.get(i).get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class));
        }
        return sentList;
    }

    public TIntObjectHashMap<String> getSentList(){return this.sentList;}

    public String getSentence(int sentIndex){
        return this.sentList.get(sentIndex);
    }

    public static synchronized CoreNlpUtil getInstance(){
        if(instance == null){
            instance = new CoreNlpUtil();
        }
        return instance;
    }

}
