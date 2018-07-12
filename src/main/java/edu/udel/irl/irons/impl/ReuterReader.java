package edu.udel.irl.irons.impl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mike on 7/6/18.
 */
public class ReuterReader {
    private static final File catsFile = new File("/home/mike/Documents/corpus/reuters/cats.txt");
    private static final File testDocsFolder = new File("/home/mike/Documents/corpus/reuters/test/");
    private static final File trainingDocsFolder = new File("/home/mike/Documents/corpus/reuters/training/");
    private static final String docType = "test";
    private static final boolean useFullSize = false;

    private Int2ObjectMap<String> doc2CatMap;
    private Int2ObjectMap<List<String>> doc2CatsMap;
    private Int2ObjectMap<List<String>> doc2CatCatsMap;

    private Object2ObjectMap<String,IntList> cat2DocMap;
    private Object2ObjectMap<String,IntList> cat2DocsMap;
    private Object2ObjectMap<String,IntList> cat2DocDocsMap;

    public ReuterReader(){
        this.doc2CatMap = new Int2ObjectOpenHashMap<>();
        this.doc2CatsMap = new Int2ObjectOpenHashMap<>();
        this.doc2CatCatsMap = new Int2ObjectOpenHashMap<>();

        this.cat2DocMap = new Object2ObjectOpenHashMap<>();
        this.cat2DocsMap = new Object2ObjectOpenHashMap<>();
        this.cat2DocDocsMap = new Object2ObjectOpenHashMap<>();

        try {
            this.readCats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCats() throws IOException {
        List<String> lines = Files.readAllLines(catsFile.toPath());

        for(String line: lines){
            if(line.startsWith(docType) || useFullSize) {
                String[] seg = line.split(" ");

                int docId = Integer.parseInt(seg[0].split("/")[1]);

                List<String> cats = Arrays.asList(seg).subList(1, seg.length);

                //add item for doc as key maps
                this.doc2CatCatsMap.put(docId, cats);

                if(cats.size() == 1){
                    this.doc2CatMap.put(docId, cats.get(0));
                }else{
                    this.doc2CatsMap.put(docId, cats);
                }

                //add item for cat as key maps
                for(String cat: cats){
                    if(this.cat2DocDocsMap.containsKey(cat)){
                        this.cat2DocDocsMap.get(cat).add(docId);
                    }else{this.cat2DocDocsMap.put(cat, new IntArrayList(new int[]{docId}));}
                }

                if(cats.size() == 1){
                    if(this.cat2DocMap.containsKey(cats.get(0))){
                        this.cat2DocMap.get(cats.get(0)).add(docId);
                    }else{this.cat2DocMap.put(cats.get(0), new IntArrayList(new int[]{docId}));}
                }else{
                    for(String cat : cats){
                        if(this.cat2DocsMap.containsKey(cat)){
                            this.cat2DocsMap.get(cat).add(docId);
                        }else{
                            this.cat2DocsMap.put(cat, new IntArrayList(new int[]{docId}));
                        }
                    }
                }
            }
        }
    }

    public Int2ObjectMap<String> getDoc2CatMap(){return this.doc2CatMap;}

    public Int2ObjectMap<List<String>> getDoc2CatsMap(){return this.doc2CatsMap;}

    public Int2ObjectMap<List<String>> getDoc2CatCatsMap(){return this.doc2CatCatsMap;}

    public Object2ObjectMap<String, IntList> getCat2DocMap(){return this.cat2DocMap;}

    public Object2ObjectMap<String, IntList> getCat2DocsMap(){return this.cat2DocsMap;}

    public Object2ObjectMap<String, IntList> getCat2DocDocsMap(){return this.cat2DocDocsMap;}


    public static void trimDocs(int minimumDocSize, int minimumDocNumber, Object2ObjectMap<String, IntList> cdmap){
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory() && file.length() > minimumDocSize;
            }
        };

        IntList validDocs = new IntArrayList(Arrays
                .stream(testDocsFolder.listFiles(filter))
                .map(File::getName)
                .map(Integer::parseInt)
                .collect(Collectors.toList()));

        validDocs.addAll(Arrays
                .stream(trainingDocsFolder.listFiles(filter))
                .map(File::getName)
                .map(Integer::parseInt)
                .collect(Collectors.toList()));

        //Attn: has to new a instance of hashset of the keySet to avoid mis-behavior when you try to remove specific entry in a for loop
//        Set<String> cats = new HashSet<>(cdmap.keySet());
//        for(String cat: cats){
//
//            cdmap.get(cat).retainAll(validDocs);
//
//            if(cdmap.get(cat).size() < minimumDocNumber){
//                cdmap.remove(cat);
//            }
//        }
        Iterator<Map.Entry<String, IntList>> iterator = cdmap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, IntList> entry = iterator.next();
            entry.getValue().retainAll(validDocs);
            if(entry.getValue().size() < minimumDocNumber){
                iterator.remove();
            }
        }
    }

}
