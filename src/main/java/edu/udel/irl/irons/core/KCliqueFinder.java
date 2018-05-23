package edu.udel.irl.irons.core;

/**
 * Created by mike on 5/7/18.
 */
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.jgrapht.Graph;

import java.util.*;

public class KCliqueFinder {

    private Graph graph;
    private int cliqueSize;

    private HashSet<TIntArrayList> allCliquesToK;

//    private TIntObjectHashMap<Set<TIntArrayList>> test;
    private List<TIntArrayList> cliques;

    public KCliqueFinder(Graph graph , int cliqueSize ) {
        this.graph = graph;
        this.cliqueSize = cliqueSize;
        this.cliques = new ArrayList();
//        this.test = new TIntObjectHashMap<>();
        this.allCliquesToK = new HashSet<>();

        doCliqueBT(new TIntArrayList(), 0);
    }

    private boolean isConnected(int i, int j) { return graph.containsEdge(i,j); }

    private void doCliqueBT(TIntArrayList aClique, int k) {
        if (k == cliqueSize) {
            this.cliques.add(aClique);
            return;
        } else {
            k = k + 1;
            List<TIntArrayList> currentKCliques = new ArrayList();
            if (k <= cliqueSize) { currentKCliques = getCandidates(aClique); }

            if (!currentKCliques.isEmpty()) {

                for (TIntArrayList clique: currentKCliques){
                    doCliqueBT(clique, k);
                }

                /**
                 * Only list k-cliques where k > 2.
                 * for performance purpose.
                 */
                if (k > 2) {
                    this.allCliquesToK.addAll(currentKCliques);

//                    if(!this.test.containsKey(k)){
//                        this.test.put(k, new HashSet<>(currentKCliques));
//                    }else {
//                        this.test.get(k).addAll(new HashSet<>(currentKCliques));
//                    }

                }
            }

        }
    }

    private List<TIntArrayList> getCandidates(TIntArrayList A) {
        List<TIntArrayList> candidates = new ArrayList();
        if (A.isEmpty()) {
            for (int i = 1; i <= this.graph.vertexSet().size(); i++) {
                TIntArrayList sj = new TIntArrayList(1);
                sj.add(i);
                candidates.add(sj);
            }

        } else {

            int q = A.get(A.size()-1);
            for (int j = q; j < this.graph.vertexSet().size(); j++) {
                boolean allConnected = true;

                TIntIterator iterator = A.iterator();
                while (iterator.hasNext()) {
                    int i = iterator.next();
                    if (!isConnected(i,j)) {
                        allConnected = false;
                        break;
                    }
                }
                if (allConnected) {
                    TIntArrayList sj = new TIntArrayList();
                    sj.addAll(A);
                    sj.add(j);
                    candidates.add(sj);
                }
            }
        }
        return candidates;
    }

    public List getCliques(){
        return this.cliques;
    }

//    public TIntObjectHashMap<Set<TIntArrayList>> getAllCliquesToK(){return this.test;}

    public HashSet<TIntArrayList> getAllCliquesToK(){return this.allCliquesToK;}

}