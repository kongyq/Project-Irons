package edu.udel.irl.irons.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/16/18.
 */
public class IroNodeTest {
    @Test
    public void getContent() throws Exception {
    }

    @Test
    public void getNodeID() throws Exception {

        IroNode node1 = new IroNode("1", 1,"1", false);
        IroNode node2 = new IroNode("2", 1,"2", false);
        IroNode node3 = new IroNode("3", 1,"3", false);
        assertEquals(1, node1.getNodeID());
        assertEquals(2, node2.getNodeID());
        assertEquals(3, node3.getNodeID());
    }

    @Test
    public void getDocID() throws Exception {
    }

    @Test
    public void getSentID() throws Exception {
    }

    @Test
    public void isSegmentation() throws Exception {
    }

}