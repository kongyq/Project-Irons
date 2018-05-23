package edu.udel.irl.irons.mani;

import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.simple.Sentence;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 5/8/18.
 */
public class CoreManiTest {
    @Test
    public void getAllAdjacentNodePairs2() throws Exception {

        String test = new String("Most of them fled Afghanistan after the US-led offensive, but officials from the Patriotic Union of Kurdistan (PUK), which controls part of north-east Iraq, claim an \"abnormal\" number of recruits are making their way to the area from Jordan, Syria and Egypt.");
        Sentence sentence = new Sentence(test);

        System.out.println(CoreMani.getAllAdjacentNodePairs(sentence.dependencyGraph(SemanticGraphFactory.Mode.BASIC)));

    }

    @Test
    public void getAllAdjacentNodePairs3() throws Exception {
    }

    @Test
    public void parseSent() throws Exception {
    }

    @Test
    public void tagToPOS() throws Exception {
    }

    @Test
    public void getAllAdjacentNodePairs() throws Exception {
    }

    @Test
    public void getAllAdjacentNodePairs1() throws Exception {
    }

    @Test
    public void isStopword() throws Exception {
    }

    @Test
    public void run() throws Exception {
    }

    @Test
    public void computeWeight() throws Exception {
    }

    @Test
    public void getWeight() throws Exception {
    }

}