package com.pluralsight;

//import log4j2 classes
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    //create a static logger
    final static Logger logger = LogManager.getLogger(App.class);

    //main method
    public static void main(String[] args) {
        logMeLikeYouDo("☕");
    }

    //logs messages with the input from above
    private static void logMeLikeYouDo(String input) {
        if (logger.isDebugEnabled()) {
            logger.debug("This is debug : " + input);
        }
        if (logger.isInfoEnabled()) {
            logger.info("This is info : " + input);
        }
        logger.warn("This is warn : " + input);
        logger.error("This is error : " + input);
        logger.fatal("This is fatal : " + input);
    }
}