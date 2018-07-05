package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.IronsConfiguration;
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

    private static final String synsetTargetOffset = IronsConfiguration.getInstance().getWSDTarget();
    private static BabelSenseSource senseSource = IronsConfiguration.
            getEnumFromString(BabelSenseSource.class, IronsConfiguration.getInstance().getWSDSource());

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
                if(senseSource.equals(BabelSenseSource.WN)) {
                    senseList = babelNet
                            .getSenses(
                                    word.lemma(),
                                    Language.EN,
                                    tagToBabelPOS(word.tag()),
                                    Collections.singletonList(Language.EN),
                                    senseSource);
                }else if(senseSource.equals(BabelSenseSource.BABELNET)){
                    senseList = babelNet
                            .getSenses(
                                    word.lemma(),
                                    Language.EN,
                                    tagToBabelPOS(word.tag()),
                                    Collections.singletonList(Language.EN));
                }else{
                    throw new Exception("Wrong synset source! please check the iron.properties file.");
                }
//                System.out.println(senseList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(senseList.isEmpty()){continue;}
            //find the most frequency sense
            switch (synsetTargetOffset) {
                case "BABELNET": {
                    String synsetOffset = senseList
                            .stream()
                            .max(Comparator.comparing(BabelSense::getFrequency))
                            .get()
                            .getSynsetID().getID();
//                    .getWordNetOffset();
                    wsdedSynsetList.put(word.index(), Collections.singletonList(synsetOffset));
                    break;
                }
                case "WN": {
                    String synsetOffset = senseList
                            .stream()
                            .max(Comparator.comparing(BabelSense::getFrequency))
                            .get()
                            .getWordNetOffset();
                    wsdedSynsetList.put(word.index(), Collections.singletonList(synsetOffset));
                    break;
                }
                default:
                    try {
                        throw new Exception("Wrong target synset offset source! please check irons.properties file.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
        return wsdedSynsetList;
    }

    /**
     * convert Penn Treebank POS to BabelPOS
     * @param tag Penn Treebank POS
     * @return BabelPOS
     */
    private BabelPOS tagToBabelPOS(String tag){
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
