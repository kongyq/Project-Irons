package edu.udel.irl.irons.wsd;

import gnu.trove.map.hash.TIntObjectHashMap;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;

import java.io.IOException;
import java.util.List;

/**
 * Created by mike on 4/23/18.
 */
public interface Disambiguator <T, U>{

    public TIntObjectHashMap<List<String>> disambiguate(T content) throws InvalidBabelSynsetIDException, IOException;

}
