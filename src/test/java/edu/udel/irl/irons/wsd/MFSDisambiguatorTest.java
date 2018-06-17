package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.util.CoreNlpUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/15/18.
 */
public class MFSDisambiguatorTest {

    MFSDisambiguator mfsDisambiguator = MFSDisambiguator.getInstance();
    CoreNlpUtil coreNlpUtil = CoreNlpUtil.getInstance();

    SemanticGraph testSG = coreNlpUtil.parseOneSentenceDoc("A mathematician found a solution to the problem.");

    @Test
    public void disambiguate() throws Exception {
        System.out.println(mfsDisambiguator.disambiguate(testSG));


    }

}