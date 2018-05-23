package edu.udel.irl.irons.impl;

import edu.udel.irl.irons.util.CoreNlpUtil;

import java.io.File;

/**
 * Created by mike on 5/15/18.
 */
public class DocFeeder {

    private String docName;
    private CoreNlpUtil nlpUtil;

    public DocFeeder(){
    }

    public void read(File filename){
        try {
            this.docName = filename.getName();


        }catch (Exception e){
            System.out.println(e);
        }
    }
}
