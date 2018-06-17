package edu.udel.irl.irons.core;

/**
 * Created by mike on 4/18/18.
 */
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.udel.irl.irons.util.CoreNlpUtil;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mike on 3/26/18.
 */
public class IroNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final AtomicInteger uid = new AtomicInteger();

//    private String content;
    private SemanticGraph content;
    private int nodeID;
    private String docID;
    private int sentID;
    private boolean segmentation;

    public IroNode(String docID, int sentID, String content, boolean segmentation){
        this(docID, sentID, CoreNlpUtil.getInstance().parseOneSentenceDoc(content), segmentation);
    }

    public IroNode(String docID, int sentID, SemanticGraph content, boolean segmentation){
        this.content = content;
        this.docID = docID;
        this.sentID = sentID;
        this.segmentation = segmentation;

        this.nodeID = this.uid.incrementAndGet();
    }

    public SemanticGraph getContent(){return this.content;}

    public String getText(){return this.content.toString();}

    public int getNodeID(){return this.nodeID;}

    public String getDocID(){return this.docID;}

    public int getSentID(){return this.sentID;}

    public boolean isSegmentation(){return this.segmentation;}
}
