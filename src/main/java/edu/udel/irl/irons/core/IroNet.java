package edu.udel.irl.irons.core;

import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.mani.Plex;
import gnu.trove.impl.sync.TSynchronizedObjectDoubleMap;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import gnu.trove.set.hash.TIntHashSet;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mike on 3/26/18.
 */
public class IroNet {

    public TIntObjectHashMap<IroNode> nodeList;
    private TObjectDoubleHashMap<TIntHashSet> edgeList;

    private Plex plex;
    private int k;

    private Graph<Integer, DefaultWeightedEdge> graph;

    private static final File indexFile = new File(IronsConfiguration.getInstance().getIndexPath());
    private static final File barcodeFile = new File(IronsConfiguration.getInstance().getBarcodePath());
    private static final File graphFile = new File(IronsConfiguration.getInstance().getGraphPath());

    public IroNet(int k){
        this.k = k;
        this.nodeList = new TIntObjectHashMap<>();
        this.edgeList = new TObjectDoubleHashMap<>();
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.plex = new Plex();
    }

    public synchronized void addNode(IroNode node){
        this.graph.addVertex(node.getNodeID());
        this.nodeList.put(node.getNodeID(), node);
        this.plex.addVertex(node.getNodeID());
    }

    //for test purpose only
    public synchronized void addNode(int nodeId){
        this.graph.addVertex(nodeId);
        this.plex.addVertex(nodeId);
        this.nodeList.put(nodeId, new IroNode(null, 0, "", false));
    }

    //All remove methods are not tested and may not be used in the future, just as the in case solution.
    public synchronized void removeNode(IroNode node){
        this.nodeList.remove(node.getNodeID());
        this.graph.removeVertex(node.getNodeID());
    }

    //for test purpose only
    public synchronized void removeNode(int nodeID){
        this.graph.removeVertex(nodeID);
        this.nodeList.remove(nodeID);
    }

    public synchronized void addEdge(int node1Id, int node2Id, double weight){
        this.edgeList.putIfAbsent(new TIntHashSet(new int[]{node1Id, node2Id}), weight);
        this.graph.setEdgeWeight(this.graph.addEdge(node1Id, node2Id), weight);
        this.plex.addElement(node1Id, node2Id, weight);
    }

    public synchronized void addEdge(IroNode node1, IroNode node2, double weight){
        this.edgeList.putIfAbsent(new TIntHashSet(new int[]{node1.getNodeID(), node2.getNodeID()}), weight);
        this.graph.setEdgeWeight(this.graph.addEdge(node1.getNodeID(), node2.getNodeID()), weight);
        this.plex.addElement(node1.getNodeID(), node2.getNodeID(),weight);
    }

    //for conventional coding, may never use.
    public synchronized void removeEdge(int node1Id, int node2Id){
        this.plex.removeElement(node1Id, node2Id);
        this.graph.removeEdge(node1Id, node2Id);
        this.edgeList.remove(new TIntHashSet(new int[]{node1Id, node2Id}));
    }

    /** CORE:ALG: finalize the simplex stream by transfer all k-clique faces into corresponding k-simplex and add
     * them into the stream.
     *
     */
    public void finalizeIroNet(){
        KCliqueFinder kCliqueFinder = new KCliqueFinder(this.graph, this.k);
        Set<TIntArrayList> allKCliquesToK = kCliqueFinder.getAllCliquesToK();
        for (TIntArrayList kClique: allKCliquesToK){

            // add a epsilon value to the start time of each face, in order to separate dimensions.
            int dimEpsilon = kClique.size() - 2 ;

            TDoubleArrayList weights = new TDoubleArrayList();
            AsSubgraph subgraph = new AsSubgraph(
                    this.graph,
                    Arrays.stream(kClique.toArray()).boxed().collect(Collectors.toSet()));

            // compute the maximum filtration value (latest appeared) of all faces.
            subgraph.edgeSet().forEach(edge -> weights.add(subgraph.getEdgeWeight(edge)));

            this.plex.addElement(kClique.toArray(), weights.max() + dimEpsilon);
//            this.plex.addElement(kClique.toArray(), weights.max());
        }

        this.plex.finalizeStream();
    }

    public TIntObjectHashMap<IroNode> getNodeList(){return this.nodeList;}

    public TObjectDoubleHashMap<TIntHashSet> getEdgeList(){return this.edgeList;}

    public AnnotatedBarcodeCollection getAnnotationBarcode(){
        // maximum dimension always less 1 than maximum k.
        this.plex.computeAnnotatedBarcode(1, this.k-1);
        return this.plex.getAnnotatedBarcode();
    }

    public void createIndex() throws IOException {
        //create the index file if not exist.
        indexFile.getParentFile().mkdir();
        indexFile.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(indexFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        this.nodeList.writeExternal(objectOutputStream);
    }

    public void createBarcode() throws IOException {
        //create the barcode file if not exist.
        barcodeFile.getParentFile().mkdir();
        barcodeFile.createNewFile();

        //store current System.out before assign to new value.
        PrintStream console = System.out;

        PrintStream out = new PrintStream(new FileOutputStream(barcodeFile));
        System.setOut(out);
        System.out.println(this.getAnnotationBarcode());

        //redirect to console output
//        out = new PrintStream(new FileOutputStream(FileDescriptor.out));
        System.setOut(console);
    }

    public Graph<Integer, DefaultWeightedEdge> getGraph(){return this.graph;}

    public void saveGraph() throws IOException {
        graphFile.getParentFile().mkdir();
        graphFile.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(graphFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.graph);
        objectOutputStream.flush();
        objectOutputStream.close();
    }
}
