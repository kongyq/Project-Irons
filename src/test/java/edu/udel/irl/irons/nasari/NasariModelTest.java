package edu.udel.irl.irons.nasari;

import com.google.common.base.Stopwatch;
import edu.udel.irl.irons.IronsConfiguration;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/19/18.
 * FastUtil collections version
 */
public class NasariModelTest {

    private NasariModel model = NasariModel.getInstance(new File(IronsConfiguration.getInstance().getNASARIVectorFile()));

    @Test
    public void loadVectorFile() throws Exception {
    }

    @Test
    public void getVectors() throws Exception {
//        Stopwatch stopwatch = Stopwatch.createUnstarted();
//        Random random = new Random();
//
//        for(int j = 0; j<10; j++) {
//
//            stopwatch.start();
//            for (int i = 0; i < 100000; i++) {
//                model.getVectors(random.nextInt(2867355) + 1);
//            }
//
////        System.out.println(model.getVectors(1));
//            stopwatch.stop();
//            System.out.println(stopwatch);
//        }
////        System.out.println(model.getVectors(2));
////        System.out.println(model.getVectors(10));
////        System.out.println(model.getVectors(17368606));
//    }
        System.out.println(model.getVectors(1));
//        System.out.println(model.getVectors(2));
//        System.out.println(model.getVectors(10));
//        System.out.println(model.getVectors(17368606));
    }

    @Test
    public void getInstance1() throws Exception {
    }
}


