package edu.udel.irl.irons;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        System.out.println(stopwatch.elapsed(TimeUnit.MINUTES));
    }

    @Test
    public void hcstest() throws IOException, ClassNotFoundException {
        Irons.HCSClustering();
    }

}