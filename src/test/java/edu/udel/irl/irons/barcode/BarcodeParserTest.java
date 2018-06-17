package edu.udel.irl.irons.barcode;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by mike on 6/8/18.
 */
public class BarcodeParserTest {

    private static final File barcodeFile = new File(System.getProperty("user.dir") + "/index/barcodesPwithR.txt");

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