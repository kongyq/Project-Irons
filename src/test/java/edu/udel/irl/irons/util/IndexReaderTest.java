package edu.udel.irl.irons.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/17/18.
 */
public class IndexReaderTest {
    @Test
    public void showIronode() throws Exception {

        IndexReader indexReader = new IndexReader();
        indexReader.showIronode(11);
        indexReader.showIronode(24);
        indexReader.showIronode(50);
        indexReader.showIronode(34);
//        indexReader.showIronode(102);
    }

}