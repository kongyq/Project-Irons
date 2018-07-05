package edu.udel.irl.irons.synsim;

import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.nasari.NasariLexicalModel;
import edu.udel.irl.irons.nasari.NasariModel;
import edu.udel.irl.irons.nasari.NasariUnifiedModel;
import edu.udel.irl.irons.nasari.WeightedOverlap;

import java.io.*;
import java.util.List;

/**
 * Created by mike on 4/18/18.
 */
public class NasariSynsetSimilarity implements SynsetComparator<List<String>>{

    private static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    private static final File vectorFile = new File(IronsConfiguration.getInstance().getNASARIVectorFile());
    public static NasariSynsetSimilarity instance = null;
    private static NasariModel model;

    private NasariSynsetSimilarity(){
        if(vectorType.equals("unified")){
            model = NasariUnifiedModel.getInstance(vectorFile);
        }else if(vectorType.equals("lexical")){
            model = NasariLexicalModel.getInstance(vectorFile);
        }else{
            System.out.println("Wrong vector file type! Please check configuration in irons.properties.");
            System.exit(1);
        }
    }

    public double compare(List<String> synsetList1, List<String> synsetList2){
        double score = 0D;
        for(String synset1: synsetList1){
            for(String synset2: synsetList2){
                double subScore = this.compare(synset1, synset2);
                if(subScore > score){
                    score = subScore;
                }
            }
        }
        if(score > 1D){return 1D;}
        else {return score;}
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
