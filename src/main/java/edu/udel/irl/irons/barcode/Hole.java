package edu.udel.irl.irons.barcode;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mike on 6/6/18.
 */
public class Hole {
    private int dim;
    private boolean isMinimum = true;
    private boolean isUncovered = false;
    private boolean isCoveredByHigherDimFacelet = false;
    private boolean isDocCoveredByHigherDimDoc = false;
    private double startTime;
    private Set<Facelet> facelets;
    private TIntHashSet nodeIdList;
    private TObjectIntHashMap<String> docList;
//    private Set<String> docList;

    public Hole(String line){
        this(Integer.parseInt(line.split("\\.")[0].substring(1)) + 1, line);
    }

    public Hole(int dim, String line){
        this(dim, line.split(": ")[0], line.split(": ")[1]);
    }

    private Hole(int dim, String barcode, String annotation){
        this.dim = dim;
        this.facelets = new HashSet<>();

        this.parseBarcode(barcode);
        this.parseAnnotation(annotation);
        this.createNodeIdAndDocList();
        if(this.facelets.size() > this.dim + 2){
            this.isMinimum = false;
        }
    }

    private void parseBarcode(String barcode){
        if (barcode.contains("infinity")){
            this.isUncovered = true;}
        this.startTime = Double.parseDouble(barcode.split(", ")[0].substring(1));
    }

    private void parseAnnotation(String annotation){
        for(String face: annotation.split(" \\+ ")){
            this.facelets.add(new Facelet(this.dim, this.startTime, face));
        }
    }

    public void createNodeIdAndDocList(){
        this.nodeIdList = new TIntHashSet();
        this.docList = new TObjectIntHashMap<>();
        for (Facelet facelet: this.facelets){
            TObjectIntIterator<String> iterator = facelet.getDocList().iterator();
            while(iterator.hasNext()){
                iterator.advance();
                this.docList.adjustOrPutValue(iterator.key(),iterator.value(),iterator.value());
            }
            this.nodeIdList.addAll(facelet.getNodeIdList());
//            this.docList.addAll(facelet.getDocList());
        }
    }

    public void setCoveredByHigherDimFacelet(boolean isCoveredByHigherDimFacelet){
        this.isCoveredByHigherDimFacelet = isCoveredByHigherDimFacelet;
    }

    public void setDocCoveredByHigherDimDoc(boolean isDocCoveredByHigherDimDoc){
        this.isDocCoveredByHigherDimDoc = isDocCoveredByHigherDimDoc;
    }

    public String toString(){
        String faces = "";
        for(Facelet facelet: this.facelets){
            faces += facelet.toString() + "  ";
        }
        return this.dim + ":\t" + String.format("%.6f",this.startTime) + ":\t" + faces + "|\t" + this.docList.toString();
    }

    public void setMinimum(boolean isMinimum){this.isMinimum = isMinimum;}

    public int getDim(){return this.dim;}

    public boolean isMinimum(){return this.isMinimum;}

    public boolean isUncovered(){return this.isUncovered;}

    public boolean isCoveredByHigherDimFacelet(){return this.isCoveredByHigherDimFacelet;}

    public boolean isDocCoveredByHigherDimDoc(){return this.isDocCoveredByHigherDimDoc;}

    public double getStartTime(){return this.startTime;}

    public Set<Facelet> getFacelets(){return this.facelets;}

    public TIntHashSet getNodeIdList(){return this.nodeIdList;}

    public TObjectIntHashMap<String> getDocList(){return this.docList;}

    public void addFacelet(Facelet facelet){
        this.facelets.add(facelet);
    }

    public void removeFacelet(Facelet facelet){
        this.facelets.remove(facelet);
    }
}
