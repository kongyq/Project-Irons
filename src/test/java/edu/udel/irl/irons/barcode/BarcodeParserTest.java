package edu.udel.irl.irons.barcode;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/8/18.
 */
public class BarcodeParserTest {

    private static final File barcodeFile = new File(System.getProperty("user.dir") + "/index/barcodes_New_Pipeline_MCS_Nasari_3_no_stopword_571.txt");

    BarcodeParser barcodeParser = new BarcodeParser();

    @Test
    public void parse() throws Exception {
        barcodeParser.parse(barcodeFile);
    }


    @Test
    public void display() throws Exception {
        barcodeParser.parse(barcodeFile);
        barcodeParser.display(false, true);
    }

}