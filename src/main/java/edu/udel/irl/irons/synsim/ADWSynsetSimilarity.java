package edu.udel.irl.irons.synsim;

import it.uniroma1.lcl.adw.ADW;
import it.uniroma1.lcl.adw.DisambiguationMethod;
import it.uniroma1.lcl.adw.ItemType;
import it.uniroma1.lcl.adw.comparison.SignatureComparison;
import it.uniroma1.lcl.adw.comparison.WeightedOverlap;

import java.util.List;

/**
 * Created by mike on 4/18/18.
 */
public class ADWSynsetSimilarity implements SynsetComparator<List<String>>{
    private ADW pipeline;
    private SignatureComparison measure;

    public ADWSynsetSimilarity(){
        this.pipeline = new ADW();
        this.measure = new WeightedOverlap();
    }

    public double compare(String synset1, String synset2){
        ItemType wordType = ItemType.SENSE_OFFSETS;
        return this.pipeline.getPairSimilarity(
                synset1.substring(0,8) + "-" + synset1.substring(8),
                synset2.substring(0,8) + "-" + synset2.substring(8),
                DisambiguationMethod.ALIGNMENT_BASED,
                this.measure,
                wordType, wordType);
    }

    public double compare(List<String> synsetList1, List<String> synsetList2){
        double score = 0D;
        for (String synset1: synsetList1){
            for (String synset2: synsetList2){
                double subscore = this.compare(synset1, synset2);
                if (subscore > score){
                    score = subscore;
                }
            }
        }
        if (score >= 1D){
            return 1D;
        }else {
            return score;
        }
    }
}
