package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.util.CoreNlpUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/16/18.
 */
public class MCSDisambiguatorTest {

    private MCSDisambiguator mcsDisambiguator = MCSDisambiguator.getInstance();
    private CoreNlpUtil coreNlpUtil = CoreNlpUtil.getInstance();

    private SemanticGraph testSG = coreNlpUtil.parseOneSentenceDoc("A mathematician found a solution to the problem.");
    @Test
    public void disambiguate() throws Exception {
        System.out.println(mcsDisambiguator.disambiguate(testSG));
    }

    @Test
    public void tagToBabelPOS() throws Exception {
    }

    @Test
    public void getInstance() throws Exception {
    }

}