package edu.udel.irl.irons;

import com.google.common.base.Stopwatch;
import edu.udel.irl.irons.impl.ReuterReader;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/16/18.
 */
public class IronsTest {

    private static final File DATASET = new File("/home/mike/Documents/corpus/documents.txt");
    private HashMap<Integer, String> shortArticles;

    public void readShortArticle(File shortArticleFile) throws IOException {
        this.shortArticles = new HashMap<>();
        List<String> lines = Files.readAllLines(shortArticleFile.toPath(), StandardCharsets.ISO_8859_1);
        String pattern = "\\(\\d+\\swords\\)";
        for (String line: lines){
//            System.out.println(line.split("\t")[0].replaceAll("\\.", ""));
            Integer index = Integer.parseInt(line.split("\t")[0].replaceAll("\\.", ""));
            String text = line.split("\t")[1].replaceAll(pattern, "");
            this.shortArticles.put(index, text);
        }
    }

    @Test
    public void test() throws Exception{
        Stopwatch stopwatch = Stopwatch.createStarted();
        Irons irons = new Irons(5);
        this.readShortArticle(DATASET);
        for (Map.Entry<Integer, String> entry : this.shortArticles.entrySet()) {
            irons.addDocument(String.valueOf(entry.getKey()), entry.getValue());
        }
        System.out.println("Reading documents done!");
        irons.processing();
        System.out.println("Processing finished!");
        irons.createIndex();
        irons.showBarcode();
        Irons.HCSClustering();
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));
    }

    @Test
    public void hcstest() throws IOException, ClassNotFoundException {
        Irons.HCSClustering();
    }

    @Test
    public void reutersTest() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        final String docFolder = "/home/mike/Documents/corpus/reuters/test/";

        ReuterReader reuterReader = new ReuterReader();
        Irons irons = new Irons(10);

        Object2ObjectMap<String, IntList> cdMap = reuterReader.getCat2DocMap();
        ReuterReader.trimDocs(500,10, cdMap);
        IntSet docList = new IntOpenHashSet();
        cdMap.values().forEach(docList::addAll);

        for(int docId: docList){
            String content = new String(Files.readAllBytes(Paths.get(docFolder + String.valueOf(docId))));
//            System.out.println(content);
            irons.addDocument(String.valueOf(docId), content);
        }

        System.out.println("All documents are read!");
        irons.processing();
        System.out.println("Processing finished!");
        irons.createIndex();
        irons.showBarcode();
        Irons.HCSClustering();
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MINUTES));
    }

    @Test
    public void HCSOnlyTest() throws IOException, ClassNotFoundException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Irons.HCSClustering();
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MINUTES));
    }

}