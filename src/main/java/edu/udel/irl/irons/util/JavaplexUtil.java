package edu.udel.irl.irons.util;
import edu.stanford.math.plex4.api.Plex4;
import edu.stanford.math.plex4.autogen.homology.IntAbsoluteHomology;
import edu.stanford.math.plex4.homology.barcodes.AnnotatedBarcodeCollection;
import edu.stanford.math.plex4.homology.barcodes.BarcodeCollection;
import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.homology.chain_basis.SimplexComparator;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceAlgorithm;
import edu.stanford.math.plex4.homology.interfaces.AbstractPersistenceBasisAlgorithm;
import edu.stanford.math.plex4.streams.impl.ExplicitSimplexStream;
import edu.stanford.math.primitivelib.algebraic.impl.ModularIntField;
import edu.stanford.math.primitivelib.autogen.formal_sum.IntSparseFormalSum;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mike on 4/14/18.
 */
public class JavaplexUtil {

    public static void main(String[] args){

        Pair<String, String> test1= new MutablePair<>("a", "b");
        Pair<String, String> test2= new MutablePair<>("b", "a");

        Set<Integer> t1 = new HashSet<>(2);
        Set<Integer> t2 = new HashSet<>(2);

        t1.add(1);
        t1.add(2);
        t2.add(2);
        t2.add(1);


        System.out.println(t1.equals(t2));

        System.out.println(test1.equals(test2));
        ExplicitSimplexStream stream = new ExplicitSimplexStream(1.0D);

//        for (int i=1; i <= 20; i++){
//            stream.addVertex(i);
//        }
//
//        stream.addElement(new int[] {1,2},0D );
//        stream.addElement(new int[] {1,3},0D );
//        stream.addElement(new int[] {1,4},0D );
//        stream.addElement(new int[] {2,5},0D );
//        stream.addElement(new int[] {2,6},0D );
//        stream.addElement(new int[] {3,7},0D );
//        stream.addElement(new int[] {4,8},0D );
//        stream.addElement(new int[] {4,9},0D );
//        stream.addElement(new int[] {4,10},0D );
//
//        stream.addElement(new int[] {11,12},0D );
//        stream.addElement(new int[] {11,13},0D );
//        stream.addElement(new int[] {11,14},0D );
//        stream.addElement(new int[] {12,15},0D );
//        stream.addElement(new int[] {12,16},0D );
//        stream.addElement(new int[] {13,17},0D );
//        stream.addElement(new int[] {14,18},0D );
//        stream.addElement(new int[] {14,19},0D );
//        stream.addElement(new int[] {14,20},0D );
//
//        stream.addElement(new int[] {1,12},0.8D );
//        stream.addElement(new int[] {8,11},0.5D );
//        stream.addElement(new int[] {6,16},0.5D );
//        stream.addElement(new int[] {10,20},0.8D );

//        for (int i = 1; i <= 8; i ++){
//            stream.addVertex(i,0D);
//        }
//        stream.addElement(new int[] {1,2},0 );
//        stream.addElement(new int[] {2,3},0 );
//        stream.addElement(new int[] {3,4},0 );
//        stream.addElement(new int[] {5,6},0 );
//        stream.addElement(new int[] {6,7},0 );
//        stream.addElement(new int[] {7,8},0 );
//        stream.addElement(new int[] {1,5},0.8D );
//        stream.addElement(new int[] {2,6},0.5D );
//        stream.addElement(new int[] {3,7},0.5D );
//        stream.addElement(new int[] {4,8},0.8D );

        stream.addElement(new int[]{1,2});
        stream.addElement(new int[]{1,3});
        stream.addElement(new int[]{2,4});
        stream.addElement(new int[]{3,4});
        stream.addElement(new int[]{3,5});
        stream.addElement(new int[]{4,6});
        stream.addElement(new int[]{5,6});

//        stream.addVertex(0);
//        stream.addVertex(1);
//        stream.addVertex(2);
//        stream.addVertex(3);
//        stream.addElement(new int[] {0,1});
//        stream.addElement(new int[] {0,2});
//        stream.addElement(new int[] {0,3});
//        stream.addElement(new int[] {1,2});
//        stream.addElement(new int[] {1,3});
//        stream.addElement(new int[] {2,3});
        stream.finalizeStream();

        stream.validateVerbose();

        AbstractPersistenceAlgorithm<Simplex> algorithm = Plex4.getModularSimplicialAlgorithm(2,2);

        AbstractPersistenceBasisAlgorithm<Simplex, IntSparseFormalSum<Simplex>>
                algorithm1 = (AbstractPersistenceBasisAlgorithm<Simplex, IntSparseFormalSum<Simplex>>)
                Plex4.getModularSimplicialAlgorithm(2, 2);

        BarcodeCollection barcode = algorithm.computeIntervals(stream);
        AnnotatedBarcodeCollection annotatedBarcodeCollection = algorithm1.computeAnnotatedIntervals(stream);

        IntAbsoluteHomology<Simplex> intAbsoluteHomology = new IntAbsoluteHomology<>(ModularIntField.getInstance(2),
                SimplexComparator.getInstance(),
                0, 2);

        AnnotatedBarcodeCollection annotatedBarcodeCollection1 = intAbsoluteHomology.computeAnnotatedIntervals(stream);

        System.out.println(barcode);
        System.out.println(annotatedBarcodeCollection1);
    }
}

