package edu.udel.irl.irons.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/12/18.
 */
public class CoreNlpUtilTest {
    @Test
    public void parseOneSentenceDoc() throws Exception {
    }

    @Test
    public void getSentList() throws Exception {
        System.out.println(CoreNlpUtil.getInstance().getSentList("As scores of white farmers went into hiding to escape a round-up by Zimbabwean police, a senior Bush administration official called Mr Mugabe's rule \\\"illegitimate and irrational\\\" and said that his re-election as president in March was won through fraud."));
    }

    @Test
    public void getInstance() throws Exception {
    }

}