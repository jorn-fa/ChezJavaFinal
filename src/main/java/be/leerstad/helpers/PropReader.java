package be.leerstad.helpers;


import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Hiel Jorn
 * @version 1.0, jan 2018
 * @since 1.0
 */

public abstract class PropReader {


    /**
     * @param bestand Requires property file
     * @return Property file
     */
    public static Properties ReadProperties(String bestand) {

        Logger logger = Logger.getLogger("dbase");

        Properties eigenschappen = new Properties();

        try {
            FileInputStream in = new FileInputStream(bestand);
            eigenschappen.load(in);
            logger.debug("loaded from config file: " + bestand );
        }

        catch (IOException ioe)
        {
            logger.error("File not found: " + bestand);
        }

        return eigenschappen;
    }

}
