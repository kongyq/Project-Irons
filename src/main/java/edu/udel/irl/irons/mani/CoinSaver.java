package edu.udel.irl.irons.mani;

import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.core.IroNode;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;
import java.util.List;

/**
 * Created by mike on 5/15/18.
 * This class only used as a babelNet coin saver. This may be deprecated in the future version
 */
public class CoinSaver {

    private static CoinSaver instance = null;

    private static final File cacheFile = new File(IronsConfiguration.getInstance().getBabelfycache());

    private volatile TIntObjectHashMap<TIntObjectHashMap<List<String>>> wsdedSentences;

    public static synchronized CoinSaver getInstance(){
        if (instance == null){
            instance = new CoinSaver();
        }
        return instance;
    }

    private CoinSaver(){
        this.wsdedSentences = new TIntObjectHashMap<>();
    }

    public synchronized void addSentSenses(IroNode node, TIntObjectHashMap<List<String>> sentSenses){
        this.wsdedSentences.putIfAbsent(node.getNodeID(), sentSenses);
    }

    public boolean isContain(IroNode node){
        if (this.wsdedSentences.containsKey(node.getNodeID())){
            return true;
        }else{
            return false;
        }
    }

    public TIntObjectHashMap getSentSenses(IroNode node){
        return this.wsdedSentences.get(node.getNodeID());
    }

    public void writeToFile() throws IOException {
        cacheFile.getParentFile().mkdir();
        cacheFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        this.wsdedSentences.writeExternal(objectOutputStream);
    }

    public void readFromFile() throws IOException, ClassNotFoundException {
        if (cacheFile.exists()){
            FileInputStream fileInputStream = new FileInputStream(cacheFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.wsdedSentences.readExternal(objectInputStream);
        }
    }
}
