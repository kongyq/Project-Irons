package edu.udel.irl.irons.mani;

import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Pair;
import edu.udel.irl.irons.IronsConfiguration;
import edu.udel.irl.irons.core.IroNode;
import edu.udel.irl.irons.synsim.ADWSynsetSimilarity;
import edu.udel.irl.irons.synsim.SynsetComparator;
import edu.udel.irl.irons.util.Stemmer;
import edu.udel.irl.irons.util.StopwordAnnotator;
import edu.udel.irl.irons.wsd.BabelfyDisambiguator;
import edu.udel.irl.irons.wsd.Disambiguator;
import gnu.trove.map.hash.TIntObjectHashMap;
import it.uniroma1.lcl.babelnet.InvalidBabelSynsetIDException;

import java.io.IOException;
import java.util.*;

/**
 * Created by mike on 4/23/18.
 * Core Algorithm of Project Mani
 * Utilized as computing node-node similarity
 */

public class CoreMani {

    private IroNode node1;
    private IroNode node2;

    private SemanticGraph sentence1;
    private SemanticGraph sentence2;

    private SynsetComparator synsetComparator;
    private Disambiguator disambiguator;

    // load config file to set below field
    private POS[] expectedPOSes = IronsConfiguration.getInstance().getExpectPOSes().chars().mapToObj(i -> POS.getPartOfSpeech((char)i)).toArray(POS[]::new);
    private boolean skipStopword = IronsConfiguration.getInstance().getSkipwordCondition();
    private double synsimThreshold = IronsConfiguration.getInstance().getSynSimThreshold();

    private Plex plex;
    //used for save BabelNet Coins when conduct wsd.
    private CoinSaver coinSaver;

    private static final int SEPARATOR = 1000;

    private HashMap<POS, ArrayList<IndexedWord>> wordList1;
    private HashMap<POS, ArrayList<IndexedWord>> wordList2;

    //Wsded word-sense hashmap
    private TIntObjectHashMap<List<String>> sent1Senses;
    private TIntObjectHashMap<List<String>> sent2Senses;

    public CoreMani(Disambiguator disambiguator, SynsetComparator synsetComparator) {
        this.disambiguator = disambiguator;
        this.synsetComparator = synsetComparator;

        //For saving BabelNet coins purpose only.
        this.coinSaver = new CoinSaver();
    }

    /**
     * initialize fields for new nodes.
     * @param node1
     * @param node2
     * @throws InvalidBabelSynsetIDException
     * @throws IOException
     */
    private void initialize(IroNode node1, IroNode node2) throws Exception {
        this.plex = new Plex();
        this.sentence1 = new SemanticGraph(parseSent(node1.getContent()));
        this.sentence2 = new SemanticGraph(parseSent(node2.getContent()));

        //This only for saving BabelNet coins purpose.
        if (this.coinSaver.isContain(node1)){
            this.sent1Senses = this.coinSaver.getSentSenses(node1);
//            System.out.println("Found1!");
        }else {
            this.sent1Senses = disambiguator.disambiguate(this.sentence1);
            this.coinSaver.addSentSenses(node1, this.sent1Senses);
        }

        if (this.coinSaver.isContain(node2)){
            this.sent2Senses = this.coinSaver.getSentSenses(node2);
//            System.out.println("Found2!");
        }else {
            this.sent2Senses = disambiguator.disambiguate(this.sentence2);
            this.coinSaver.addSentSenses(node2, this.sent2Senses);
        }

        this.wordList1 = this.initWordList();
        this.wordList2 = this.initWordList();
    }

    public double computeEdgeWeight(IroNode node1, IroNode node2) throws Exception {
        this.initialize(node1, node2);
        this.run(this.expectedPOSes, this.skipStopword);
        return this.computeWeight(this.synsimThreshold);
    }

    public SemanticGraph parseSent(String origSentence){
        return new Sentence(origSentence).dependencyGraph(SemanticGraphFactory.Mode.BASIC);
    }

    /***
     * Initial HashMap by adding 4 POS (n, v, r, a) for words in the documents.
     * @param
     */
    private HashMap initWordList(){
        HashMap<POS, ArrayList<IndexedWord>> wordList = new HashMap<>();
        wordList.put(POS.NOUN, new ArrayList<>());
        wordList.put(POS.VERB, new ArrayList<>());
        wordList.put(POS.ADVERB, new ArrayList<>());
        wordList.put(POS.ADJECTIVE, new ArrayList<>());
        return wordList;
    }

    /***
     * Convert coreNlp POS tag string to WordNet POS format
     * @param tag
     * @return
     */
    public static POS tagToPOS (String tag){
        String[] noun = {"NN", "NNS", "NNP", "NNPS"};
        String[] verb = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        String[] adv = {"RB", "RBR", "RBS"};
        String[] adj = {"JJ", "JJR", "JJS"};

        if (Arrays.asList(noun).contains(tag)){
            return POS.NOUN;
        }else if(Arrays.asList(verb).contains(tag)){
            return POS.VERB;
        }else if(Arrays.asList(adv).contains(tag)){
            return POS.ADVERB;
        }else if(Arrays.asList(adj).contains(tag)){
            return POS.ADJECTIVE;
        }else{
            return null;
        }
    }

    /**
     * acquire all word pair in the sentence.
     * @param sentence parsed sentence
     * @return set of word pairs
     */
    public static Set<Pair<IndexedWord, IndexedWord>> getAllAdjacentNodePairs(SemanticGraph sentence){
        return getAllAdjacentNodePairs(sentence, sentence.getFirstRoot());

    }


    /**
     * dependency parse tree mode has to be BASIC, since the recursive cannot handle cycle in the ENHANCE++ mode.
     * @param sentence Semantic Graph of the sentence
     * @param node IndexedWord of the word
     * @return node pairs include all edges of the dependency parse tree.
     */
    public static Set<Pair<IndexedWord, IndexedWord>> getAllAdjacentNodePairs(SemanticGraph sentence, IndexedWord node){
        Set<Pair<IndexedWord, IndexedWord>> nodePairs = new HashSet<>();
        if (sentence.hasChildren(node)){
            for (IndexedWord child: sentence.getChildren(node)){
                nodePairs.addAll(getAllAdjacentNodePairs(sentence, child));
                nodePairs.add(new Pair<>(node, child));
            }
        }
        return nodePairs;
    }

    /**
     * CORE:ALG add all nodes inside same sentence to the simplex stream.
     */
    private void addIntraConnection(){
        //use addVertex method to add just one node if the sentence only contain one node(word).
        if (this.sentence1.size() == 1){
            this.plex.addVertex(this.sentence1.getFirstRoot().index());
        }else {
            for (Pair<IndexedWord, IndexedWord> wordPair : getAllAdjacentNodePairs(this.sentence1)) {
                this.plex.addElement(wordPair.first().index(), wordPair.second().index(), 0D);
            }
        }
        //for sentence 2 and a gap(1000) to the vertex index to separate two sentences.
        if (this.sentence2.size() == 1){
            this.plex.addVertex(this.sentence1.getFirstRoot().index()+ SEPARATOR);
        }else {
            for (Pair<IndexedWord, IndexedWord> wordPair : getAllAdjacentNodePairs(this.sentence2)) {
                this.plex.addElement(wordPair.first().index()+ SEPARATOR,
                        wordPair.second().index()+ SEPARATOR,
                        0D);
            }
        }
    }

    /***
     * Create wordlist based on the POS of the indexedword by adding to the corresponding POS category.
     * @param sentence sentence need to add to the word list
     * @param wordList the word list
     */
    private void createWordList(SemanticGraph sentence, HashMap<POS, ArrayList<IndexedWord>> wordList, Boolean skipStopword){

        for (IndexedWord word : sentence.vertexSet()) {
            //POS pos = tagToPOS(word.tag());
            if (tagToPOS(word.tag()) == null) {
                continue;
            }
            //if word is stopword then skip
            if (skipStopword && isStopword(word)){
                continue;
            }
            wordList.get(tagToPOS(word.tag())).add(word);
        }

    }

    /***
     * Is the IndexedWord stopword
     * P.S. since the way this method called, we cannot use customized stopword list file, but only
     * the lucene stopword.
     * @param indexedWord indexedWord which need to check
     * @return true if the IndexedWord is a stopword
     */
    public static boolean isStopword(IndexedWord indexedWord){
        Pair<Boolean, Boolean> stopword = indexedWord.backingLabel().get(StopwordAnnotator.class);
        if (stopword.first() || stopword.second()){
            return true;
        }else{
            return false;
        }
    }

    private void addInterConnection(POS pos){
        Stemmer stemmer = new Stemmer();
        for (IndexedWord indexedWord1: this.wordList1.get(pos)){
            for (IndexedWord indexedWord2: this.wordList2.get(pos)){
                if (!this.sent1Senses.containsKey(indexedWord1.index()) &&
                        !this.sent2Senses.containsKey(indexedWord2.index())){
                    if (stemmer.stem(indexedWord1.word()).equalsIgnoreCase(stemmer.stem(indexedWord2.word()))){
                        this.plex.addElement(
                                indexedWord1.index(),
                                indexedWord2.index() + SEPARATOR,
                                0D);
                    }
                    continue;
                }

                if (this.sent1Senses.containsKey(indexedWord1.index()) &&
                        this.sent2Senses.containsKey(indexedWord2.index())){
                    double score = this.synsetComparator.compare(this.sent1Senses.get(indexedWord1.index()),
                            this.sent2Senses.get(indexedWord2.index()));

                    this.plex.addElement(
                            indexedWord1.index(),
                            indexedWord2.index() + SEPARATOR,
                            1D-score);
                }
            }
        }
    }


    public void run(POS[] supportedPOS, boolean skipStopword){

//        Stopwatch timer = Stopwatch.createStarted();
        this.addIntraConnection();
//        this.addIntraConnection(this.doc2SentList, 2);
//        System.out.println("Adding Intra-doc Elements: " + timer.stop());
//        timer.reset();
//        timer.start();
        this.createWordList(this.sentence1, this.wordList1, skipStopword);
        this.createWordList(this.sentence2, this.wordList2, skipStopword);
//        System.out.println("Creating POS-IndexedWord HashMap: " + timer.stop());
//        timer.reset();
//        timer.start();
        for (POS pos: supportedPOS){
            this.addInterConnection(pos);
        }
//        System.out.println("Adding Inter-docs Elements: " + timer.stop());
        this.plex.finalizeStream();
    }

    public double computeWeight(double threshold){
        this.plex.computeBarcode();
//        System.out.println(this.plex.getBarcode());
        return this.plex
                    .getIntervalsOfDim1()
                    .stream()
                    .mapToDouble(interval -> 1D - interval.getStart())
                    .filter(interval -> interval >= threshold)
                    .sum();
    }

    public void saveCache() throws IOException {
        this.coinSaver.writeToFile();
    }

    public void readCache() throws IOException, ClassNotFoundException {
        this.coinSaver.readFromFile();
    }

    /**
     * Test purpose only!
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
//        IroNode iroNode1 = new IroNode("1", 1, "A mathematician found a solution to the problem.", false);
//        IroNode iroNode2 = new IroNode("2", 2, "The problem was solved by a young mathematician.", false);
        IroNode iroNode1 = new IroNode("1", 1,
                "As scores of white farmers went into hiding to escape a round-up by Zimbabwean police, a senior Bush administration official called Mr Mugabe's rule \"illegitimate and irrational\" and said that his re-election as president in March was won through fraud.",
                false);
        IroNode iroNode2 = new IroNode("2", 2,
                "Prince William has told friends his mother was right all along to suspect her former protection officer of spying on her and he doesn't want any detective intruding on his own privacy.",
                false);

        Disambiguator disambiguator = new BabelfyDisambiguator();
        SynsetComparator synsetComparator = new ADWSynsetSimilarity();

        CoreMani coreMani = new CoreMani(disambiguator, synsetComparator);

//        coreMani.run(new POS[] {POS.NOUN, POS.VERB}, true);
//
//        coreMani.computeWeight(0.45D);

        System.out.println(coreMani.computeEdgeWeight(iroNode1,iroNode2));
        System.out.println(coreMani.computeEdgeWeight(iroNode2, iroNode1));
    }
}
