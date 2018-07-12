package edu.udel.irl.irons.impl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by mike on 7/6/18.
 */
public class ReuterReaderTest {

    ReuterReader reuterReader = new ReuterReader();

    @Test
    public void getDoc2CatMap() throws Exception {
        System.out.println(reuterReader.getDoc2CatMap());
        System.out.println(reuterReader.getDoc2CatMap().size());
    }

    @Test
    public void getDoc2CatsMap() throws Exception {
        System.out.println(reuterReader.getDoc2CatsMap());
        System.out.println(reuterReader.getDoc2CatsMap().size());
    }

    @Test
    public void getDoc2CatCatsMap() throws Exception {
        System.out.println(reuterReader.getDoc2CatCatsMap());
        System.out.println(reuterReader.getDoc2CatCatsMap().size());
    }

    @Test
    public void getCat2DocMap() throws Exception {
//        System.out.println(reuterReader.getCat2DocMap());
        System.out.println(reuterReader.getCat2DocMap().size());
        System.out.println(reuterReader.getCat2DocMap().values().stream().mapToInt(IntList::size).sum());

        ReuterReader.trimDocs(500, 10, reuterReader.getCat2DocMap());

        System.out.println(reuterReader.getCat2DocMap().values().stream().mapToInt(IntList::size).sum());
        System.out.println(reuterReader.getCat2DocMap().size());

        IntSet test = new IntOpenHashSet();
        reuterReader.getCat2DocMap().values().forEach(test::addAll);
//        reuterReader.getCat2DocMap().values().stream().map(test::addAll);
        System.out.println(test.size());

        System.out.println(reuterReader.getCat2DocMap().values().stream().mapToInt(IntList::size).filter(number-> number >= 10 && number <= 361).sum());
        System.out.println(reuterReader.getCat2DocMap().values().stream().mapToInt(IntList::size).filter(number-> number >= 10 && number <= 371).count());

    }

    @Test
    public void getCat2DocsMap() throws Exception {
        System.out.println(reuterReader.getCat2DocsMap());
        System.out.println(reuterReader.getCat2DocsMap().size());
    }

    @Test
    public void getCat2DocDocsMap() throws Exception {
        System.out.println(reuterReader.getCat2DocDocsMap());
        System.out.println(reuterReader.getCat2DocDocsMap().size());
    }

}