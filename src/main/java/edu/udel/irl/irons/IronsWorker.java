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

    private double sensitiveness = IronsConfiguration.getInstance().getSensitiveness();
    private IroNode node1;
    private IroNode node2;
    private CoreMani coreMani;
    private IroNet iroNet;
    private int i;
    private int j;

    public IronsWorker(IroNode node1, IroNode node2, IroNet iroNet, int i, int j) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.node1 = node1;
        this.node2 = node2;
        this.coreMani = new CoreMani();
        this.iroNet = iroNet;
        this.i = i;
        this.j = j;
    }
//
//    public IronsWorker(IroNode node1, IroNode node2, Disambiguator disambiguator, SynsetComparator synsetComparator, IroNet iroNet, int i, int j){
//        this.node1 = node1;
//        this.node2 = node2;
//        this.coreMani = new CoreMani(disambiguator, synsetComparator);
//        this.iroNet = iroNet;
//        this.i = i;
//        this.j = j;
//    }

    public void run() {
        double similarity = 0;
        try {
            similarity = this.coreMani.computeEdgeWeight(this.node1, this.node2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // (sensitiveness * similarity) has to be great than or equal to 1.0
        // then use reciprocal as their edge's filtration value.
        double threshold = this.sensitiveness / similarity;
//                double threshold = 1D / (this.sensitiveness * similarity);
        if (threshold <= 1D) {
            this.iroNet.addEdge(this.node1, this.node2, threshold);
        }
        System.out.format("%d, %d%n", this.i, this.j);
    }
}
