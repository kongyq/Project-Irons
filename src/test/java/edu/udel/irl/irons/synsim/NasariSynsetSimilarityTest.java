package edu.udel.irl.irons.synsim;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/18/18.
 */
public class NasariSynsetSimilarityTest {

    NasariSynsetSimilarity instance = NasariSynsetSimilarity.getInstance();

    @Test
    public void getDimensions() throws Exception {
        System.out.println(instance.getDimensions("bn:00000001n"));
    }

    @Test
    public void compare() throws Exception {
    }

    @Test
    public void getInstance() throws Exception {
    }

}