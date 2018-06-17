package edu.udel.irl.irons.barcode;

import edu.udel.irl.irons.util.IndexReader;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mike on 6/6/18.
 */
public class Facelet {

    private int dim;
    private double startTime;
    private boolean posPhase;
    private TIntArrayList nodeIdList;
    private TObjectIntHashMap<String> docList;
//    private Set<String> docList;

    public Facelet(int dim, double startTime, String face){
        this.dim = dim;
        this.startTime = startTime;
        if(face.startsWith("-")){
            this.posPhase = false;
        }else {this.posPhase = true;}
        this.nodeIdList = new TIntArrayList();
        this.docList = new TObjectIntHashMap();
//        this.docList = new HashSet<>();
        this.createNodeIdAndDocList(face);
    }

    private void createNodeIdAndDocList(String face){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(face);
        while (matcher.find()){
            this.nodeIdList.add(Integer.parseInt(matcher.group()));
            try {
                this.docList.adjustOrPutValue(IndexReader.getInstance().getDocId(Integer.parseInt(matcher.group())),
                        1,1);
//                this.docList.add(IndexReader.getInstance().getDocId(Integer.parseInt(matcher.group())));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean equals(Object object){
        if(object instanceof Facelet && ((Facelet)object).nodeIdList.equals(this.getNodeIdList())){
            return true;
        }else {return false;}
    }

    public int hashCode(){
        return this.nodeIdList.hashCode();
    }

    public String toString(){
        return this.nodeIdList.toString();
    }

    public int getDim(){return this.dim;}

    public double getStartTime(){return this.startTime;}

    public boolean isPosPhase(){return this.posPhase;}

    public TIntArrayList getNodeIdList(){return this.nodeIdList;}

    public TObjectIntHashMap<String> getDocList(){return this.docList;}

}