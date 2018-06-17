package edu.udel.irl.irons.synsim;

import edu.udel.irl.irons.IronsConfiguration;

import java.io.File;

/**
 * Created by mike on 4/18/18.
 */
public class NasariSynsetSimilarity implements SynsetComparator<String>{

    public static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    public static final File vectorFile = new File(IronsConfiguration.getInstance().getNASARIVectorFile());
    public static NasariSynsetSimilarity instance = null;


    private NasariSynsetSimilarity(){
        //TODO: need to be finished


    }

    public double compare(String synset1, String synset2){
        return 0D;
    }


    public static synchronized NasariSynsetSimilarity getInstance(){
        if(instance == null){
            instance = new NasariSynsetSimilarity();
        }
        return instance;
    }
}
