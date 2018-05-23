package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.simple.Sentence;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

/**
 * Created by mike on 5/14/18.
 */
public class NasariDisambiguator implements Disambiguator<Sentence, TIntObjectHashMap>{

    public NasariDisambiguator(){

    }

    public TIntObjectHashMap<List<String>> disambiguate(Sentence content){
        return null;
    }

}
