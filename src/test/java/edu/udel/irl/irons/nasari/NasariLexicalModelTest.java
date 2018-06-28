package edu.udel.irl.irons.nasari;

import edu.udel.irl.irons.IronsConfiguration;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/26/18.
 */
public class NasariLexicalModelTest {


    NasariLexicalModel model = NasariLexicalModel.getInstance(new File(IronsConfiguration.getInstance().getNASARIVectorFile()));
    @Test
    public void loadLemmaFile() throws Exception {
    }

    @Test
    public void countTabs() throws Exception {
    }

    @Test
    public void getInstance() throws Exception {
    }

    @Test
    public void getVectors() throws Exception {
        for(String lemma: model.getVectors(1)){
            System.out.print(lemma + ' ');
        }
        System.out.println();
        for(String lemma: model.getVectors(2)){
            System.out.print(lemma + ' ');
        }
        System.out.println();
        for(String lemma: model.getVectors(10)){
            System.out.print(lemma + ' ');
        }
        System.out.println();
    }
}