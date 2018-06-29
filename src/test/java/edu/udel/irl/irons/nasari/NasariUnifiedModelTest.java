package edu.udel.irl.irons.nasari;

import edu.udel.irl.irons.IronsConfiguration;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/28/18.
 */
public class NasariUnifiedModelTest {

    private NasariModel model = NasariModel.getInstance(new File(IronsConfiguration.getInstance().getNASARIVectorFile()));

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
        System.out.println(model.getVectors(2));
        System.out.println(model.getVectors(10));
        System.out.println(model.getVectors(17368606));
    }

}