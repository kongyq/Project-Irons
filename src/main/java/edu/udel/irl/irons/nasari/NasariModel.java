package edu.udel.irl.irons.nasari;

import java.io.File;

/**
 * Created by mike on 6/28/18.
 */
public interface NasariModel <T> {

    T getVectors(int synset);

}
