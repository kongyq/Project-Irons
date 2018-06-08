package edu.udel.irl.irons.util;

import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.core.IroNode;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.*;

/**
 * Created by mike on 5/17/18.
 */
public class IndexReader {
    private static IndexReader instance = null;
    private static final File indexFile = new File(IronsConfiguration.getInstance().getIndexPath());
    public TIntObjectHashMap<IroNode> nodeList;

    private IndexReader() throws IOException, ClassNotFoundException {
        this.nodeList = new TIntObjectHashMap<>();
        if(indexFile.exists()){
            FileInputStream fileInputStream = new FileInputStream(indexFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.nodeList.readExternal(objectInputStream);

        }else{
            System.out.println("ironodes index file does not exist!");
        }

    }

    public static synchronized IndexReader getInstance() throws IOException, ClassNotFoundException {
        if (instance == null){
            instance = new IndexReader();
        }
        return instance;
    }

    public String getDocId(int nodeId){
        return this.nodeList.get(nodeId).getDocID();
    }

    public int getSentId(int nodeId){
        return this.nodeList.get(nodeId).getSentID();
    }

    public String getContent(int nodeId){
        return this.nodeList.get(nodeId).getContent();
    }

    public void showIronode(int indexNumber){
        IroNode iroNode = this.nodeList.get(indexNumber);
        System.out.format("NodeID: %s, DocID: %s, SentID: %d, Content: %s%n",
                iroNode.getNodeID(),
                iroNode.getDocID(),
                iroNode.getSentID(),
                iroNode.getContent());
    }



}
