package edu.udel.irl.irons.clustering;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 7/3/18.
 */
public class HCSTest {

    @Test
    public void execute() throws Exception {
        SimpleWeightedGraph<Integer, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for(int i = 1; i <= 12; ++i) {
            g.addVertex(i);
        }

        DefaultWeightedEdge e;
        e = g.addEdge(1, 2);
        g.setEdgeWeight(e, 3.0);
        e = g.addEdge(1, 11);
        g.setEdgeWeight(e, 2.0);
        e = g.addEdge(1, 12);
        g.setEdgeWeight(e, 4.0);
        e = g.addEdge(2, 3);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(2, 12);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(3, 12);
        g.setEdgeWeight(e, 3.0);
        e = g.addEdge(3, 11);
        g.setEdgeWeight(e, 2.0);
        e = g.addEdge(3, 4);
        g.setEdgeWeight(e, 4.0);
        e = g.addEdge(12, 11);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(11, 10);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(4, 5);
        g.setEdgeWeight(e, 3.0);
        e = g.addEdge(5, 6);
        g.setEdgeWeight(e, 2.0);
        e = g.addEdge(4, 6);
        g.setEdgeWeight(e, 4.0);
        e = g.addEdge(4, 10);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(6, 7);
        g.setEdgeWeight(e, 1.0);

        e = g.addEdge(10, 7);
        g.setEdgeWeight(e, 3.0);
        e = g.addEdge(7, 8);
        g.setEdgeWeight(e, 2.0);
        e = g.addEdge(8, 9);
        g.setEdgeWeight(e, 4.0);
        e = g.addEdge(10, 9);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(9, 7);
        g.setEdgeWeight(e, 1.0);
        e = g.addEdge(10, 8);
        g.setEdgeWeight(e, 3.0);

        HCS hcs = new HCS(g);
        hcs.execute();

        hcs.getClusters().forEach(System.out::println);
    }

    @Test
    public void getClusters() throws Exception {
    }

}