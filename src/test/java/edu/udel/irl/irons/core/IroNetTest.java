package edu.udel.irl.irons.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/14/18.
 */
public class IroNetTest {
    @Test
    public void createIndex() throws Exception {
        IroNode iroNode = new IroNode("1",1,"This is a test!", false);

        IroNet iroNet = new IroNet(6);
        iroNet.addNode(iroNode);

        iroNet.createIndex();

    }

    @Test
    public void test(){
        System.out.println(1D / (Math.E * 3));
    }

    public static void main(String[] args){
        IroNet iroNet = new IroNet(7);
        for (int i=1; i <= 7; i ++){
            iroNet.addNode(i);
        }

        iroNet.addEdge(1,2, 0.1);
        iroNet.addEdge(1,3, 0.1);
        iroNet.addEdge(3,2, 0.1);

        iroNet.addEdge(2,6, 0.2);
        iroNet.addEdge(2,5, 0.2);
        iroNet.addEdge(3,6, 0.2);
        iroNet.addEdge(3,5, 0.2);
        iroNet.addEdge(5,6, 0.2);

        iroNet.addEdge(3,4, 0.3);
        iroNet.addEdge(4,5, 0.3);

        iroNet.addEdge(6,7, 0.4);
        System.out.println("test");
        iroNet.finalizeIroNet();
        System.out.println(iroNet.getAnnotationBarcode());
    }


}