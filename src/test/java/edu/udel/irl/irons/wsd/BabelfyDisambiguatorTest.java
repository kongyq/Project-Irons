package edu.udel.irl.irons.wsd;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.util.CoreNlpUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/8/18.
 */
public class BabelfyDisambiguatorTest {

    BabelfyDisambiguator babelfyDisambiguator = BabelfyDisambiguator.getInstance();
    CoreNlpUtil coreNlpUtil = CoreNlpUtil.getInstance();

//    SemanticGraph testSG = coreNlpUtil.parseOneSentenceDoc("A mathematician found a solution to the problem.");
//    SemanticGraph testSG = coreNlpUtil.parseOneSentenceDoc("She is a beautiful woman with a golden hair and a pair of charming eyes.");
    SemanticGraph testSG = coreNlpUtil.parseOneSentenceDoc("Babelfy has received funding from the European Research Council.");
    @Test
    public void tagToPosTag() throws Exception {
    }

    @Test
    public void disambiguate() throws Exception {
        System.out.println(babelfyDisambiguator.disambiguate(testSG));

    }

}