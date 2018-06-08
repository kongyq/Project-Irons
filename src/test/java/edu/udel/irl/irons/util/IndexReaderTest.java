package edu.udel.irl.irons.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/17/18.
 */
public class IndexReaderTest {
    @Test
    public void showIronode() throws Exception {

        IndexReader.getInstance().showIronode(11);
        IndexReader.getInstance().showIronode(24);
        IndexReader.getInstance().showIronode(50);
        IndexReader.getInstance().showIronode(34);
//        indexReader.showIronode(102);
    }

}