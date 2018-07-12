package edu.udel.irl.irons.impl;

import edu.udel.irl.irons.Irons;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by mike on 7/7/18.
 */
public class ReuterClusteringRun {

    private static final String docFolder = "/home/mike/Documents/corpus/reuters/test/";

    private ReuterReader reuterReader;
    private Irons irons;

    public ReuterClusteringRun(){
        this.reuterReader = new ReuterReader();
        this.irons = new Irons(10);
        try {
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {

        Object2ObjectMap<String, IntList> cdMap = this.reuterReader.getCat2DocMap();
        ReuterReader.trimDocs(500,10, cdMap);
        IntSet docList = new IntOpenHashSet();
        cdMap.values().forEach(docList::addAll);

        for(int docId: docList){
            String content = new String(Files.readAllBytes(Paths.get(docFolder + String.valueOf(docId))));
            System.out.println(content);
            this.irons.addDocument(String.valueOf(docId), content);
        }
    }


}
