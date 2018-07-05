package edu.udel.irl.irons.synsim;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/18/18.
 */
public class NasariSynsetSimilarityTest {

    NasariSynsetSimilarity synsetSimilarity = NasariSynsetSimilarity.getInstance();

    @Test
    public void compare() throws Exception {
        System.out.println(synsetSimilarity.compare("bn:00000004n","bn:70000005n"));
    }

}