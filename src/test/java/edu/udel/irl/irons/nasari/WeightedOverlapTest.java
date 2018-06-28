package edu.udel.irl.irons.nasari;

import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/25/18.
 */
public class WeightedOverlapTest {
    @Test
    public void compare() throws Exception {

//        System.out.println(Arrays.equals(new byte[]{'c', 'b', 'a'}, new byte[]{'c','b','a'}));
        System.out.println(WeightedOverlap.<Integer> compare(new Integer[]{1, 2, 3}, new Integer[] {3,2,1}));
//        System.out.println(WeightedOverlap.<Byte> compare(new Byte[]{'1','2','3'}, new Byte[]{'3','2','1'}));
//        System.out.println(new byte[]{'a','b','c'}.equals(new byte[]{'a','b','c'}));
        System.out.println(WeightedOverlap.compare(
                    new byte[][]{
                            {'a', 'b', 'b'},
                            {'c', 'd', 'd'},
                            {'d', 'e', 'e'}},
                    new byte[][]{
                            {'a', 'b', 'b'},
                            {'c', 'd', 'd'},
                            {'d', 'e', 'e'}}
            ));
        System.out.println(WeightedOverlap.compare(new byte[][]{
                        {'a', 'b', 'b'},
                        {'c', 'd', 'd'},
                        {'d', 'e', 'e'}},
                new byte[][]{
                        {'d', 'e', 'e'},
                        {'c', 'd', 'd'},
                        {'a', 'b', 'b'}}));
        Stopwatch stopwatch = Stopwatch.createStarted();
        for(int i = 0; i < 1000000; ++i) {
            WeightedOverlap.compare(new byte[][]{
                            {'a', 'b', 'b'},
                            {'c', 'd', 'd'},
                            {'d', 'e', 'e'}},
                    new byte[][]{
                            {'d', 'e', 'e'},
                            {'c', 'd', 'd'},
                            {'a', 'b', 'b'}});
//            System.out.println(WeightedOverlap.compare(
//                    new byte[][]{
//                            {'a', 'b', 'b'},
//                            {'c', 'd', 'd'},
//                            {'d', 'e', 'e'}},
//                    new byte[][]{
//                            {'d', 'e', 'e'},
//                            {'c', 'd', 'd'},
//                            {'a', 'b', 'b'}}
//            ));
//            System.out.println(WeightedOverlap.compare(
//                    new byte[][]{
//                            {'a', 'b', 'b'},
//                            {'c', 'd', 'd'},
//                            {'d', 'e', 'e'}},
//                    new byte[][]{
//                            {'a', 'b', 'b'},
//                            {'c', 'd', 'd'},
//                            {'d', 'e', 'e'}}
//            ));
        }
        System.out.println(stopwatch.stop());
        HashSet<Byte> testss = new HashSet<>();
    }



}