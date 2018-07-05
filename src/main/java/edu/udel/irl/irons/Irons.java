package edu.udel.irl.irons;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.clustering.HCS;
import edu.udel.irl.irons.core.IroNet;
import edu.udel.irl.irons.core.IroNode;
import edu.udel.irl.irons.mani.CoinSaver;
import edu.udel.irl.irons.mani.CoreMani;
import edu.udel.irl.irons.util.CoreNlpUtil;
import edu.udel.irl.irons.util.IndexReader;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mike on 5/16/18.
 */
public class Irons {

    private static final int numofThread = IronsConfiguration.getInstance().getNumberofThread();

    private IroNet iroNet;
    private CoreMani coreMani;
    private double sensitiveness = IronsConfiguration.getInstance().getSensitiveness();
    private CoreNlpUtil coreNlpUtil;

    public Irons(int k){
        this.iroNet = new IroNet(k);
        this.coreNlpUtil = CoreNlpUtil.getInstance();
//        this.coreMani = new CoreMani(disambiguator, synsetComparator);
    }

    public void addDocument(String docId, String content){
//        CoreNlpUtil coreNlpUtil = new CoreNlpUtil(content);
        TIntObjectIterator<SemanticGraph> iterator = coreNlpUtil.getSentList(content).iterator();
        while (iterator.hasNext()){
            iterator.advance();
            IroNode iroNode = new IroNode(docId, iterator.key(), iterator.value(), false);
            this.iroNet.addNode(iroNode);
        }
    }

    public void processing() throws Exception {

        CoinSaver.getInstance().readFromFile();

        ExecutorService executor = Executors.newFixedThreadPool(numofThread);
//        ExecutorService executor = Executors.newCachedThreadPool();

        int numberOfNodes = this.iroNet.nodeList.size();

        for (int i = 1; i <= numberOfNodes; i ++){
            System.out.format("%d / %d%n", i, numberOfNodes);
            System.out.flush();
            for (int j = i + 1; j <= numberOfNodes; j ++){
                Runnable worker = new IronsWorker(
                        this.iroNet.nodeList.get(i),
                        this.iroNet.nodeList.get(j),
                        this.iroNet,
                        i,j);

                executor.execute(worker);


//                double similarity = this.coreMani.computeEdgeWeight(
//                        this.iroNet.nodeList.get(i),
//                        this.iroNet.nodeList.get(j));
//
//                // (sensitiveness * similarity) has to be great than or equal to 1.0
//                // then use reciprocal as their edge's filtration value.
//                double threshold = this.sensitiveness / similarity;
////                double threshold = 1D / (this.sensitiveness * similarity);
//                if (threshold <= 1D){
//                    this.iroNet.addEdge(
//                            this.iroNet.nodeList.get(i),
//                            this.iroNet.nodeList.get(j),
//                            threshold);
//
//                }
            }
        }

        executor.shutdown();

        while (!executor.isTerminated()){
            Thread.yield();
        }

        CoinSaver.getInstance().writeToFile();

        this.iroNet.finalizeIroNet();
    }

    public void showBarcode() throws IOException {
        System.out.println(this.iroNet.getAnnotationBarcode());
    }

    public void createIndex() throws IOException {
        this.iroNet.createIndex();
        this.iroNet.createBarcode();
        //save irons graph for clustering
        this.iroNet.saveGraph();
    }

    public static void HCSClustering() throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(new File(IronsConfiguration.getInstance().getGraphPath()));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Graph<Integer, DefaultWeightedEdge> graph = (SimpleWeightedGraph) objectInputStream.readObject();
        objectInputStream.close();

        HCS hcs = new HCS(graph);
        hcs.execute();

        List<IntSet> clusters = hcs.getClusters();

        File clusterFile = new File(IronsConfiguration.getInstance().getClustersPath());
        clusterFile.getParentFile().mkdir();
        clusterFile.createNewFile();
        BufferedWriter writer = Files.newBufferedWriter(clusterFile.toPath());

        for(IntSet cluster: clusters){
            System.out.println(cluster);
            TObjectIntHashMap<String> docFreqMap = new TObjectIntHashMap<>();
            for(int nodeId: cluster){
                docFreqMap.adjustOrPutValue(IndexReader.getInstance().getDocId(nodeId),1,1);
            }
            writer.write(docFreqMap.toString() + "\n");
        }
        writer.close();
    }
}
