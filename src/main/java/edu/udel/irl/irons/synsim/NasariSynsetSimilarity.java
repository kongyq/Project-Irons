package edu.udel.irl.irons.synsim;

import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.nasari.NasariLexicalModel;
import edu.udel.irl.irons.nasari.NasariModel;
import edu.udel.irl.irons.nasari.NasariUnifiedModel;
import edu.udel.irl.irons.nasari.WeightedOverlap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mike on 4/18/18.
 */
public class NasariSynsetSimilarity implements SynsetComparator<String>{

    private static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    private static final File vectorFile = new File(IronsConfiguration.getInstance().getNASARIVectorFile());
    public static NasariSynsetSimilarity instance = null;
    private static NasariModel model;

    private NasariSynsetSimilarity(){
        if(vectorType.equals("unified")){
            model = NasariUnifiedModel.getInstance(vectorFile);
        }else if(vectorType.equals("lexical")){
            model = NasariLexicalModel.getInstance(vectorFile);
        }
    }

    public double compare(String synset1, String synset2){
        if(vectorType.equals("unified")) {
            return WeightedOverlap.compare(
                    (int[]) model.getVectors(convertSynsetIDToInt(synset1)),
                    (int[]) model.getVectors(convertSynsetIDToInt(synset2)));
        }else if(vectorType.equals("lexical")){
            return WeightedOverlap.compare(
                    (String[]) model.getVectors(convertSynsetIDToInt(synset1)),
                    (String[]) model.getVectors(convertSynsetIDToInt(synset2)));
        }
        System.out.println("");
        return 0D;
    }

    private int convertSynsetIDToInt(String synsetID){
        return Integer.parseInt(synsetID.substring(3,11));
    }

    public static synchronized NasariSynsetSimilarity getInstance(){
        if(instance == null){
            instance = new NasariSynsetSimilarity();
        }
        return instance;
    }
}
