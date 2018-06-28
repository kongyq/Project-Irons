package edu.udel.irl.irons.synsim;

import edu.udel.irl.irons.IronsConfiguration;
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

    public static final String vectorType = IronsConfiguration.getInstance().getNASARIVectorType();
    public static final File vectorFile = new File(IronsConfiguration.getInstance().getNASARIVectorFile());
    public static NasariSynsetSimilarity instance = null;

    private Map<String, TIntObjectHashMap<String>> vectors;

    private NasariSynsetSimilarity(){
        //TODO: need to be finished
        this.vectors = new HashMap<>();
        this.loadVectorFile();

    }

    private void loadVectorFile(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(vectorFile));

            while(br.ready()){
                String line = br.readLine();
                String[] lineSplited = line.split("\t");

                String synset = lineSplited[0];
                TIntObjectHashMap<String> dimensions = new TIntObjectHashMap<>();

                for(int i = 2; i < lineSplited.length; i++){
                    dimensions.put(i - 1, lineSplited[i].split("_")[0]);
                }
                this.vectors.put(synset, dimensions);
//                break;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TIntObjectHashMap<String> getDimensions(String synset){
        return this.vectors.get(synset);
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
