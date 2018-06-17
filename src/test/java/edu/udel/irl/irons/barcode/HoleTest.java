package edu.udel.irl.irons.barcode;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/6/18.
 */
public class HoleTest {


    Hole hole = new Hole("[0.848857, infinity): [56,126] + [34,126] + [24,68] + [24,34] + [56,68]\n");

    @Test
    public void getDim() throws Exception {
        System.out.println(hole.getDim());
    }

    @Test
    public void isMinimum() throws Exception {
        System.out.println(hole.isMinimum());
    }

    @Test
    public void isUncovered() throws Exception {
        System.out.println(hole.isUncovered());
    }

    @Test
    public void isCoveredByHigherDimFacelet() throws Exception {
        System.out.println(hole.isCoveredByHigherDimFacelet());
    }

    @Test
    public void getStartTime() throws Exception {
        System.out.println(hole.getStartTime());
    }

    @Test
    public void getFacelets() throws Exception {
        for(Facelet facelet:hole.getFacelets()){
            System.out.print(facelet.getNodeIdList()+ " ");
            System.out.println(facelet.isPosPhase());
        }
        System.out.println(hole.getFacelets());
    }

    @Test
    public void getDocList() throws Exception {
        for(Facelet facelet: hole.getFacelets()){
            System.out.println(facelet.getDocList());
        }
        System.out.println(hole.getDocList());
    }

    @Test
    public void getNodeIdList() throws Exception{
        System.out.println(hole.getNodeIdList());

    }

}