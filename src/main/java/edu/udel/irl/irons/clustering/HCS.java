package edu.udel.irl.irons.clustering;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mike on 7/3/18.
 */
public class HCS {

    private Graph<Integer, DefaultWeightedEdge> similarityGraph;
    private List<IntList> clusters;

    private int count;

    public HCS(Graph<Integer, DefaultWeightedEdge> graph){
        this.similarityGraph = graph;
        this.clusters = new ArrayList<>();
        this.count = 0;
    }

    public void execute(){
        this.clusters.clear();
        IntSet singletons = new IntOpenHashSet();

        //get all singletons
        for(int vertex: this.similarityGraph.vertexSet()){
            if(this.similarityGraph.degreeOf(vertex) < 1){
                singletons.add(vertex);
            }
        }

        //remove all singletons
        this.similarityGraph.removeAllVertices(singletons);

        //find all maximally connected component
        ConnectivityInspector<Integer, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.similarityGraph);

        List<Set<Integer>> mccSets = inspector.connectedSets();

        System.out.println("connected component checked!");
        System.out.println(mccSets.size());

        //for each mcc graph do hcs
        for(Set<Integer> mccSet: mccSets){
            AsSubgraph<Integer, DefaultWeightedEdge> mccGraph = new AsSubgraph<>(this.similarityGraph,mccSet);

            if(isHighlyConnected(mccGraph)){
                this.clusters.add(new IntArrayList(mccGraph.vertexSet()));
            }else{
                hcs(mccGraph);
            }
        }
    }

    private void hcs(Graph<Integer, DefaultWeightedEdge> graph){
        //if graph is too small then remove it.
        if(graph.vertexSet().size() <= 2){
            return;
        }

        //initialize minimum cut
        StoerWagnerMinimumCut<Integer, DefaultWeightedEdge> minimumCut = new StoerWagnerMinimumCut<>(graph);
        Set<Integer> minCut = minimumCut.minCut();
        Set<Integer> maxCut = new HashSet<>(graph.vertexSet());
        maxCut.removeAll(minCut);

        //create subgraphs based on minimum cut
        AsSubgraph<Integer, DefaultWeightedEdge> subgraphA = new AsSubgraph<>(graph, minCut);
        AsSubgraph<Integer, DefaultWeightedEdge> subgraphB = new AsSubgraph<>(graph, maxCut);

        //recursively check is the subgraphs highly connected
        if(isHighlyConnected(subgraphA)){
            this.clusters.add(new IntArrayList(subgraphA.vertexSet()));
        }else{hcs(subgraphA);}

        if(isHighlyConnected(subgraphB)){
            this.clusters.add(new IntArrayList(subgraphB.vertexSet()));
        }else{hcs(subgraphB);}
    }

    private boolean isHighlyConnected(Graph<Integer, DefaultWeightedEdge> graph){
        this.count ++;
//        if(this.count%100 == 0) {
            System.out.println(count);
//        }
//        IntSet vertices = new IntOpenHashSet(graph.vertexSet());
//        int n = vertices.size();
        int n = graph.vertexSet().size();
        //cluster must have at least 3 vertices
//        if(graph.vertexSet().size() <= 2){return false;}
        if(n <= 2){return false;}

//        int k = Integer.MAX_VALUE;
        for(int vertex: graph.vertexSet()){
//            int degree = graph.degreeOf(vertex);

            if(graph.degreeOf(vertex) <= n / 4){
                return false;
            }
//            if(degree < k){
//                k = degree;
//            }
        }
        //TODO: NEED TO CHECK
        return true;
//        return (k > n / 4);
    }

    public List<IntList> getClusters(){return this.clusters;}
}
