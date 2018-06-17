package edu.udel.irl.irons.barcode;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 6/5/18.
 */
public class BarcodeParser {

    private int highestDim;
    private TIntObjectHashMap<ArrayList<Hole>> holes;

    public BarcodeParser(){
        this.holes = new TIntObjectHashMap();
    }

    public void parse(File barcodeFile) throws IOException {
        List<String> allLines = new ArrayList(Files.readAllLines(barcodeFile.toPath()));
        int currentDim = 0;
        for(String line: allLines){

            if(line.matches(" *")){break;}

            if(line.startsWith("Dimension")){
                currentDim = Integer.parseInt(line.split(": ")[1]);
                this.holes.put(currentDim, new ArrayList<>());
            }else{
                this.holes.get(currentDim).add(new Hole(currentDim, line));
            }
        }
        this.highestDim = currentDim;

//        System.out.println("simplifying");
        TIntObjectIterator<ArrayList<Hole>> iterator = this.holes.iterator();
        while(iterator.hasNext()){
            iterator.advance();
            for(Hole hole: iterator.value()){
                if(hole.isMinimum() == false) {
                    this.simplifyNonminimumHole(hole);
                }
            }
        }

//        System.out.println("coverage checking");
        this.checkCoverage();

    }


    private void simplifyNonminimumHole(Hole nonMinimumHole){
//        System.out.println(nonMinimumHole);
        boolean modified = true;
        while(modified) {
            modified = false;
            for (Hole hole : this.holes.get(nonMinimumHole.getDim())) {

                if(hole.equals(nonMinimumHole)){continue;}

                if (nonMinimumHole.getNodeIdList().containsAll(hole.getNodeIdList())) {
//                    System.out.println("found!");
//                    System.out.println(hole);
                    for (Facelet facelet : hole.getFacelets()) {
//                        System.out.println(nonMinimumHole.getFacelets());
//                        System.out.println(facelet);
                        if (nonMinimumHole.getFacelets().contains(facelet)) {
                            nonMinimumHole.removeFacelet(facelet);
                        } else {
                            nonMinimumHole.addFacelet(facelet);
                        }
                    }
                    //recreate NodeIdList and DocList based on new facelets
                    nonMinimumHole.createNodeIdAndDocList();
//                    System.out.println("new: " + nonMinimumHole.getFacelets());
                    modified = true;
                }
            }
//            break;
        }
        nonMinimumHole.setMinimum(true);
    }

    private void simplifyUncoveredHole(){

    }

    private void checkCoverage(){
        for (int currentDim = 1; currentDim < this.highestDim; currentDim++){
            for(Hole lowerDimHole: this.holes.get(currentDim)){
                for(Hole higherDimHole: this.holes.get(currentDim+1)){
                    if(higherDimHole.getNodeIdList().containsAll(lowerDimHole.getNodeIdList())){
                        lowerDimHole.setCoveredByHigherDimFacelet(true);
                        lowerDimHole.setDocCoveredByHigherDimDoc(true);
                        break;
                    }else if(higherDimHole.getDocList().keySet().containsAll(lowerDimHole.getDocList().keySet())){
                        lowerDimHole.setDocCoveredByHigherDimDoc(true);
                    }
                }
            }
        }
    }

    public void display(){
        this.display(false, false);
    }

    public void display(boolean isNodeIndependent, boolean isDocIndependent){
        for(int dim: this.holes.keys()){
            System.out.println("Dimension: " + dim);
            for(Hole hole: this.holes.get(dim)){

                //isDocIndependent will override isNodeIndependent if its value is true.
                if(isDocIndependent == true){
                    if(hole.isDocCoveredByHigherDimDoc() == false){
                        System.out.println(hole);
                    }
                    continue;
                }

                if(isNodeIndependent == false){
                    System.out.println(hole);
                }else{
                    if(hole.isCoveredByHigherDimFacelet() == false){
                        System.out.println(hole);
                    }
                }
            }
        }
    }
}
