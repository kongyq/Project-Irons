package edu.udel.irl.irons;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by mike on 5/9/18.
 */


public class IronsConfiguration {
    private PropertiesConfiguration config = null;
    private static final Logger LOGGER = Logger.getLogger(IronsConfiguration.class.getName());
    private static IronsConfiguration instance = null;
    private static String CONFIG_DIR = "config/";
    public static String CONFIG_FILE = "irons.properties";

    private IronsConfiguration(){
        File configFile = new File(CONFIG_DIR, CONFIG_FILE);
        boolean bDone = false;
        if(configFile.exists()) {
            LOGGER.info("Loading " + CONFIG_FILE + " FROM " + configFile.getAbsolutePath());

            try {
                this.config = new PropertiesConfiguration(configFile);
                bDone = true;
            } catch (ConfigurationException exception) {
                exception.printStackTrace();
            }
        }

        if(!bDone) {
            LOGGER.info(CONFIG_FILE + " is missing. Please check that the file is available in the config folder.");
            LOGGER.info("Irons starts with empty configuration");
            this.config = new PropertiesConfiguration();
        }
    }

    public static synchronized IronsConfiguration getInstance() {
        if(instance == null) {
            instance = new IronsConfiguration();
        }

        return instance;
    }

    public void setConfigurationFile(File configurationFile) {
        LOGGER.info("Changing configuration properties to " + configurationFile);

        try {
            this.config = new PropertiesConfiguration(configurationFile);
            this.config.setBasePath(configurationFile.getParentFile().getAbsolutePath());
        } catch (ConfigurationException e) {
            e.printStackTrace();
            LOGGER.info("Setting Irons to an empty configuration");
            this.config = new PropertiesConfiguration();
        }

    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
        if(c != null && string != null) {
            try {
                return Enum.valueOf(c, string.trim().toUpperCase());
            } catch (IllegalArgumentException var3) {
                ;
            }
        }

        return null;
    }

    public String getDisambiguator(){return this.config.getString("mani.disambiguator");}

    public String getSynsetComparator(){return this.config.getString("mani.synsetComparator");}

    public boolean getSkipwordCondition(){return Boolean.parseBoolean(this.config.getString("mani.skipStopword"));}

    public double getSynSimThreshold(){return this.config.getDouble("mani.threshold");}

    public String getExpectPOSes(){return this.config.getString("mani.POS");}

    public String getIndexPath(){return this.config.getString("index.path");}

    public String getBarcodePath(){return this.config.getString("index.barcode.path");}

    public double getSensitiveness() {
        double value = this.config.getDouble("irons.edge.sensitiveness");
        if (value == 0D) {
            return Math.E;
        } else {
            return value;
        }
    }

    public String getBabelfycache() {return this.config.getString("babelfy.coinsaver.path");}

}
