package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyToken;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;
import it.uniroma1.lcl.jlt.util.Language;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mike on 4/23/18.
 * Modified to singleton on 6/1/18.
 */
public class BabelfyDisambiguator implements Disambiguator<SemanticGraph, TIntObjectHashMap> {

    private static BabelfyDisambiguator instance = null;

    private Babelfy babelfy;
    private BabelfyParameters babelfyParameters;

    private BabelNet babelNet;

    private BabelfyDisambiguator(){
        this.babelfyParameters = new BabelfyParameters();
        this.babelfyParameters.setAnnotationResource(BabelfyParameters.SemanticAnnotationResource.WN);
        this.babelfyParameters.setMCS(BabelfyParameters.MCS.ON_WITH_STOPWORDS);

        this.babelfy = new Babelfy(this.babelfyParameters);

        this.babelNet = BabelNet.getInstance();
    }

    public PosTag tagToPosTag (String tag){
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
            if (word.tag() == "."){
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

        for (SemanticAnnotation annotation:annotations){
            Set<String> wordnetIDs = babelNet
                    .getSynset(new BabelSynsetID(annotation.getBabelSynsetID()))
                    .getWordNetOffsets()
                    .stream()
                    .map(obj -> obj.getSimpleOffset())
                    .collect(Collectors.toSet());

            wsdedSynsetList.put(annotation.getTokenOffsetFragment().getStart() + 1,
                    new ArrayList<>(wordnetIDs));
        }

//        System.out.println(wsdedSynsetList);

        return wsdedSynsetList;
    }

    public static synchronized BabelfyDisambiguator getInstance(){
        if (instance == null){
            instance = new BabelfyDisambiguator();
        }
        return instance;
    }
}
