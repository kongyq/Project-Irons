package edu.udel.irl.irons.util;


import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by mike on 4/13/18.
 */
public class CoreNlpUtil {

    private Document document;
    private TIntObjectHashMap<String> sentList;

    public CoreNlpUtil(String origText){
        this.document = new Document(origText);
        this.sentList = new TIntObjectHashMap<>();

        for (Sentence sent: this.document.sentences()){
            this.sentList.put(sent.sentenceIndex(), sent.text());
        }
    }

    public TIntObjectHashMap<String> getSentList(){return this.sentList;}

    public String getSentence(int sentIndex){
        return this.sentList.get(sentIndex);
    }



}
