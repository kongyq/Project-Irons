package edu.udel.irl.irons;

import edu.udel.irl.irons.core.IroNet;
import edu.udel.irl.irons.core.IroNode;
import edu.udel.irl.irons.mani.CoreMani;
import edu.udel.irl.irons.synsim.SynsetComparator;
import edu.udel.irl.irons.wsd.Disambiguator;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by mike on 6/1/18.
 */
public class IronsWorker implements Runnable {

    private static final double sensitiveness = IronsConfiguration.getInstance().getSensitiveness();
    private IroNet iroNet;
    private int nodeId_i;
    private int nodeId_j;

    public IronsWorker(int nodeId_i, int nodeId_j, IroNet iroNet){
        this.iroNet = iroNet;
        this.nodeId_i = nodeId_i;
        this.nodeId_j = nodeId_j;
    }

    public void run(){

        double maniScore = 0D;
        CoreMani coreMani = new CoreMani();

        try {
            maniScore = coreMani.computeEdgeWeight(
                    this.iroNet.nodeList.get(nodeId_i),
                    this.iroNet.nodeList.get(nodeId_j));
        } catch (Exception e) {
            e.printStackTrace();
        }

        double similarity = sensitiveness / maniScore;

        if(similarity <= 1D){
            this.iroNet.addEdge(nodeId_i, nodeId_j, similarity);
        }

        System.out.format("%d, %d%n", nodeId_i, nodeId_j);
    }
}
