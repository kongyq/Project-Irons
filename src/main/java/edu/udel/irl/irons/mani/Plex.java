package edu.udel.irl.irons.mani;

import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.Interval;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import gnu.trove.TDoubleArrayList;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import org.jgrapht.Graph;
import org.jgrapht.alg.clique.DegeneracyBronKerboschCliqueFinder;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Set;

/**
 * Created by mike on 4/25/18.
 */
public class Plex {

    private ExplicitSimplexStream stream;
    private AnnotatedBarcodeCollection annotatedBarcode;
    private BarcodeCollection barcode;

//    private Graph<Integer, DefaultWeightedEdge> graph;

    private int possibleMaxDimension;

    /**
     * initiate simplex stream with float value supported.
     */
    public Plex(){
        this.stream = new ExplicitSimplexStream(1.0D);

//        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void addVertex(int vertex){
        this.stream.addVertex(vertex);
//        this.graph.addVertex(vertex);
    }

    public void removeElement(int vertex1, int vertex2){
        this.stream.removeElementIfPresent(new int[]{vertex1, vertex2});
    }

    public void addElement(int vertex1, int vertex2, double filtrationValue){
        this.stream.addElement(new int[] {vertex1, vertex2}, filtrationValue);
//        this.graph.setEdgeWeight(graph.addEdge(vertex1, vertex2), filtrationValue);
    }

    public void addElement(int[] vertexes, double filtrationValue){
        this.stream.addElement(vertexes, filtrationValue);
    }

    public void finalizeStream(){
//        DegeneracyBronKerboschCliqueFinder<Integer, DefaultWeightedEdge> cliqueFinder =
//                new DegeneracyBronKerboschCliqueFinder<>(this.graph);
//
//        TObjectDoubleHashMap<Set<Integer>> cliques = new TObjectDoubleHashMap<>();
//
//        /**
//         * use jgrapht lib to get all maximum cliques
//         */
//        cliqueFinder.forEach(vertexSet ->{
//            TDoubleArrayList weights = new TDoubleArrayList();
//            AsSubgraph subgraph = new AsSubgraph(this.graph, vertexSet);
//
//            subgraph.edgeSet().forEach(edge -> weights.add(subgraph.getEdgeWeight(edge)));
//
//            cliques.put(vertexSet, weights.max()+0.1d);
//        });
//
//        TObjectDoubleIterator<Set<Integer>> iterator = cliques.iterator();
//
//        for (int i = cliques.size(); i-- > 0;){
//            iterator.advance();
//            this.stream.addElement(
//                    iterator.key().stream().mapToInt(Integer::intValue).toArray(),
//                    iterator.value());
//        }
//
////        this.stream.ensureAllFaces();

        //For test purpose only.
//        this.stream.validateVerbose();
//
//        if(!this.stream.validate()){
//            System.out.println("Stream invalid!");
//        }

        this.stream.finalizeStream();
    }

    public void computeAnnotatedBarcode(int minDimension, int maxDimension){
        IntAbsoluteHomology<Simplex> intAbsoluteHomology = new IntAbsoluteHomology<>(
                ModularIntField.getInstance(2),
                SimplexComparator.getInstance(),
                minDimension, maxDimension);
        this.annotatedBarcode = intAbsoluteHomology.computeAnnotatedIntervals(this.stream);
    }

    /**
     * customized barcode compute function for node to node similarity.
     * ONLY compute the 1-dimensional barcode and use Z=2 field.
     */
    public void computeBarcode(){
        IntAbsoluteHomology<Simplex> intAbsoluteHomology = new IntAbsoluteHomology<>(
                ModularIntField.getInstance(2),
                SimplexComparator.getInstance(),
                1, 2);
        this.barcode = intAbsoluteHomology.computeIntervals(this.stream);
    }

    public AnnotatedBarcodeCollection getAnnotatedBarcode(){
        return this.annotatedBarcode;
    }

    /**
     * used for maniScore computation
     * @return Interval<Double> type
     */
    public List<Interval<Double>> getIntervalsOfDim1(){
        this.computeBarcode();
        return this.barcode.getIntervalsAtDimension(1);
    }

    public BarcodeCollection getBarcode(){
        this.computeBarcode();
        return this.barcode;
    }

    /**
     * for test purpose only
     * @param args
     */
    public static void main(String[] args){
//        Plex plex = new Plex();
//        for (int i = 0; i < 10; i++){
//            plex.addVertex(i);
//        }
//        for (int i = 0; i < 10; i++){
//            for (int j = i + 1; j < 10; j++){
//                plex.addElement(i, j, (double)(i + j) /2);
//            }
//        }
//        plex.finalizeStream();
//        plex.computeAnnotatedBarcode(0, 4);
//        System.out.println(plex.getAnnotatedBarcode());


        Plex plex2 = new Plex();
        for (int i = 1; i < 8; i++){plex2.addVertex(i);}

        plex2.addElement(1, 2, 1.0d);
        plex2.addElement(2, 3, 2.0d);
        plex2.addElement(1, 3, 1.0d);
        plex2.addElement(2, 6, 4.0d);
        plex2.addElement(2, 5, 4.0d);
        plex2.addElement(3, 6, 4.0d);
        plex2.addElement(3, 5, 4.0d);
        plex2.addElement(3, 4, 2.0d);
        plex2.addElement(5, 6, 4.0d);
        plex2.addElement(6, 7, 3.0d);
        plex2.addElement(5, 4, 2.0d);

        plex2.finalizeStream();
        plex2.computeAnnotatedBarcode(0, 4);
        System.out.println(plex2.getAnnotatedBarcode());
    }
}
