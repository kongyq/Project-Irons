package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.uniroma1.lcl.babelnet.*;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;

import java.io.IOException;
import java.util.*;

/**
 * Created by mike on 6/14/18.
 */
public class MFSDisambiguator implements Disambiguator<SemanticGraph, TIntObjectHashMap>{

    public static MFSDisambiguator instance = null;

    private BabelNet babelNet;


    private MFSDisambiguator(){
        this.babelNet = BabelNet.getInstance();
    }

    public TIntObjectHashMap<List<String>> disambiguate(SemanticGraph content){

        //WSDed result to return
        TIntObjectHashMap<List<String>> wsdedSynsetList = new TIntObjectHashMap<>();

        Set<IndexedWord> indexedWordSet = content.vertexSet();

        for(IndexedWord word:indexedWordSet){

            //if the word's POS is not nvra, then skip
            if(tagToBabelPOS(word.tag()) == null){continue;}

            List<BabelSense> senseList = new ArrayList<>();
            try {
                //get all senses of the word with specific language pos and source
                senseList = babelNet.getSenses(word.lemma(), Language.EN, tagToBabelPOS(word.tag()), BabelSenseSource.WN);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(senseList.isEmpty()){continue;}
            //find the most frequency sense
            String wordNetOffset = senseList
                    .stream()
                    .max(Comparator.comparing(BabelSense::getFrequency))
                    .get()
                    .getWordNetOffset()
                    .toString();

            ArrayList<String> wordNetOffsetList = new ArrayList<>(1);
            wordNetOffsetList.add(wordNetOffset);

            wsdedSynsetList.put(word.index(), wordNetOffsetList);

        }
        return wsdedSynsetList;
    }

    /**
     * convert Penn Treebank POS to BabelPOS
     * @param tag Penn Treebank POS
     * @return BabelPOS
     */
    public BabelPOS tagToBabelPOS (String tag){
        String[] noun = {"NN", "NNS", "NNP", "NNPS"};
        String[] verb = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        String[] adv = {"RB", "RBR", "RBS"};
        String[] adj = {"JJ", "JJR", "JJS"};

        if (Arrays.asList(noun).contains(tag)){
            return BabelPOS.NOUN;
        }else if(Arrays.asList(verb).contains(tag)){
            return BabelPOS.VERB;
        }else if(Arrays.asList(adv).contains(tag)){
            return BabelPOS.ADVERB;
        }else if(Arrays.asList(adj).contains(tag)){
            return BabelPOS.ADJECTIVE;
        }else{
            return null;
        }
    }

    public static synchronized MFSDisambiguator getInstance(){
        if(instance == null){
            instance = new MFSDisambiguator();
        }
        return instance;
    }
}
