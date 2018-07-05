package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.IronsConfiguration;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyToken;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;
import it.uniroma1.lcl.babelnet.WordNetSynsetID;
import it.uniroma1.lcl.jlt.util.Language;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mike on 4/23/18.
 * Modified to singleton on 6/1/18.
 */
public class BabelfyDisambiguator implements Disambiguator<SemanticGraph, TIntObjectHashMap> {

    private static BabelfyParameters.SemanticAnnotationResource senseSource;
    private static final String synsetTargetOffset = IronsConfiguration.getInstance().getWSDTarget();

    private static BabelfyDisambiguator instance = null;

    private Babelfy babelfy;

    private BabelNet babelNet;

    static {
        switch (IronsConfiguration.getInstance().getWSDSource()){
            case "BABELNET": senseSource = BabelfyParameters.SemanticAnnotationResource.BN;
            break;
            case "WN": senseSource = BabelfyParameters.SemanticAnnotationResource.WN;
            break;
            default:
                try {
                    throw new Exception("Wrong synset source! please check the iron.properties file.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private BabelfyDisambiguator(){
        BabelfyParameters babelfyParameters = new BabelfyParameters();
        babelfyParameters.setAnnotationResource(senseSource);
        babelfyParameters.setMCS(BabelfyParameters.MCS.ON_WITH_STOPWORDS);

        this.babelfy = new Babelfy(babelfyParameters);

        this.babelNet = BabelNet.getInstance();
    }

    private PosTag tagToPosTag(String tag){
        String[] noun = {"NN", "NNS", "NNP", "NNPS"};
        String[] verb = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        String[] adv = {"RB", "RBR", "RBS"};
        String[] adj = {"JJ", "JJR", "JJS"};

        if (Arrays.asList(noun).contains(tag)){
            return PosTag.NOUN;
        }else if(Arrays.asList(verb).contains(tag)){
            return PosTag.VERB;
        }else if(Arrays.asList(adv).contains(tag)){
            return PosTag.ADVERB;
        }else if(Arrays.asList(adj).contains(tag)){
            return PosTag.ADJECTIVE;
        }else{
            return PosTag.OTHER;
        }
    }

    public TIntObjectHashMap<List<String>> disambiguate(SemanticGraph content) throws InvalidBabelSynsetIDException, IOException {

        TIntObjectHashMap<List<String>> wsdedSynsetList = new TIntObjectHashMap<>();

        List<BabelfyToken> tokens = new ArrayList<>();
        List<IndexedWord> indexedWordList = content.vertexListSorted();

        for (IndexedWord word: indexedWordList){
            if (word.tag().equals(".")){
                tokens.add(BabelfyToken.EOS);
            }else{
                tokens.add(new BabelfyToken(word.word(),
                        word.lemma(),
                        tagToPosTag(word.tag()),
                        Language.EN));
            }
        }

//        indexedWordList.forEach(word -> System.out.print(word.word()));

        List<SemanticAnnotation> annotations = this.babelfy.babelfy(tokens, Language.EN);


//        System.out.println(annotations);

        switch (synsetTargetOffset) {
            case "BABELNET":
                for (SemanticAnnotation annotation : annotations) {
                    wsdedSynsetList.put(
                            annotation.getTokenOffsetFragment().getStart() + 1,
                            Collections.singletonList(annotation.getBabelSynsetID()));
                }
                break;
            case "WN":
                for (SemanticAnnotation annotation : annotations) {
                    Set<String> wordnetIDs = babelNet
                            .getSynset(new BabelSynsetID(annotation.getBabelSynsetID()))
                            .getWordNetOffsets()
                            .stream()
                            .map(WordNetSynsetID::getSimpleOffset)
                            .collect(Collectors.toSet());

                    wsdedSynsetList.put(annotation.getTokenOffsetFragment().getStart() + 1,
                            new ArrayList<>(wordnetIDs));
                }
                break;
            default:
                try {
                    throw new Exception("Wrong target synset offset source! please check irons.properties file.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return wsdedSynsetList;
    }

    public static synchronized BabelfyDisambiguator getInstance(){
        if (instance == null){
            instance = new BabelfyDisambiguator();
        }
        return instance;
    }
}
